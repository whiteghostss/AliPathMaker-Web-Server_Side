from fastapi import APIRouter, Form
from fastapi.responses import JSONResponse
import os
import utils
from utils import save_source_to_java_file

router = APIRouter()

@router.post("/list-methods")
async def list_methods(sessionId: str = Form(...), filePath: str = Form(...)):
    if not filePath.lower().endswith(".java"):
        return JSONResponse(status_code=400, content={"error": "不是java源代码"})
    file_abs_path = os.path.join("uploads", sessionId, filePath)
    methods = utils.get_java_methods(file_abs_path)
    return {"methods": methods}


@router.post("/get-method-source")
async def get_method_source(sessionId: str = Form(...), filePath: str = Form(...), methodName: str = Form(...)):
    if not filePath.lower().endswith(".java"):
        return JSONResponse(status_code=400, content={"error": "不是java源代码"})
    file_abs_path = os.path.join("uploads", sessionId, filePath)
    source = utils.get_java_method_source_by_file(file_abs_path, methodName)
    if not source:
        return JSONResponse(status_code=404, content={"error": "Method not found"})
    # 新增：保存源码到 results/{sessionId}/PathAnalysis.java
    file_path = save_source_to_java_file(sessionId, source, class_name="PathAnalysis")
    # 确保返回的是正确格式的JSON
    return JSONResponse(content={
        "source": source,
        "file": file_path
    })
