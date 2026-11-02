"""Pipeline E2E de Tamizaje y Orientación Agéntica con RAG y Guardarraíles."""

import os
from typing import Dict, Any, Optional
from app.services.corpus.chunker import chunk_corpus
from app.services.corpus.retriever import CorpusRetriever
from app.services.guardrails.input_filter import evaluate_input, get_fixed_response
from app.services.agents.selector import SelectorAgent
from app.services.agents.procesador import ProcessorAgent
from app.services.agents.informe import ReportAgent

# Directorio por defecto del corpus (soporta raíz del repo o subcarpeta backend)
_dir_1 = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", "..", "corpus_habitar"))
_dir_2 = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", "..", "corpus_habitar"))
DEFAULT_CORPUS_DIR = _dir_1 if os.path.exists(_dir_1) else _dir_2


class RAGPipelineService:
    """Orquestador integral del sistema RAG agéntico de HabiTAR."""

    _instance: Optional["RAGPipelineService"] = None

    def __init__(self, corpus_dir: str = DEFAULT_CORPUS_DIR):
        self.corpus_dir = corpus_dir
        if os.path.exists(corpus_dir):
            self.chunks = chunk_corpus(corpus_dir)
        else:
            self.chunks = []

        self.retriever = CorpusRetriever(self.chunks)
        self.selector = SelectorAgent()
        self.processor = ProcessorAgent(self.retriever)
        self.reporter = ReportAgent()

    @classmethod
    def get_instance(cls, corpus_dir: str = DEFAULT_CORPUS_DIR) -> "RAGPipelineService":
        if cls._instance is None:
            cls._instance = cls(corpus_dir)
        return cls._instance

    def run(
        self,
        query: str,
        user_profile: Optional[Dict[str, Any]] = None,
        top_k: int = 3,
    ) -> Dict[str, Any]:
        """
        Ejecuta el ciclo de 4 capas:
        1. Filtro de entrada (Guardarraíl adversarial y de derivación clínica inmediata).
        2. Agente Selector (Sanitización del perfil y exclusión de datos clínicos).
        3. Agente Procesador (Búsqueda RAG en el corpus con métrica de similitud).
        4. Agente de Informe (Generación controlada y auditoría de salida).
        """
        user_profile = user_profile or {}

        # 1. Filtro de Entrada
        hits = evaluate_input(query)
        if hits:
            category, fixed_msg = get_fixed_response(hits)
            return {
                "query": query,
                "response": fixed_msg,
                "sources": [],
                "layer": "input_guardrail",
                "category": category,
                "blocked": True,
            }

        # 2. Agente Selector
        clean_profile = self.selector.sanitize_profile(user_profile)
        profile_text = self.selector.profile_to_text(clean_profile)

        # 3. Agente Procesador (RAG)
        context_text, retrieved_chunks, scores = self.processor.retrieve_context(
            query, top_k=top_k
        )
        sources = [
            {
                "chunk_id": c["chunk_id"],
                "doc_titulo": c["doc_titulo"],
                "seccion": c["seccion"],
                "score": round(score, 3),
            }
            for c, score in zip(retrieved_chunks, scores)
        ]

        # 4. Agente de Informe
        report_text, meta, blocked_output = self.reporter.generate_report(
            query=query,
            profile_text=profile_text,
            context_text=context_text,
        )

        return {
            "query": query,
            "response": report_text,
            "sources": sources,
            "layer": "output_guardrail" if blocked_output else "agent_pipeline",
            "blocked": blocked_output,
            "metadata": meta,
        }
