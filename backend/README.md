# HabiTAR — Backend API (FastAPI)

Orquestador backend del módulo de screening para Trastorno del Espectro Autista (TEA) en adultos dentro del proyecto HabiTAR (Grupo 5 - GPI / UXLab).

## 1. Arquitectura del Módulo

```
backend/
├── app/
│   ├── api/
│   │   └── v1/
│   │       ├── endpoints/
│   │       │   ├── health.py        # Diagnóstico y estado operativo
│   │       │   ├── screening.py     # Procesamiento de instrumentos (CAT-Q, RBQ-2A)
│   │       │   └── orientation.py   # Generación de orientación con RAG agéntico
│   │       └── router.py            # Centralizador de rutas API v1
│   ├── core/
│   │   ├── config.py                # Variables de entorno y ajustes
│   │   └── security.py              # Políticas de No-Diagnóstico y Ley 21.719
│   ├── schemas/
│   │   ├── health.py                # Modelos Pydantic de estado
│   │   ├── screening.py             # Modelos de entrada/salida para tests
│   │   └── report.py                # Modelos de reporte orientativo
│   ├── services/
│   │   ├── corpus/                  # Segmentación y búsqueda semántica en corpus_habitar
│   │   │   ├── chunker.py           # Segmentación en 50 fragmentos estandarizados
│   │   │   └── retriever.py         # Recuperador semántico TF-IDF
│   │   ├── guardrails/              # Filtros de seguridad (Cero Diagnóstico)
│   │   │   ├── input_filter.py      # Filtro de entrada (8 categorías adversariales)
│   │   │   └── output_filter.py     # Filtro de salida (auditoría post-generación)
│   │   ├── agents/                  # Tríada Agéntica (Recetario v1)
│   │   │   ├── prompts.py           # Directrices y reglas de sistema
│   │   │   ├── selector.py          # Agente Selector (sanitización de perfil)
│   │   │   ├── procesador.py        # Agente Procesador (contexto RAG trazable)
│   │   │   └── informe.py           # Agente de Informe (generación controlada)
│   │   ├── scoring/                 # Motores matemáticos deterministas
│   │   │   ├── catq.py              # 25 ítems, 5 invertidos, 3 subescalas
│   │   │   └── rbq2a.py             # 20 ítems, medias aritméticas
│   │   └── rag_pipeline.py          # Orquestador del ciclo completo de 4 capas
│   └── main.py                      # Instancia FastAPI, CORS y Swagger
├── tests/
│   ├── test_health.py               # Tests de endpoints base
│   ├── test_scoring.py              # Tests de algoritmos psicométricos
│   └── test_rag_pipeline.py         # Tests de RAG, guardarraíles y orientación
├── .env.example                     # Plantilla de variables de entorno
└── requirements.txt                 # Dependencias
```

## 2. Endpoints Principales (API v1)

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/v1/health` | Estado del backend y versión operativa. |
| `POST` | `/api/v1/screening/process` | Cálculo determinista de respuestas crudas CAT-Q y RBQ-2A. |
| `POST` | `/api/v1/orientation/generate` | Orientación psicoeducativa con RAG trazable y guardarraíles. |

## 3. Ejecución Local

```powershell
cd backend
py -m uvicorn app.main:app --reload --port 8000
```

* **Swagger UI:** `http://localhost:8000/docs`
* **Especificación OpenAPI (Redoc):** `http://localhost:8000/redoc`

## 4. Ejecución de Pruebas Unitarias

```powershell
cd backend
py -m unittest discover tests
```
