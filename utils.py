import os
import zipfile
import tarfile
import rarfile
import py7zr
import shutil
import uuid
import subprocess
import javalang
from typing import Optional, List
import re
from graphviz import Source
import chardet

UPLOAD_ROOT = 'uploads'
RESULT_ROOT = 'results'

# 递归生成文件树JSON
def get_file_tree(dir_path: str, rel_path: str = "") -> dict:
    name = os.path.basename(dir_path) if rel_path == "" else os.path.basename(rel_path)
    node = {
        "name": name,
        "path": rel_path,
        "type": "directory" if os.path.isdir(dir_path) else "file"
    }
    if os.path.isdir(dir_path):
        node["children"] = []
        for entry in sorted(os.listdir(dir_path)):
            entry_path = os.path.join(dir_path, entry)
            entry_rel = os.path.join(rel_path, entry) if rel_path else entry
            node["children"].append(get_file_tree(entry_path, entry_rel))
    return node

# 提取Java文件中所有方法签名
def get_java_methods(file_path: str) -> list:
    import javalang
    methods = []
    if not os.path.exists(file_path):
        return methods
    with open(file_path, 'r', encoding='utf-8') as f:
        source = f.read()
    try:
        tree = javalang.parse.parse(source)
        class_stack = []
        for path, node in tree:
            if isinstance(node, javalang.tree.ClassDeclaration):
                class_stack.append(node.name)
            if isinstance(node, javalang.tree.MethodDeclaration):
                param_list = []
                for p in getattr(node, "parameters", []):
                    try:
                        # 类型名
                        type_name = getattr(getattr(p, "type", None), "name", "UnknownType")
                        # 维度
                        dim = getattr(getattr(p, "type", None), "dimensions", 0)
                        if isinstance(dim, list):
                            dim_count = len(dim)
                        elif isinstance(dim, int):
                            dim_count = dim
                        else:
                            dim_count = 0
                        param_str = type_name + ('[]' * dim_count)
                    except Exception as param_e:
                        print(f"警告: 解析参数失败: {param_e}, 参数对象: {p}")
                        param_str = "UnknownType"
                    param_list.append(param_str)
                params = ', '.join(param_list)
                # 获取当前类名（支持嵌套类）
                class_name = '.'.join(class_stack) if class_stack else ''
                if class_name:
                    sig = f"{class_name}.{node.name}({params})"
                else:
                    sig = f"{node.name}({params})"
                methods.append(sig)
            # 离开类作用域时弹栈
            if isinstance(node, javalang.tree.ClassDeclaration) and path and path[-1] is node:
                class_stack.pop()
    except Exception as e:
        print(f"解析Java方法失败: {e}\n文件: {file_path}")
    return methods

