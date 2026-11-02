"""Agente Procesador: Recuperación y ponderación de contexto clínico desde el corpus documental."""

from typing import List, Dict, Any, Tuple
from app.services.corpus.retriever import CorpusRetriever


class ProcessorAgent:
    """
    Agente responsable de consultar el motor RAG local, recuperar los fragmentos documentales
    más pertinentes a la consulta del usuario y formatear el contexto con identificadores de trazabilidad.
    """

    def __init__(self, retriever: CorpusRetriever):
        self.retriever = retriever

    def retrieve_context(
        self, query: str, top_k: int = 3
    ) -> Tuple[str, List[Dict[str, Any]], List[float]]:
        """Recupera los K fragmentos más relevantes y los formatea como contexto documentado."""
        results = self.retriever.search(query, top_k=top_k, with_score=True)
        chunks = [c for c, _ in results]
        scores = [float(s) for _, s in results]

        context_text = "\n\n".join(
            f"[{c['chunk_id']}] ({c['doc_titulo']}) {c['texto']}" for c in chunks
        )

        return context_text, chunks, scores
