from fastapi import APIRouter, Form
from fastapi.responses import FileResponse, JSONResponse
import os
from typing import List
import utils

router = APIRouter()

@router.post("/package-and-download")
async def package_and_download(sessionId: str = Form(...), method: str = Form(...), images: List[str] = Form(...)):
    project_dir = os.path.join("uploads", sessionId)
    source = utils.find_java_method_source(project_dir, method)
    if not source:
        return JSONResponse(status_code=404, content={"error": "Method not found"})
    abs_images = [os.path.join('.', img) for img in images]
    zip_path = os.path.join("results", f"{sessionId}.zip")
    utils.package_results(source, abs_images, zip_path)
    return FileResponse(zip_path, filename=f"{sessionId}.zip") 