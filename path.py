from fastapi import APIRouter, Form
from fastapi.responses import JSONResponse
import os
import utils

router = APIRouter()

@router.post("/generate-paths")
async def generate_paths(sessionId: str = Form(...), method: str = Form(...)):
    project_dir = os.path.join("uploads", sessionId)
    output_dir = os.path.join("results", sessionId)
    try:
        images = utils.call_comex(project_dir, method, output_dir)
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})
    rel_images = [os.path.relpath(img, '.') for img in images]
    return {"images": rel_images} 