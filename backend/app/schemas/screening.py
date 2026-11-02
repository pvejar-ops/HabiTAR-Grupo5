from typing import Dict, Optional
from pydantic import BaseModel, Field


class CATQInput(BaseModel):
    """Respuestas a los 25 ítems del cuestionario CAT-Q (escala Likert 1 a 7)."""
    answers: Dict[int, int] = Field(
        ...,
        description="Mapeo de número de ítem (1 a 25) a respuesta (1 a 7)",
        example={1: 5, 2: 3, 3: 7}
    )


class RBQ2AInput(BaseModel):
    """Respuestas a los 20 ítems del cuestionario RBQ-2A (escala 1 a 3 o 1 a 4)."""
    answers: Dict[int, int] = Field(
        ...,
        description="Mapeo de número de ítem (1 a 20) a respuesta",
        example={1: 2, 2: 1, 3: 3}
    )


class CATQResponse(BaseModel):
    total_score: int
    compensation_score: Optional[int] = None
    masking_score: Optional[int] = None
    assimilation_score: Optional[int] = None
    interpretation_note: str


class RBQ2AResponse(BaseModel):
    total_mean: float
    repetitive_motor_mean: Optional[float] = None
    insistence_on_sameness_mean: Optional[float] = None
    interpretation_note: str


class ScreeningRequest(BaseModel):
    user_id: Optional[str] = Field(None, description="Identificador anónimo del usuario (hash/UUID)")
    catq: Optional[CATQInput] = None
    rbq2a: Optional[RBQ2AInput] = None


class ScreeningResponse(BaseModel):
    user_id: Optional[str] = None
    catq_result: Optional[CATQResponse] = None
    rbq2a_result: Optional[RBQ2AResponse] = None
    disclaimer: str
