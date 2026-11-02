from fastapi import APIRouter
from app.api.v1.endpoints import health, screening, orientation

api_router = APIRouter()

api_router.include_router(health.router, tags=["Health"])
api_router.include_router(screening.router, prefix="/screening", tags=["Screening"])
api_router.include_router(orientation.router, prefix="/orientation", tags=["Orientation / RAG"])
