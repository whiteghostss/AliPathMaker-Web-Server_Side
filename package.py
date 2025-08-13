from fastapi import APIRouter, HTTPException
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import List, Optional
import os
import utils
import re
import json
import uuid

class PathFileItem(BaseModel):
    png: Optional[str] = None
    dot: Optional[str] = None
    json: Optional[str] = None

class PackageRequest(BaseModel):
    sessionId: str
    output_json: Optional[str] = None
    output_dot: Optional[str] = None
    selected_paths: List[PathFileItem] = []
    methodName: Optional[str] = None  # 添加方法名参数

router = APIRouter()

def get_safe_filename(name: str) -> str:
    """
    将字符串转换为安全的文件名，去除非法字符
    """
    # 替换非法文件名字符为下划线
    name = re.sub(r'[\\/*?:"<>|]', '_', name)
    # 限制长度
    if len(name) > 50:
        name = name[:47] + "..."
    return name

def extract_method_info_from_java(java_path: str) -> str:
    """
    从Java文件中提取方法信息（类名.方法名）
    """
    try:
        with open(java_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 尝试提取类名
        class_match = re.search(r'public\s+class\s+(\w+)', content)
        class_name = class_match.group(1) if class_match else "PathAnalysis"
        
        # 尝试提取方法名
        method_match = re.search(r'(?:public|private|protected)?\s+\w+\s+(\w+)\s*\(', content)
        method_name = method_match.group(1) if method_match else "method"
        
        return f"{class_name}.{method_name}"
    except Exception as e:
        print(f"提取方法信息失败: {e}")
        return "PathAnalysis.method"

@router.post("/package-and-download", summary="多选打包源码、图片和路径文本为zip", response_description="返回zip下载链接")
async def package_and_download(data: PackageRequest):
    sessionId = data.sessionId
    output_json = data.output_json
    output_dot = data.output_dot
    selected_paths = data.selected_paths
    method_name = data.methodName  # 获取方法名
    
    base_dir = os.path.abspath(os.path.join("results", sessionId))
    file_list = []
    arc_file_list = []  # (绝对路径, zip内相对路径)
    
    # 必选：自动查找PathAnalysis.java和output.png
    java_path = os.path.join(base_dir, "PathAnalysis.java")
    output_png_path = os.path.join(base_dir, "output.png")
    for f in [java_path, output_png_path]:
        if not os.path.exists(f):
            return JSONResponse(status_code=404, content={"error": f"未找到文件: {f}"})
        file_list.append(f)
        arc_file_list.append((f, os.path.basename(f)))
    
    # 可选
    if output_json:
        json_path = os.path.join(base_dir, output_json)
        if not os.path.exists(json_path):
            return JSONResponse(status_code=404, content={"error": f"未找到文件: {json_path}"})
        file_list.append(json_path)
        arc_file_list.append((json_path, os.path.basename(json_path)))
    if output_dot:
        dot_path = os.path.join(base_dir, output_dot)
        if not os.path.exists(dot_path):
            return JSONResponse(status_code=404, content={"error": f"未找到文件: {dot_path}"})
        file_list.append(dot_path)
        arc_file_list.append((dot_path, os.path.basename(dot_path)))
    
    # 路径多选，分目录
    for path_item in selected_paths:
        for file_name in [path_item.png, path_item.dot, path_item.json]:
            if file_name:
                file_path = os.path.join(base_dir, file_name)
                if not os.path.exists(file_path):
                    return JSONResponse(status_code=404, content={"error": f"未找到路径相关文件: {file_path}"})
                file_list.append(file_path)
                # 按类型分目录
                ext = os.path.splitext(file_name)[1].lower()
                if ext == ".dot":
                    arcname = f"dot/{os.path.basename(file_name)}"
                elif ext == ".json":
                    arcname = f"json/{os.path.basename(file_name)}"
                elif ext in [".png", ".jpg", ".jpeg", ".bmp", ".gif"]:
                    arcname = f"img/{os.path.basename(file_name)}"
                else:
                    arcname = os.path.basename(file_name)
                arc_file_list.append((file_path, arcname))
    
    # 自动添加AST分析结果到打包列表
    ast_output_png = os.path.join(base_dir, "ast_output.png")
    ast_output_dot = os.path.join(base_dir, "ast_output.dot") 
    ast_output_json = os.path.join(base_dir, "ast_output.json")

    # 检查AST文件是否存在，如果存在则添加到打包列表
    ast_files = []
    if os.path.exists(ast_output_png):
        ast_files.append((ast_output_png, "ast_output.png"))
    if os.path.exists(ast_output_dot):
        ast_files.append((ast_output_dot, "ast_output.dot"))
    if os.path.exists(ast_output_json):
        ast_files.append((ast_output_json, "ast_output.json"))

    # 将AST文件添加到打包列表
    file_list.extend([f[0] for f in ast_files])
    arc_file_list.extend(ast_files)
    
    # 简化压缩包命名逻辑：只使用方法名和会话ID
    # zip_filename = sessionId
    
    # 如果前端提供了方法名，使用方法名命名
    if method_name:
        safe_method_name = get_safe_filename(method_name)
        unique_id = str(uuid.uuid4())
        zip_filename = f"{safe_method_name}_{unique_id}"
    else:
        # 尝试从Java文件中提取方法信息
        method_info = extract_method_info_from_java(java_path)
        if method_info:
            safe_method_info = get_safe_filename(method_info)
            unique_id = str(uuid.uuid4())
            zip_filename = f"{safe_method_info}_{unique_id}"
        else:
            unique_id = str(uuid.uuid4())
            zip_filename = f"PathAnalysis_{unique_id}"
    
    # 生成最终的zip路径 - 不再添加额外标识
    zip_path = os.path.join("results", f"{zip_filename}.zip")
    
    try:
        # 创建元数据，记录打包信息
        metadata = {
            "sessionId": sessionId,
            "methodName": method_name or extract_method_info_from_java(java_path),
            "pathCount": len(selected_paths),
            "packageTime": utils.get_formatted_time(),
            "files": [os.path.basename(f) for f in file_list]
        }
        
        # ========== 生成 method_info.txt =============
        # 1. 方法所在类在原始上传包中的路径
        # 假设上传解压目录为 uploads/{sessionId}，查找 .java 文件路径
        upload_dir = os.path.abspath(os.path.join("uploads", sessionId))
        java_file_path = None
        for root, dirs, files in os.walk(upload_dir):
            for file in files:
                if file.endswith(".java"):
                    java_file_path = os.path.join(root, file)
                    break
            if java_file_path:
                break
        java_file_rel = os.path.relpath(java_file_path, upload_dir) if java_file_path else "未找到原始Java文件"
        # 2. 方法源码（取 results/{sessionId}/PathAnalysis.java 全部内容）
        method_source = ""
        try:
            with open(java_path, "r", encoding="utf-8") as f:
                method_source = f.read()
        except Exception as e:
            method_source = f"读取 PathAnalysis.java 失败: {e}"
        # 3. 用户所选择的路径（文件名列表）
        selected_path_files = []
        for path_item in selected_paths:
            for file_name in [path_item.png, path_item.dot, path_item.json]:
                if file_name:
                    selected_path_files.append(file_name)
        # 组织内容
        txt_content = (
            f"【方法类原始路径】\n{java_file_rel}\n"
            f"\n==============================\n"
            f"【方法源码】\n{method_source}\n"
            f"\n==============================\n"
            f"【所选路径文件】\n" + '\n'.join(selected_path_files) + '\n'
        )
        extra_texts = {"metadata.json": json.dumps(metadata, ensure_ascii=False, indent=2), "method_info.txt": txt_content}
        
        # 将元数据添加到打包内容
        utils.package_selected_files(
            zip_path, 
            arc_file_list, 
            extra_texts=extra_texts, 
            base_dir=base_dir
        )
        
        zip_url = f"/results/{os.path.basename(zip_path)}"
        return {"zip_url": zip_url}
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)}) 