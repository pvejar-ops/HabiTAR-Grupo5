"""Agente Selector: Evaluación de contexto del usuario y sanitización del perfil (Ley 21.719)."""

from typing import Dict, Any, List

CAMPOS_PERMITIDOS = [
    "id_usuario",
    "nombre_ficticio",
    "arquetipo",
    "necesidades_ux",
    "estresores",
    "implicacion_habitar",
    "resumen_scores",
]

CAMPOS_EXCLUIDOS = ["condicion", "diagnostico", "nivel_de_apoyo", "etiqueta_clinica"]


class SelectorAgent:
    """
    Agente responsable de evaluar y sanitizar la información del perfil del usuario,
    garantizando que ninguna etiqueta clínica previa o dato sensible llegue al modelo generativo.
    """

    @staticmethod
    def sanitize_profile(raw_profile: Dict[str, Any]) -> Dict[str, Any]:
        """Filtra y elimina campos sensibles o diagnósticos."""
        clean_profile = {}
        for k, v in raw_profile.items():
            if k in CAMPOS_PERMITIDOS:
                clean_profile[k] = v
        return clean_profile

    @staticmethod
    def profile_to_text(profile: Dict[str, Any]) -> str:
        """Formatea el perfil sanitizado a texto estructurado para el prompt."""
        lines = []
        if "id_usuario" in profile:
            lines.append(f"Identificador: {profile['id_usuario']}")
        if "arquetipo" in profile:
            lines.append(f"Arquetipo UX: {profile['arquetipo']}")
        if "necesidades_ux" in profile:
            necesidades = profile["necesidades_ux"]
            if isinstance(necesidades, list):
                necesidades = ", ".join(necesidades)
            lines.append(f"Necesidades UX: {necesidades}")
        if "estresores" in profile:
            estresores = profile["estresores"]
            if isinstance(estresores, list):
                estresores = ", ".join(estresores)
            lines.append(f"Estresores frecuentes: {estresores}")
        if "implicacion_habitar" in profile:
            lines.append(f"Implicancias para la app: {profile['implicacion_habitar']}")
        if "resumen_scores" in profile:
            lines.append(f"Tamizaje descriptivo: {profile['resumen_scores']}")

        return "\n".join(lines) if lines else "Sin datos adicionales de perfil."
