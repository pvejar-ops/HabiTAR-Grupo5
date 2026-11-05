"""Agente de Informe: Generación del reporte explicativo con guardarraíles y trazabilidad."""

import time
import re
from typing import Dict, Any, Tuple, Optional, List
from app.core.config import settings
from app.services.agents.prompts import build_system_instruction
from app.services.guardrails.output_filter import evaluate_output
from app.services.guardrails.input_filter import MENSAJE_DERIVACION

# Modelos candidatos en orden de preferencia según la SDK google-genai
FALLBACK_MODELS = [
    "gemini-2.5-flash",
    "gemini-2.0-flash",
    "gemini-1.5-flash-latest",
    "gemini-1.5-flash",
    "gemini-1.5-pro",
]


def get_gemini_client():
    """Inicializa y retorna el cliente de Google GenAI si la API key está disponible."""
    api_key = settings.GEMINI_API_KEY
    if api_key and api_key != "your_gemini_api_key_here":
        try:
            from google import genai
            return genai.Client(api_key=api_key)
        except Exception:
            return None
    return None


class ReportAgent:
    """
    Agente que orquesta la generación de texto con Gemini y audita la salida con el filtro de seguridad.
    Cuenta con cadena de contingencia de modelos (fallback) y síntesis segura contextual.
    """

    def __init__(self, client=None, model: str = None):
        self._client = client
        self.model = model or settings.GEMINI_MODEL

    @property
    def client(self):
        if self._client is not None:
            return self._client
        return get_gemini_client()

    def _extract_primary_citation(self, context_text: str) -> str:
        """Extrae el primer identificador de fragmento disponible en el contexto para las citas."""
        match = re.search(r"\[(doc\d+_[^#\]]+#\d+)\]", context_text)
        return match.group(1) if match else "doc01_screening_vs_diagnostico#1"

    def _build_fallback_response(
        self, profile_text: str, context_text: str, error_detail: Optional[str] = None
    ) -> str:
        """Genera una orientación estructurada y trazable de contingencia a partir del corpus recuperado."""
        citation = self._extract_primary_citation(context_text)

        # Extraer necesidades o estresores si están en el perfil
        necesidades = "estrategias de autorregulación y adaptación al entorno universitario"
        for line in profile_text.splitlines():
            if "Necesidades UX:" in line:
                necesidades = line.replace("Necesidades UX:", "").strip()
                break

        return (
            f"1) Lo que compartiste: Perfil orientativo con foco en {necesidades}.\n"
            f"2) Orientación: Se sugiere planificar anticipadamente los cambios de rutina, estructurar "
            f"espacios de menor sobrecarga sensorial y utilizar apoyos visuales para reducir la fatiga [{citation}].\n"
            f"3) Alcance: Orientación descriptiva de carácter psicoeducativo. No constituye ni reemplaza un diagnóstico clínico."
        )

    def generate_report(
        self,
        query: str,
        profile_text: str,
        context_text: str,
    ) -> Tuple[str, Dict[str, Any], bool]:
        """Genera el informe orientativo descriptivo con trazabilidad y guardarraíles.

        Retorna:
            (texto_respuesta, metadata, fue_bloqueada_por_filtro)
        """
        system_instruction = build_system_instruction(profile_text, context_text)
        client = self.client
        meta: Dict[str, Any] = {"elapsed_sec": 0.0, "simulated": client is None}

        # Modo simulado si no hay clave de API válida configurada
        if client is None:
            simulated_text = self._build_fallback_response(profile_text, context_text)
            return simulated_text, meta, False

        # Cadena de modelos a intentar (evita fallos por diferencias de versión/nombres en Google AI Studio)
        candidate_models: List[str] = []
        for m in [self.model] + FALLBACK_MODELS:
            if m and m not in candidate_models:
                candidate_models.append(m)

        generated_text: Optional[str] = None
        last_error: Optional[Exception] = None
        t0 = time.time()

        for current_model in candidate_models:
            try:
                from google.genai import types
                response = client.models.generate_content(
                    model=current_model,
                    contents=query,
                    config=types.GenerateContentConfig(
                        system_instruction=system_instruction,
                        temperature=0.2,
                        max_output_tokens=1024,
                    ),
                )
                if response and response.text:
                    generated_text = response.text
                    meta["model_used"] = current_model
                    break
            except Exception as e:
                last_error = e
                continue

        meta["elapsed_sec"] = round(time.time() - t0, 2)

        # Si todos los modelos remotos fallaron (ej: clave sin cuota o error 404), aplicar síntesis contextual segura
        if not generated_text:
            meta["fallback_mode"] = True
            meta["model_error"] = str(last_error) if last_error else "Modelo no disponible"
            fallback_text = self._build_fallback_response(profile_text, context_text, str(last_error))
            return fallback_text, meta, False

        # Auditoría de salida (Post-guardrail)
        prohibiciones = evaluate_output(generated_text)
        if prohibiciones:
            meta["output_blocked_reasons"] = prohibiciones
            return MENSAJE_DERIVACION, meta, True

        return generated_text, meta, False
