"""Motor de cálculo determinista para el instrumento RBQ-2A (Adult Repetitive Behaviours Questionnaire-2).

Referencia: Barrett et al. (2015), Journal of Autism and Developmental Disorders.
Estructura:
- 20 ítems evaluados en escala unificada de 3 puntos (1 a 3).
  1 = Rara vez o nunca
  2 = Ocasionalmente / moderado
  3 = Frecuente / marcado
- Dimensiones y subescalas principales:
  * Conductas motoras repetitivas (RMB - Repetitive Motor Behaviours): 6 ítems [1, 2, 4, 5, 6, 8]
  * Insistencia en la igualdad (IS - Insistence on Sameness): 8 ítems [11, 12, 13, 14, 15, 16, 17, 18]
  * Sensorimotoras agrupadas (RSMB): 10 ítems [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
- Puntaje total: Media aritmética de los 20 ítems (rango 1.00 a 3.00).
- Puntajes de subescala: Medias aritméticas de los ítems correspondientes.
"""

from typing import Dict, Set
from app.schemas.screening import RBQ2AResponse

RBQ2A_TOTAL_ITEMS = 20
RBQ2A_MIN_VALUE = 1
RBQ2A_MAX_VALUE = 3

# Subescalas
RMB_ITEMS: Set[int] = {1, 2, 4, 5, 6, 8}
IS_ITEMS: Set[int] = {11, 12, 13, 14, 15, 16, 17, 18}
RSMB_ITEMS: Set[int] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}


def calculate_rbq2a_score(raw_answers: Dict[int, int]) -> RBQ2AResponse:
    """Calcula las medias de puntaje total y por dimensiones del RBQ-2A.

    Args:
        raw_answers: Diccionario con clave número de ítem (1..20) y valor respuesta (1..3).
                     Si algún ítem viene en escala de 4 puntos (1..4), el valor 4 se normaliza a 3
                     siguiendo el criterio de Barrett et al. (2015).

    Raises:
        ValueError: Si faltan ítems, sobran ítems o los valores están fuera de rango permitido.
    """
    if len(raw_answers) != RBQ2A_TOTAL_ITEMS:
        missing = set(range(1, RBQ2A_TOTAL_ITEMS + 1)) - set(raw_answers.keys())
        extra = set(raw_answers.keys()) - set(range(1, RBQ2A_TOTAL_ITEMS + 1))
        error_msg = []
        if missing:
            error_msg.append(f"Faltan ítems: {sorted(list(missing))}")
        if extra:
            error_msg.append(f"Ítems no válidos: {sorted(list(extra))}")
        raise ValueError("; ".join(error_msg))

    normalized_answers: Dict[int, int] = {}
    for item_num, val in raw_answers.items():
        if not isinstance(val, int) or val < RBQ2A_MIN_VALUE or val > 4:
            raise ValueError(
                f"Valor inválido para el ítem {item_num}: {val}. Debe ser un entero entre {RBQ2A_MIN_VALUE} y 3."
            )
        # Normalizar valores de 4 a 3 según metodología de unificación Barrett et al.
        normalized_answers[item_num] = 3 if val == 4 else val

    # Cálculo de medias aritméticas
    total_mean = round(sum(normalized_answers.values()) / float(RBQ2A_TOTAL_ITEMS), 2)
    rmb_mean = round(sum(normalized_answers[i] for i in RMB_ITEMS) / float(len(RMB_ITEMS)), 2)
    is_mean = round(sum(normalized_answers[i] for i in IS_ITEMS) / float(len(IS_ITEMS)), 2)

    note = (
        f"Media total RBQ-2A: {total_mean:.2f} (escala 1.00 a 3.00). "
        f"Subescalas: Conductas Motoras (RMB)={rmb_mean:.2f}, Insistencia en Igualdad (IS)={is_mean:.2f}. "
        "Describe la frecuencia y presencia de rutinas o patrones repetitivos de forma descriptiva. "
        "No establece diagnósticos clínicos ni umbrales normativos determinantes."
    )

    return RBQ2AResponse(
        total_mean=total_mean,
        repetitive_motor_mean=rmb_mean,
        insistence_on_sameness_mean=is_mean,
        interpretation_note=note,
    )
