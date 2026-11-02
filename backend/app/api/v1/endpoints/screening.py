from fastapi import APIRouter, HTTPException, status
from app.schemas.screening import ScreeningRequest, ScreeningResponse
from app.services.scoring import calculate_catq_score, calculate_rbq2a_score
from app.core.security import DISCLAIMER_CLINICO_NO_DIAGNOSTICO

router = APIRouter()


@router.post(
    "/process",
    response_model=ScreeningResponse,
    summary="Procesar instrumentos de screening (CAT-Q, RBQ-2A)",
)
async def process_screening(payload: ScreeningRequest):
    """
    Recibe respuestas crudas de los instrumentos CAT-Q y/o RBQ-2A,
    ejecuta el cálculo determinista (inversión y medias) y retorna
    los puntajes desglosados con el guardarraíl de Cero Diagnóstico.
    """
    catq_result = None
    rbq2a_result = None

    try:
        if payload.catq is not None:
            catq_result = calculate_catq_score(payload.catq.answers)

        if payload.rbq2a is not None:
            rbq2a_result = calculate_rbq2a_score(payload.rbq2a.answers)

    except ValueError as e:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(e),
        )

    return ScreeningResponse(
        user_id=payload.user_id,
        catq_result=catq_result,
        rbq2a_result=rbq2a_result,
        disclaimer=DISCLAIMER_CLINICO_NO_DIAGNOSTICO,
    )
