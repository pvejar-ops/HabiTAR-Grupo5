"""Filtro de seguridad de salida (auditoría post-generación de Cero Diagnóstico)."""

import re
import unicodedata
from typing import List

SALIDA_PROHIBIDA = [
    (r"(?<!si )\b(tienes|presentas|cumples)\b.{0,15}\b(autismo|tea|asperger)\b", "afirma condición"),
    (r"\beres\b.{0,10}\b(autista|asperger)\b", "afirma condición"),
    (
        r"\b(tu resultado|tu puntaje)\b.{0,30}\b(indica|confirma|demuestra)\b.{0,30}\b(autis\w*|tea)\b",
        "interpreta puntaje como diagnóstico",
    ),
    (
        r"\b(probablemente|es probable que|seguramente)\b.{0,25}\b(tengas|seas|tienes|eres)\b",
        "probabilidad diagnóstica",
    ),
    (r"\b(umbral|punto de corte|corte)\b.{0,20}\b100\b", "umbral no respaldado"),
    (r"\bpuntaje t\b.{0,20}\b(\d{2,3})\b", "puntaje T del SRS-2"),
]


def normalize_text(text: str) -> str:
    t = unicodedata.normalize("NFD", text.lower())
    return "".join(c for c in t if unicodedata.category(c) != "Mn")


def evaluate_output(text: str) -> List[str]:
    """Evalúa la salida generada y retorna los motivos de infracción diagnóstica encontrados."""
    t = normalize_text(text)
    return [motivo for pat, motivo in SALIDA_PROHIBIDA if re.search(pat, t)]
