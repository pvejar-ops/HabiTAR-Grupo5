# Contexto General y Técnico: Proyecto HabiTAR (Grupo 5 - GPI)

**Fecha de actualización:** 22 de octubre de 2026  
**Línea de Trabajo:** TEA Screening Agent (RAG) en población adulta  
**Asignatura:** Gestión de Proyectos Informáticos (GPI) - Universidad Tecnológica Metropolitana (UTEM)  
**Profesor Guía:** Eduardo Arturo Osorio Venegas (`eduardo.osoriov@utem.cl`)  
**Contraparte Externa:** UXLab  
**Integrantes del Grupo 5:**
- Patricio Vejar
- Maximiliano Campos
- Vicente Fuentes
- Maximiliano Rubio

---

## 1. Propósito y Principio Ético del Proyecto

HabiTAR es un **sistema agéntico con arquitectura RAG (*Retrieval-Augmented Generation*)** diseñado para el tamizaje (*screening*) complementario del Trastorno del Espectro Autista (TEA) en adultos.

> [!IMPORTANT]
> **Política Estricta de "Cero Diagnóstico" (Guardarraíl Central)**
> El sistema **NO diagnostica ni emite dictámenes clínicos**. Su objetivo es 100% orientativo, entregando perfiles descriptivos de compatibilidad (*bajo, moderado, alto*) e instando formalmente al usuario a recurrir a un profesional de salud acreditado.

---

## 2. Enlaces y Recursos Clave

* **Carpeta en Google Drive (Oficial del Proyecto):**
  * URL: `https://drive.google.com/drive/folders/1xUc5tNUoCaQBGq8vtvs9s-exKhGW8kR7`
  * ID de carpeta: `1xUc5tNUoCaQBGq8vtvs9s-exKhGW8kR7`
  * Acceso configurado mediante el plugin/CLI `gdrive`.
* **Corpus y Datos Mock:**
  * Ubicación local: `C:\Users\Patricio\Desktop\HabiTAR\`
  * 31 perfiles arquetípicos UX (`perfiles_31.json`) validados ante la espera de resolución del Comité de Ética.
  * Cuaderno de prototipado validado: `Colab_Grupo5_Pipeline_E2E_trazable.ipynb`.

---

## 3. Instrumentos de Screening

| Instrumento | Estado | Justificación / Lógica |
| :--- | :--- | :--- |
| **CAT-Q** (*Camouflaging Autistic Traits Questionnaire*) | **Aprobado** | 25 ítems con fórmula determinista de inversión (8 - respuesta original) para ítems compensatorios y de enmascaramiento social. |
| **RBQ-2A** (*Repetitive Behaviors Questionnaire-2 for Adults*) | **Aprobado** | Evaluación de conductas e intereses repetitivos; cálculo de promedios en escala 1 a 3. |
| **SRS-2 Adult** (*Social Responsiveness Scale*) | **Descartado** | Descartado para eliminar riesgos legales de propiedad intelectual (licencia comercial privada WPS Nivel C). |

---

## 4. Arquitectura y Stack Tecnológico

1. **Frontend / App Móvil:**
   - Aplicación base desarrollada en **Android Studio**.
   - Se integra como un módulo anexable para adultos (coordinado con el Grupo 4).
2. **Backend Intermediario:**
   - Orquestador en **FastAPI (Python)** que traduce la lógica exploratoria de Google Colab a endpoints REST estables.
   - Ejecución de cálculos de scoring determinista para los cuestionarios antes de la llamada a la IA.
3. **Persistencia y Autenticación:**
   - **Supabase** (PostgreSQL, Supabase Auth, Storage y políticas de seguridad a nivel de filas **RLS**).
4. **Inteligencia Artificial y RAG:**
   - **Embeddings locales:** `Sentence-Transformers` (`paraphrase-multilingual-MiniLM-L12-v2`), costo cero de tokens de búsqueda.
   - **LLM Generativo:** **Gemini 1.5 Flash** (Google AI Studio / Gemini API).
   - **Tríada Agéntica:**
     - *Agente Selector:* Evalúa contexto y perfil de usuario.
     - *Agente Procesador:* Recupera contexto del corpus clínico y pondera resultados.
     - *Agente de Informe:* Redacta el reporte explicativo y empático con guardarraíles de no-diagnóstico.
5. **Seguridad y Cumplimiento Legal:**
   - *Red Teaming:* 57 casos adversariales superados con 100% de apego a las políticas de seguridad.
   - Cumplimiento de la **Ley N° 21.719** sobre Protección de Datos Personales (privacidad por diseño, anonimización).

---

## 5. Estado de Gestión y Cronograma (EVM)

* **Hitos Aprobados:** Hitos 0, 1, 2 y 3.
* **Avance Acumulado Actual:** **~38,7%** (SPI = 1.00, CPI = 1.00 - Desempeño ideal sin sobrecostos ni retrasos).
* **Próximo Hito:** **Hito 4 (04 de noviembre de 2026)**. Meta comprometida: **59,7% de avance**.
* **Foco Inmediato (Ruta Crítica):**
  1. Construcción del backend FastAPI (`Actividad 38`).
  2. Implementación de funciones matemáticas de scoring determinista (`Actividad 39`).
  3. Consolidación de System Prompts definitivos en código / Recetario v1 (`Actividad 41`).
  4. Cierre de contratos de interfaz (endpoints) con el Grupo 4.
