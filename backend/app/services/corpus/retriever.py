"""Motor de búsqueda y recuperación semántica en el corpus (RAG local)."""

import unicodedata
from typing import List, Dict, Any, Tuple
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

STOP_WORDS_ES = set(
    "a al algo ante como con cual cuales cuando de del desde donde el ella en entre es esa ese eso "
    "esta este esto la las le les lo los me mi mis mas muy no nos o para pero por porque que quien "
    "quienes se si sin sobre su sus te tengo tu tus un una uno unos y ya yo".split()
)


def normalize_text(text: str) -> str:
    """Elimina acentos y normaliza texto en minúsculas."""
    t = unicodedata.normalize("NFD", text.lower())
    return "".join(c for c in t if unicodedata.category(c) != "Mn")


class CorpusRetriever:
    """Recuperador semántico para fragmentos del corpus documental mediante TF-IDF."""

    def __init__(self, chunks: List[Dict[str, Any]]):
        self.chunks = chunks
        self.texts = [c["texto"] for c in chunks]

        # Inicialización del vectorizador TF-IDF adaptado a español
        self.vectorizer = TfidfVectorizer(
            preprocessor=normalize_text,
            stop_words=[normalize_text(w) for w in STOP_WORDS_ES],
            ngram_range=(1, 2),
            sublinear_tf=True,
        )
        if self.texts:
            self.tfidf_matrix = self.vectorizer.fit_transform(self.texts)
        else:
            self.tfidf_matrix = None

    def search(self, query: str, top_k: int = 3, with_score: bool = False) -> List[Any]:
        """Busca los fragmentos más relevantes para una consulta dada."""
        if not self.chunks or self.tfidf_matrix is None:
            return []

        q_vec = self.vectorizer.transform([query])
        sims = cosine_similarity(q_vec, self.tfidf_matrix).flatten()

        top_indices = sims.argsort()[::-1][:top_k]

        if with_score:
            return [(self.chunks[i], float(sims[i])) for i in top_indices]
        return [self.chunks[i] for i in top_indices]
