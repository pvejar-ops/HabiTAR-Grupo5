from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.core.config import settings
from app.api.v1.router import api_router

app = FastAPI(
    title=settings.PROJECT_NAME,
    description=(
        "Backend orquestador para el módulo de screening de Trastorno del Espectro Autista (TEA) "
        "en adultos (HabiTAR). Implementa arquitectura RAG, scoring determinista y política de Cero Diagnóstico."
    ),
    version="0.1.0",
    docs_url="/docs",
    redoc_url="/redoc",
)

# Configuración de CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Inclusión de Rutas de la API v1
app.include_router(api_router, prefix=settings.API_V1_PREFIX)


@app.get("/", tags=["Root"])
async def root():
    return {
        "project": settings.PROJECT_NAME,
        "version": "0.1.0",
        "docs": "/docs",
        "status": "online",
    }
