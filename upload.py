from fastapi import APIRouter, UploadFile, File
import os
import shutil
import utils

router = APIRouter()

@router.post("/upload")
async def upload_project(file: UploadFile = File(...)):
    UPLOAD_DIR = "uploads"
    # 支持的压缩包类型
    allowed_exts = [".zip", ".tar", ".gz", ".tar.gz", ".tgz", ".rar", ".7z"]
    filename = file.filename
    ext = os.path.splitext(filename)[1].lower()
    if ext not in allowed_exts:
        print(f"上传文件类型不支持: {filename}")
        return {"error": "暂只支持 .zip 格式压缩包"}
    try:
        file_location = os.path.join(UPLOAD_DIR, filename)
        with open(file_location, "wb") as f:
            shutil.copyfileobj(file.file, f)
        print(f"已保存上传文件: {file_location}")
        session_id, extract_dir = utils.extract_zip(file_location)
        print(f"已解压到: {extract_dir}, sessionId: {session_id}")
        return {"sessionId": session_id}
    except Exception as e:
        print(f"上传或解压失败: {e}")
        return {"error": f"上传或解压失败: {str(e)}"}
