# Informe de Estado y Avances - Proyecto HabiTAR (Grupo 5)
**Fecha de corte:** 22 de octubre de 2026
**Línea de Trabajo:** TEA Screening Agent (RAG)

---

## 1. Resumen Ejecutivo y Alcance Actual
El proyecto HabiTAR sufrió una reestructuración estratégica formalizada con la contraparte (UXLab), pasando de ser un módulo de comunidad/red social a un **Sistema Agéntico basado en Retrieval-Augmented Generation (RAG)**. 

**Objetivo principal:** Administrar, integrar y analizar instrumentos de screening complementarios asociados al Trastorno del Espectro Autista (TEA) en adultos. 
*   **Restricción principal:** El sistema opera bajo una estricta política de **"Cero Diagnóstico"**. Su fin es 100% orientativo, entregando un perfil descriptivo e instando al usuario a buscar apoyo clínico profesional.

---

## 2. Estado de Gestión y Cronograma (EVM)
El proyecto se encuentra alineado con la replanificación establecida tras el cambio de alcance.

*   **Hitos superados:** 
    *   Hito 0 y Hito 1 (Propuestas iniciales).
    *   Hito 2 y Hito 3 (Entregados el 15 de octubre de 2026).
*   **Avance General:** El proyecto reporta un avance real consolidado de **~38,7%**, encontrándose exactamente en el centro del cuadrante de desempeño ideal (SPI = 1.00, CPI = 1.00).
*   **Riesgos mitigados críticamente:** La falta de acceso al código heredado, el sobrecosto de APIs (resuelto usando modelos locales) y los riesgos de alucinaciones diagnósticas de la IA.

---

## 3. Logros Técnicos: ¿Qué se ha hecho hasta ahora?

### A. Fase de Prototipado y Arquitectura (Compuerta superada)
Se construyó y validó con éxito un pipeline *End-to-End* en Google Colab, demostrando la viabilidad técnica del RAG.
*   **Motor RAG Local:** Se implementó `Sentence-Transformers` (modelo *paraphrase-multilingual-MiniLM-L12-v2*) para vectorizar la teoría clínica de manera 100% gratuita y local, evitando gastos de tokens de API en las búsquedas.
*   **Métricas de Calidad:** Las pruebas de recuperación de información superaron la línea base requerida (Recall@k ≥ 0,8).
*   **Mock Data:** Ante la espera de la validación del Comité de Ética, el sistema se alimentó con 31 arquetipos de diseño UX elaborados por el Observatorio UX UTEM para simular los perfiles de usuario.

### B. Curaduría de Datos y Definición de Instrumentos
*   **Corpus Documental (v0.2):** Se segmentó y normalizó la literatura teórica, generando los *chunks* que alimentan a la IA.
*   **Definición de Tests:** 
    *   **CAT-Q (Camuflaje Social):** Aprobado (investigación de licencias en curso). Lógica de 25 ítems definida, incluyendo la fórmula de inversión para ítems específicos (8 - respuesta original).
    *   **RBQ-2A (Conductas Repetitivas):** Aprobado (investigación de licencias en curso). Algoritmo para calcular promedios (escala 1 a 3) definido.
    *   **SRS-2 Adult:** **Descartado** de la automatización por tratarse de un instrumento privado (WPS Nivel C) que requiere licencia comercial, mitigando un riesgo legal (R3).

### C. Seguridad, Ética y Guardarraíles
*   **Red Teaming:** Se ejecutó una batería de 57 casos de prueba adversariales. El agente (Gemini 1.5 Flash) demostró un 100% de cumplimiento en la política de no-diagnóstico, bloqueando intentos de inyección de prompts y negándose a emitir juicios clínicos.
*   **Cumplimiento Normativo (Ley 21.719):** Se generó la documentación comprobando que la arquitectura del sistema cumple con los requerimientos de la nueva Ley de Protección de Datos Personales (privacidad por diseño, no decisiones automatizadas vinculantes, anonimización).

---

## 4. ¿En qué punto exacto nos encontramos? (22 de Octubre)
El equipo ha cerrado la "Fase 1: Notebook local" y ha configurado exitosamente el **repositorio de código**. Actualmente, nos encontramos en la **Fase de Diseño Agéntico Avanzado e Integración de Motores**. 

El trabajo ya no está en cuadernos de Colab, sino en la programación de la infraestructura real que conectará la app con la Inteligencia Artificial.

**Tareas Activas (Ruta Crítica inmediata):**
1.  **Actividad 38 - Backend intermediario (FastAPI):** Traduciendo el motor Python de Colab a una API real que orqueste la entrada de la app, consulte a Supabase y llame a Gemini.
2.  **Actividad 39 - Scoring determinista:** Programando en backend las funciones matemáticas duras (inversión de valores y promedios) del CAT-Q y RBQ-2A.
3.  **Actividad 41 - Recetario v1:** Consolidando en código los System Prompts definitivos de los agentes (Selector, Procesador, Informe).

---

## 5. Próximos Pasos (Hacia el Hito 4 - 04 de Noviembre)
Para asegurar el **59,7% de avance** comprometido para el próximo hito, las prioridades son:
*   Finalizar la conexión backend-Supabase para asegurar el almacenamiento de datos bajo políticas RLS (Row Level Security).
*   Definir los contratos de interfaz (endpoints) junto con el Grupo 4, para garantizar que el módulo de adultos encaje perfectamente en la aplicación principal de HabiTAR.
*   Preparar el Informe Técnico detallando la arquitectura y el modelo de datos.