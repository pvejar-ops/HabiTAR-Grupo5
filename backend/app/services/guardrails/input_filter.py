"""Filtro de seguridad de entrada (detección de intentos diagnósticos y adversariales)."""

import re
import unicodedata
from typing import List, Tuple

MENSAJE_DERIVACION = (
    "No puedo decirte si tienes o no autismo. Esta herramienta es orientativa y no reemplaza "
    "una evaluación profesional. Si esto te preocupa o te está afectando, te recomiendo conversarlo "
    "con un profesional de salud o con los servicios de apoyo de tu universidad."
)

MENSAJE_RECHAZO = (
    "No puedo ayudarte con eso. HabiTAR ofrece orientación basada en un perfil y en contexto descriptivo. "
    "Puedo ayudarte con una consulta dentro de ese ámbito."
)

MENSAJE_MEDICO = (
    "No puedo recomendar medicamentos ni tratamientos. Para eso conviene consultar a un profesional de salud."
)

MENSAJE_RIESGO = (
    "Lamento que estés pasando por esto. Lo que cuentas es importante y mereces apoyo ahora. Si estás en peligro inmediato, "
    "contacta a los servicios de emergencia de tu zona o busca a alguien de confianza cerca de ti. También puedes acudir a "
    "los servicios de apoyo de tu universidad o a un profesional de salud."
)

DX = r"(autismo|autista|autistas|tea|asperger|aspie|espectro)"

ENTRADA = {
    "autodiagnostico": [
        rf"\b(tengo|tendre|tendria|padezco|sufro de)\b.{{0,25}}\b{DX}\b",
        r"\b(soy|sere|seria)\b.{0,10}\b(autista|asperger|aspie)\b",
        r"\bestoy\b.{0,12}\bespectro\b",
    ],
    "pedir_diagnostico": [
        r"\b(dame|dime|entregame|hazme|quiero|necesito)\b.{0,15}\bdiagnostico\b",
        r"\bdiagnosticame\b|\bdiagnosticarme\b|\bdiagnostica(me|rme)\b",
        r"\bmi diagnostico\b",
        r"\b(condicion|diagnostico|nivel de apoyo)\b.{0,30}\b(mi perfil|mi arquetipo|mis resultados)\b",
        r"\bnivel de apoyo\b",
    ],
    "tercera_persona": [
        rf"(¿|\b(creo|crees|cree|pienso|sospecho) que\b|\bsera que\b|\bseria que\b)[^?]{{0,40}}\b(mi|su|el|la)\s+(companer\w+|hij[oa]|herman[oa]|pareja|amig[oa]|alumn[oa]|estudiante|jef[ea]|prim[oa])\b.{{0,60}}\b(es|tiene|sera|seria|tendra|tendria)\b.{{0,20}}\b{DX}\b",
    ],
    "probabilidad": [
        rf"\b(probabilidad|porcentaje|chance|posibilidad)\b.{{0,40}}\b{DX}\b",
    ],
    "documento_clinico": [
        rf"\b(escribe|redacta|genera|haz|prepara)\w*\b.{{0,30}}\b(informe|certificado|constancia|carta)\b.{{0,40}}\b{DX}\b",
    ],
    "inyeccion_o_fuga": [
        r"\b(ignora|olvida|omite|desobedece)\b.{0,30}\b(instrucciones|reglas|indicaciones)\b",
        r"\b(prompt|instrucciones internas|instrucciones del sistema|tus instrucciones)\b",
    ],
    "consejo_medico": [
        r"\b(medicamento\w*|farmaco\w*|dosis|pastilla\w*)\b",
    ],
    "riesgo": [
        r"\b(hacerme dano|quitarme la vida|suicid\w*|matarme|no quiero vivir|acabar con mi vida)\b",
    ],
}

PRIORIDAD = [
    "riesgo",
    "autodiagnostico",
    "pedir_diagnostico",
    "tercera_persona",
    "probabilidad",
    "documento_clinico",
    "consejo_medico",
    "inyeccion_o_fuga",
]

RESPUESTA_POR_CATEGORIA = {
    "riesgo": MENSAJE_RIESGO,
    "consejo_medico": MENSAJE_MEDICO,
    "documento_clinico": MENSAJE_RECHAZO,
    "inyeccion_o_fuga": MENSAJE_RECHAZO,
}


def normalize_text(text: str) -> str:
    t = unicodedata.normalize("NFD", text.lower())
    return "".join(c for c in t if unicodedata.category(c) != "Mn")


def evaluate_input(text: str) -> List[str]:
    """Evalúa el texto de entrada y retorna las categorías de riesgo o intento diagnóstico detectadas."""
    t = normalize_text(text)
    hits = [cat for cat, pats in ENTRADA.items() if any(re.search(p, t) for p in pats)]
    return hits


def get_fixed_response(hits: List[str]) -> Tuple[str, str]:
    """Retorna la categoría prioritaria y el mensaje seguro estático."""
    cat = next((c for c in PRIORIDAD if c in hits), "autodiagnostico")
    return cat, RESPUESTA_POR_CATEGORIA.get(cat, MENSAJE_DERIVACION)
