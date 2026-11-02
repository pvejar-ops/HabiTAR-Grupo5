"""Agente de Informe: Generación del reporte explicativo con guardarraíles y trazabilidad."""

import time
from typing import Dict, Any, Tuple, Optional
from app.core.config import settings
from app.services.agents.prompts import build_system_instruction
from app.services.guardrails.output_filter import evaluate_output
from app.services.guardrails.input_filter import MENSAJE_DERIVACION

# Intento de inicialización del SDK oficial de Gemini
gemini_client = None
if settings.GEMINI_API_KEY:
    try:
        from google import genai
        gemini_client = genai.Client(api_key=settings.GEMINI_API_KEY)
    except Exception:
        gemini_client = None


class ReportAgent:
    """
    Agente que orquesta la generación de texto con Gemini y audita la salida con el filtro de seguridad.
    """

    def __init__(self, client=None, model: str = None):
        self.client = client or gemini_client
        self.model = model or settings.GEMINI_MODEL

    def generate_report(
        self,
        query: str,
        profile_text: str,
        context_text: str,
    ) -> Tuple[str, Dict[str, Any], bool]:
        """Genera el informe orientativo descriptivo.

        Retorna:
            (texto_respuesta, metadata, fue_bloqueada_por_filtro)
        """
        system_instruction = build_system_instruction(profile_text, context_text)
        meta = {"elapsed_sec": 0.0, "simulated": self.client is None}

        # Modo simulado si no hay clave de API configurada
        if self.client is None:
            simulated_text = (
                "1) Lo que compartiste: Un perfil de usuario con necesidades de anticipación y apoyo en entornos estructurados.\n"
                "2) Orientación: Se sugiere priorizar agendas visuales y descansos sensoriales [doc01_screening_vs_diagnostico#1].\n"
                "3) Alcance: Orientación psicoeducativa orientativa, no diagnóstica."
            )
            return simulated_text, meta, False

        t0 = time.time()
        try:
            from google.genai import types
            response = self.client.models.generate_content(
                model=self.model,
                contents=query,
                config=types.GenerateContentConfig(
                    system_instruction=system_instruction,
                    temperature=0.2,
                    max_output_tokens=1024,
                ),
            )
            meta["elapsed_sec"] = round(time.time() - t0, 2)
            generated_text = response.text or ""
        except Exception as e:
            meta["elapsed_sec"] = round(time.time() - t0, 2)
            meta["error"] = str(e)
            return f"(Error al consultar el modelo generativo: {e})", meta, False

        # Auditoría de salida (Post-guardrail)
        prohibiciones = evaluate_output(generated_text)
        if prohibiciones:
            meta["output_blocked_reasons"] = prohibiciones
            return MENSAJE_DERIVACION, meta, True

        return generated_text, meta, False
