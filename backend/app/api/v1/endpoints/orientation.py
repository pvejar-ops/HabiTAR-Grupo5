from typing import Optional, Dict, Any, List
from pydantic import BaseModel, Field
from fastapi import APIRouter
from app.services.rag_pipeline import RAGPipelineService
from app.core.security import DISCLAIMER_CLINICO_NO_DIAGNOSTICO

router = APIRouter()


class OrientationRequest(BaseModel):
    query: str = Field(..., description="Pregunta o consulta del usuario", example="¿Cómo puedo manejar la sobrecarga de estímulos sensoriales?")
    user_profile: Optional[Dict[str, Any]] = Field(None, description="Perfil UX del usuario (arquetipo, estresores)")
    screening_summary: Optional[str] = Field(None, description="Resumen descriptivo de los puntajes obtenidos")
    top_k: int = Field(default=3, ge=1, le=10, description="Cantidad de fragmentos a recuperar")


class SourceChunk(BaseModel):
    chunk_id: str
    doc_titulo: str
    seccion: str
    score: float


class OrientationResponse(BaseModel):
    query: str
    response: str
    sources: List[SourceChunk]
    layer: str
    blocked: bool
    disclaimer: str


@router.post(
    "/generate",
    response_model=OrientationResponse,
    summary="Generar orientación psicoeducativa trazable con RAG y guardarraíles",
)
async def generate_orientation(payload: OrientationRequest):
    """
    Orquesta el flujo completo de la tríada agéntica:
    - Filtro de entrada (bloquea intentos de diagnóstico o crisis de inmediato).
    - Agente Selector (elimina etiquetas clínicas sensibles del perfil).
    - Agente Procesador (busca fragmentos en el corpus clínico).
    - Agente de Informe (genera la respuesta con citas [docXX#N] y aplica filtro de salida).
    """
    pipeline = RAGPipelineService.get_instance()

    profile_data = dict(payload.user_profile) if payload.user_profile else {}
    if payload.screening_summary:
        profile_data["resumen_scores"] = payload.screening_summary

    result = pipeline.run(
        query=payload.query,
        user_profile=profile_data,
        top_k=payload.top_k,
    )

    return OrientationResponse(
        query=result["query"],
        response=result["response"],
        sources=result["sources"],
        layer=result["layer"],
        blocked=result["blocked"],
        disclaimer=DISCLAIMER_CLINICO_NO_DIAGNOSTICO,
    )
