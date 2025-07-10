from pydantic import BaseModel
from typing import List

class SourceRequest(BaseModel):
    sessionId: str
    method: str

class PathsRequest(BaseModel):
    sessionId: str
    method: str

class PackageRequest(BaseModel):
    sessionId: str
    method: str
    images: List[str]

class SourceResponse(BaseModel):
    source: str

class PathsResponse(BaseModel):
    images: List[str]

class PackageResponse(BaseModel):
    zip_url: str 