# 解压上传的压缩文件到唯一临时目录，返回解压后路径和sessionId
def extract_zip(file_path: str) -> str:
    session_id = str(uuid.uuid4())
    extract_dir = os.path.join(UPLOAD_ROOT, session_id)
    os.makedirs(extract_dir, exist_ok=True)
    ext = os.path.splitext(file_path)[1].lower()
    try:
        if ext == ".zip":
            # 手动遍历条目，尝试多种编码解码文件名，彻底解决中文乱码
            with zipfile.ZipFile(file_path, 'r') as zip_ref:
                for info in zip_ref.infolist():
                    # 依次尝试cp437->gbk, cp437->utf-8, utf-8
                    tried = False
                    for enc in [('cp437', 'gbk'), ('cp437', 'utf-8'), ('utf-8',)]:
                        try:
                            if len(enc) == 2:
                                name = info.filename.encode(enc[0]).decode(enc[1])
                            else:
                                name = info.filename.encode(enc[0]).decode(enc[0])
                            tried = True
                            break
                        except Exception:
                            continue
                    if not tried:
                        name = info.filename
                    target_path = os.path.join(extract_dir, name)
                    if info.is_dir():
                        os.makedirs(target_path, exist_ok=True)
                    else:
                        os.makedirs(os.path.dirname(target_path), exist_ok=True)
                        with open(target_path, 'wb') as f:
                            f.write(zip_ref.read(info.filename))
        elif ext in [".tar", ".gz", ".tgz", ".tar.gz"]:
            # tarfile一般支持utf-8，极少有中文乱码，如有异常可补充兼容
            try:
                with tarfile.open(file_path, 'r:*') as tar_ref:
                    tar_ref.extractall(extract_dir)
            except Exception as e:
                # 尝试逐个成员解码
                with tarfile.open(file_path, 'r:*') as tar_ref:
                    for member in tar_ref.getmembers():
                        try:
                            name = member.name.encode('utf-8').decode('gbk')
                        except Exception:
                            name = member.name
                        member.name = name
                        tar_ref.extract(member, extract_dir)
        elif ext == ".rar":
            # 手动遍历条目，尝试多种编码解码文件名
            with rarfile.RarFile(file_path) as rar_ref:
                for info in rar_ref.infolist():
                    tried = False
                    for enc in [('cp437', 'gbk'), ('cp437', 'utf-8'), ('utf-8',)]:
                        try:
                            if len(enc) == 2:
                                name = info.filename.encode(enc[0]).decode(enc[1])
                            else:
                                name = info.filename.encode(enc[0]).decode(enc[0])
                            tried = True
                            break
                        except Exception:
                            continue
                    if not tried:
                        name = info.filename
                    target_path = os.path.join(extract_dir, name)
                    if info.isdir():
                        os.makedirs(target_path, exist_ok=True)
                    else:
                        os.makedirs(os.path.dirname(target_path), exist_ok=True)
                        with open(target_path, 'wb') as f:
                            f.write(rar_ref.read(info))
        elif ext == ".7z":
            # py7zr不支持直接指定文件名编码，需手动重命名
            with py7zr.SevenZipFile(file_path, mode='r') as z:
                allnames = z.getnames()
                for orig_name in allnames:
                    tried = False
                    for enc in [('cp437', 'gbk'), ('cp437', 'utf-8'), ('utf-8',)]:
                        try:
                            if len(enc) == 2:
                                name = orig_name.encode(enc[0]).decode(enc[1])
                            else:
                                name = orig_name.encode(enc[0]).decode(enc[0])
                            tried = True
                            break
                        except Exception:
                            continue
                    if not tried:
                        name = orig_name
                    target_path = os.path.join(extract_dir, name)
                    z.extract(targets=[orig_name], path=os.path.dirname(target_path))
                    # 重命名
                    real_path = os.path.join(extract_dir, orig_name)
                    if os.path.exists(real_path):
                        os.rename(real_path, target_path)
        else:
            raise ValueError("暂不支持该压缩格式")
    except Exception as e:
        print(f"解压失败: {e}")
        raise
    # 解压成功后删除原始压缩包
    try:
        os.remove(file_path)
    except Exception as e:
        print(f"删除原始压缩包失败: {e}")
    return session_id, extract_dir

# 根据文件路径和方法名查找Java源码
def get_java_method_source_by_file(file_path: str, method_name: str) -> Optional[str]:
    import javalang
    if not os.path.exists(file_path):
        return None
    
    # 检测文件编码
    with open(file_path, 'rb') as f:
        raw_data = f.read()
        result = chardet.detect(raw_data)
        encoding = result['encoding']
    
    # 使用检测到的编码打开文件
    try:
        with open(file_path, 'r', encoding=encoding) as f:
            source = f.read()
    except:
        # 回退到utf-8
        with open(file_path, 'r', encoding='utf-8', errors='replace') as f:
            source = f.read()
    
    try:
        # 支持 method_name 形如 ClassName.methodName(...) 或 methodName(...)
        if '.' in method_name:
            class_part, method_part = method_name.split('.', 1)
            method_base = method_part.split('(')[0]
        else:
            class_part = None
            method_base = method_name.split('(')[0]
        
        # 先尝试使用javalang解析
        try:
            tree = javalang.parse.parse(source)
            class_stack = []
            for path, node in tree:
                if isinstance(node, javalang.tree.ClassDeclaration):
                    class_stack.append(node.name)
                if isinstance(node, javalang.tree.MethodDeclaration):
                    current_class = '.'.join(class_stack) if class_stack else ''
                    # 判断类名和方法名是否匹配
                    class_match = (not class_part) or (current_class.endswith(class_part))
                    method_match = node.name == method_base
                    if class_match and method_match:
                        # 使用行号提取方法源码
                        lines = source.splitlines()
                        start = node.position.line - 1
                        # 寻找方法体的结束
                        end = start
                        brace_count = 0
                        found_opening_brace = False
                        
                        for i in range(start, len(lines)):
                            line = lines[i]
                            
                            # 计算花括号
                            for char in line:
                                if char == '{':
                                    brace_count += 1
                                    found_opening_brace = True
                                elif char == '}':
                                    brace_count -= 1
                            
                            # 找到方法体结束
                            if found_opening_brace and brace_count <= 0:
                                end = i
                                break
                        
                        # 提取整个方法
                        return '\n'.join(lines[start:end+1])
                # 离开类作用域时弹栈
                if isinstance(node, javalang.tree.ClassDeclaration) and path and path[-1] is node:
                    class_stack.pop()
        except Exception as e:
            print(f"javalang解析失败，使用替代方法: {e}")
            
        # 如果javalang解析失败，使用简单的文本匹配作为备用方案
        lines = source.splitlines()
        for i, line in enumerate(lines):
            if method_base in line and '(' in line and ')' in line:
                # 可能是方法定义行
                start = i
                brace_count = 0
                found_opening_brace = False
                
                # 查找方法体
                for j in range(start, len(lines)):
                    current_line = lines[j]
                    
                    # 计算花括号
                    for char in current_line:
                        if char == '{':
                            brace_count += 1
                            found_opening_brace = True
                        elif char == '}':
                            brace_count -= 1
                    
                    # 找到方法体结束
                    if found_opening_brace and brace_count <= 0:
                        return '\n'.join(lines[start:j+1])
    
    except Exception as e:
        print(f"解析Java方法源码失败: {e}")
    
    return None

