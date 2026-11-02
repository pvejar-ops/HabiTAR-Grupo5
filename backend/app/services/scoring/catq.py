"""Motor de cálculo determinista para el instrumento CAT-Q (Camouflaging Autistic Traits Questionnaire).

Referencia: Hull et al. (2019), Journal of Autism and Developmental Disorders.
Estructura:
- 25 ítems en escala Likert 1 a 7 (1 = Totalmente en desacuerdo, 7 = Totalmente de acuerdo).
- 3 Subescalas:
  * Compensación (Compensation): 9 ítems [1, 4, 5, 8, 11, 14, 17, 20, 23]
  * Enmascaramiento (Masking): 8 ítems [2, 6, 9, 12, 15, 18, 21, 24]
  * Asimilación (Assimilation): 8 ítems [3, 7, 10, 13, 16, 19, 22, 25]
- Ítems invertidos (5 ítems): [3, 12, 19, 22, 24]
  Fórmula de inversión: puntaje_real = 8 - respuesta_original
"""

from typing import Dict, Set
from app.schemas.screening import CATQResponse

# Constantes del instrumento
CATQ_TOTAL_ITEMS = 25
CATQ_MIN_VALUE = 1
CATQ_MAX_VALUE = 7

# Subescalas
COMPENSATION_ITEMS: Set[int] = {1, 4, 5, 8, 11, 14, 17, 20, 23}
MASKING_ITEMS: Set[int] = {2, 6, 9, 12, 15, 18, 21, 24}
ASSIMILATION_ITEMS: Set[int] = {3, 7, 10, 13, 16, 19, 22, 25}

# Ítems con puntuación invertida (2 de Enmascaramiento: 12, 24; 3 de Asimilación: 3, 19, 22)
REVERSE_ITEMS: Set[int] = {3, 12, 19, 22, 24}


def calculate_catq_score(raw_answers: Dict[int, int]) -> CATQResponse:
    """Calcula el puntaje total y por dimensiones del CAT-Q a partir de las respuestas crudas.

    Args:
        raw_answers: Diccionario con clave número de ítem (1..25) y valor respuesta (1..7).

    Raises:
        ValueError: Si faltan ítems, sobran ítems o los valores están fuera de rango [1, 7].
    """
    if len(raw_answers) != CATQ_TOTAL_ITEMS:
        missing = set(range(1, CATQ_TOTAL_ITEMS + 1)) - set(raw_answers.keys())
        extra = set(raw_answers.keys()) - set(range(1, CATQ_TOTAL_ITEMS + 1))
        error_msg = []
        if missing:
            error_msg.append(f"Faltan ítems: {sorted(list(missing))}")
        if extra:
            error_msg.append(f"Ítems no válidos: {sorted(list(extra))}")
        raise ValueError("; ".join(error_msg))

    scored_answers: Dict[int, int] = {}
    for item_num, val in raw_answers.items():
        if not isinstance(val, int) or val < CATQ_MIN_VALUE or val > CATQ_MAX_VALUE:
            raise ValueError(
                f"Valor inválido para el ítem {item_num}: {val}. "
                f"Debe ser un entero entre {CATQ_MIN_VALUE} y {CATQ_MAX_VALUE}."
            )

        # Aplicar inversión determinista si corresponde
        if item_num in REVERSE_ITEMS:
            scored_answers[item_num] = 8 - val
        else:
            scored_answers[item_num] = val

    # Cálculo por dimensiones
    compensation = sum(scored_answers[i] for i in COMPENSATION_ITEMS)
    masking = sum(scored_answers[i] for i in MASKING_ITEMS)
    assimilation = sum(scored_answers[i] for i in ASSIMILATION_ITEMS)
    total = sum(scored_answers.values())

    note = (
        f"Puntaje total CAT-Q: {total}/175. "
        f"Subescalas: Compensación={compensation}/63, Enmascaramiento={masking}/56, "
        f"Asimilación={assimilation}/56. "
        "En la literatura, un puntaje mayor refleja un mayor uso autorreportado de estrategias de camuflaje social. "
        "Este valor es de carácter orientativo y no constituye ni reemplaza un diagnóstico clínico."
    )

    return CATQResponse(
        total_score=total,
        compensation_score=compensation,
        masking_score=masking,
        assimilation_score=assimilation,
        interpretation_note=note,
    )
