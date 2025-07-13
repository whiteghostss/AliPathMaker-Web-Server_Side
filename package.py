from fastapi import APIRouter, HTTPException
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import List, Optional
import os
import utils

class PathFileItem(BaseModel):
    png: Optional[str] = None
    dot: Optional[str] = None
    json: Optional[str] = None

class PackageRequest(BaseModel):
    sessionId: str
    output_json: Optional[str] = None
    output_dot: Optional[str] = None
    selected_paths: List[PathFileItem] = []

router = APIRouter()

@router.post("/package-and-download", summary="多选打包源码、图片和路径文本为zip", response_description="返回zip下载链接")
async def package_and_download(data: PackageRequest):
    sessionId = data.sessionId
    output_json = data.output_json
    output_dot = data.output_dot
    selected_paths = data.selected_paths
    base_dir = os.path.abspath(os.path.join("results", sessionId))
    file_list = []
    # 必选：自动查找PathAnalysis.java和output.png
    java_path = os.path.join(base_dir, "PathAnalysis.java")
    output_png_path = os.path.join(base_dir, "output.png")
    for f in [java_path, output_png_path]:
        if not os.path.exists(f):
            return JSONResponse(status_code=404, content={"error": f"未找到文件: {f}"})
        file_list.append(f)
    # 可选
    if output_json:
        json_path = os.path.join(base_dir, output_json)
        if not os.path.exists(json_path):
            return JSONResponse(status_code=404, content={"error": f"未找到文件: {json_path}"})
        file_list.append(json_path)
    if output_dot:
        dot_path = os.path.join(base_dir, output_dot)
        if not os.path.exists(dot_path):
            return JSONResponse(status_code=404, content={"error": f"未找到文件: {dot_path}"})
        file_list.append(dot_path)
    # 路径多选
    for path_item in selected_paths:
        for file_name in [path_item.png, path_item.dot, path_item.json]:
            if file_name:
                file_path = os.path.join(base_dir, file_name)
                if not os.path.exists(file_path):
                    return JSONResponse(status_code=404, content={"error": f"未找到路径相关文件: {file_path}"})
                file_list.append(file_path)
    zip_path = os.path.join("results", f"{sessionId}.zip")
    try:
        utils.package_selected_files(zip_path, file_list, extra_texts=None, base_dir=base_dir)
        zip_url = f"/results/{sessionId}.zip"
        return {"zip_url": zip_url}
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)}) 