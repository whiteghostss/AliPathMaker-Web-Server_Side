from fastapi import APIRouter, Form
from fastapi.responses import JSONResponse
import os
import utils

router = APIRouter()

@router.post("/get-source")
async def get_source(sessionId: str = Form(...), method: str = Form(...)):
    project_dir = os.path.join("uploads", sessionId)
    source = utils.find_java_method_source(project_dir, method)
    if not source:
        return JSONResponse(status_code=404, content={"error": "Method not found"})
    return {"source": source} 