# 调用 comex 工具生成路径图片和文件，
def call_comex(project_dir: str, method_fqn: str, output_dir: str) -> List[str]:
    os.makedirs(output_dir, exist_ok=True)
    cmd = [
        'comex',
        '--project', project_dir,
        '--method', method_fqn,
        '--output', output_dir
    ]
    subprocess.run(cmd, check=True)
    return [os.path.join(output_dir, f) for f in os.listdir(output_dir) if f.endswith('.png')]


def get_formatted_time() -> str:
    """
    获取当前时间的格式化字符串，用于打包时间标记
    """
    from datetime import datetime
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


def save_source_to_java_file(session_id: str, source: str, class_name: str = "PathAnalysis") -> str:
    """
    保存Java源码到文件，并确保格式和编码正确。
    源码可能是方法体，也可能已经包含类声明，需要进行判断处理。
    """
    output_dir = os.path.join(RESULT_ROOT, session_id)
    os.makedirs(output_dir, exist_ok=True)
    java_file = os.path.join(output_dir, f"{class_name}.java")
    
    # 清理源码，去除可能的前后空行
    source = source.strip()
    
    # 判断源码是否已经包含类声明
    if source.startswith("public class") or source.startswith("class"):
        # 已包含类声明，直接保存
        with open(java_file, "w", encoding="utf-8") as f:
            f.write(source)
    else:
        # 只有方法体，添加类声明
        with open(java_file, "w", encoding="utf-8") as f:
            f.write(f"public class {class_name} {{\n")
            f.write(source)
            # 如果源码没有结束花括号，添加一个
            if not source.rstrip().endswith("}"):
                f.write("\n}")
            else:
                f.write("\n}")
    
    # 拷贝extract_paths_and_generate_dot.py到输出目录
    try:
        extract_script = os.path.join(os.path.dirname(os.path.dirname(output_dir)), "extract_paths_and_generate_dot.py")
        if os.path.exists(extract_script):
            shutil.copy(extract_script, output_dir)
    except Exception as e:
        print(f"复制路径提取脚本失败: {e}")
    
    return java_file


def package_selected_files(zip_path: str, file_list: list, extra_texts: dict = None, base_dir: str = None):
    """
    将 file_list 中的所有文件打包进 zip_path。extra_texts 可选，所有文件必须在 base_dir（如 results/{session_id}）下。
    """
    if base_dir:
        base_dir = os.path.abspath(base_dir)
    with zipfile.ZipFile(zip_path, 'w') as zipf:
        for file in file_list:
            abs_file = os.path.abspath(file)
            if base_dir and not abs_file.startswith(base_dir):
                raise ValueError(f"文件 {file} 不在指定目录 {base_dir} 下，禁止打包！")
            arcname = os.path.basename(file)
            zipf.write(abs_file, arcname)
        if extra_texts:
            for fname, content in extra_texts.items():
                zipf.writestr(fname, content)



