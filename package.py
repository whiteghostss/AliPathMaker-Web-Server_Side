from fastapi import APIRouter, Form, HTTPException
from fastapi.responses import JSONResponse
import os
from typing import List, Optional
import utils

router = APIRouter()

@router.post("/package-and-download", summary="打包源码、图片和路径文本为zip", response_description="返回zip下载链接")
async def package_and_download(
    sessionId: str = Form(...),
    method: str = Form(...),
    images: Optional[List[str]] = Form(None),
    paths: Optional[List[str]] = Form(None)
):
    if not sessionId or not method or not images or len(images) == 0:
        raise HTTPException(status_code=400, detail="sessionId, method, 至少选择一张图片(images)！")
    project_dir = os.path.join("uploads", sessionId)
    try:
        source = utils.find_java_method_source(project_dir, method)
        if not source:
            return JSONResponse(status_code=404, content={"error": "Method not found"})
        abs_images = [os.path.join('.', img) for img in images]
        zip_path = os.path.join("results", f"{sessionId}.zip")
        # 先打包源码和图片
        utils.package_results(source, abs_images, zip_path)
        # 如果有paths，追加写入paths.txt到zip
        if paths and len(paths) > 0:
            import zipfile
            with zipfile.ZipFile(zip_path, 'a') as zipf:
                zipf.writestr('paths.txt', '\n'.join(paths))
        # 返回下载链接
        zip_url = f"/results/{sessionId}.zip"
        return {"zip_url": zip_url}
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)}) 