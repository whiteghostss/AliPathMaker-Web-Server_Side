from fastapi import APIRouter, UploadFile, File
import os
import shutil
import utils

router = APIRouter()

@router.post("/upload")
async def upload_project(file: UploadFile = File(...)):
    UPLOAD_DIR = "uploads"
    file_location = os.path.join(UPLOAD_DIR, file.filename)
    with open(file_location, "wb") as f:
        shutil.copyfileobj(file.file, f)
    session_id, extract_dir = utils.extract_zip(file_location)
    return {"sessionId": session_id} 