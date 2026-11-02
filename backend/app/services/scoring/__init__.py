"""Módulo de cálculo determinista para instrumentos psicométricos (CAT-Q, RBQ-2A)."""

from .catq import calculate_catq_score
from .rbq2a import calculate_rbq2a_score

__all__ = ["calculate_catq_score", "calculate_rbq2a_score"]
