from pydantic import BaseModel, Field
from datetime import datetime, timezone


class HealthResponse(BaseModel):
    status: str = Field(default="ok", example="ok")
    version: str = Field(default="0.1.0", example="0.1.0")
    environment: str = Field(default="development", example="development")
    timestamp: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))
