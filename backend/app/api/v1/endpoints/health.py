from fastapi import APIRouter
from app.schemas.health import HealthResponse
from app.core.config import settings

router = APIRouter()


@router.get("/health", response_model=HealthResponse, summary="Verificar estado de la API")
async def check_health():
    """Retorna el estado operativo actual del backend de HabiTAR."""
    return HealthResponse(
        status="ok",
        version="0.1.0",
        environment=settings.ENVIRONMENT,
    )
