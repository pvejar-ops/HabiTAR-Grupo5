from typing import List, Optional
from pydantic import BaseModel, Field


class Citation(BaseModel):
    doc_id: str = Field(..., description="ID del fragmento del corpus clínico (ej: doc05_ficha_catq#2)")
    source_title: str
    relevance_summary: str


class OrientationReportResponse(BaseModel):
    user_id: Optional[str] = None
    summary_of_inputs: str = Field(..., description="1) Lo que compartiste (resumen sin juicio clínico)")
    orientation_guidelines: List[str] = Field(..., description="2) Orientación práctica psicoeducativa")
    citations: List[Citation] = Field(default_factory=list, description="Citas al corpus clínico")
    scope_and_disclaimer: str = Field(..., description="3) Alcance y aviso explícito de No-Diagnóstico")
