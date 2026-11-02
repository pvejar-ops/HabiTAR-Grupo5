from .prompts import build_system_instruction, REGLA_1, PLANTILLA_SISTEMA
from .selector import SelectorAgent
from .procesador import ProcessorAgent
from .informe import ReportAgent

__all__ = [
    "build_system_instruction",
    "REGLA_1",
    "PLANTILLA_SISTEMA",
    "SelectorAgent",
    "ProcessorAgent",
    "ReportAgent",
]
