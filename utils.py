import os
import zipfile
import shutil
import uuid
import subprocess
import javalang
from typing import Optional, List

UPLOAD_ROOT = 'uploads'
RESULT_ROOT = 'results'

# 解压上传的zip文件到唯一临时目录，返回解压后路径和sessionId
def extract_zip(file_path: str) -> str:
    session_id = str(uuid.uuid4())
    extract_dir = os.path.join(UPLOAD_ROOT, session_id)
    os.makedirs(extract_dir, exist_ok=True)
    with zipfile.ZipFile(file_path, 'r') as zip_ref:
        zip_ref.extractall(extract_dir)
    return session_id, extract_dir

# 根据方法限定名查找Java源码
def find_java_method_source(project_dir: str, method_fqn: str) -> Optional[str]:
    parts = method_fqn.split('.')
    if len(parts) < 3:
        return None
    rel_path = os.path.join(*parts[:-1]) + '.java'
    method_name = parts[-1]
    # 递归查找
    for root, dirs, files in os.walk(project_dir):
        candidate = os.path.join(root, rel_path)
        if os.path.exists(candidate):
            with open(candidate, 'r', encoding='utf-8') as f:
                source = f.read()
            try:
                tree = javalang.parse.parse(source)
            except Exception:
                return None
            for path, node in tree:
                if isinstance(node, javalang.tree.MethodDeclaration) and node.name == method_name:
                    lines = source.splitlines()
                    start = node.position.line - 1
                    brace = 0
                    end = start
                    for i in range(start, len(lines)):
                        brace += lines[i].count('{') - lines[i].count('}')
                        if brace == 0 and i > start:
                            end = i
                            break
                    return '\n'.join(lines[start:end+1])
    return None

# 调用 comex 工具生成路径图片，返回图片路径列表
def call_comex(project_dir: str, method_fqn: str, output_dir: str) -> List[str]:
    os.makedirs(output_dir, exist_ok=True)
    # comex 可执行文件名为 comex.exe 或 comex.py
    # 这里 为comex.exe ，参数——>修改path.py中的调用
    cmd = [
        'comex',
        '--project', project_dir,
        '--method', method_fqn,
        '--output', output_dir
    ]
    subprocess.run(cmd, check=True)
    # 返回 output_dir 下所有图片路径
    return [os.path.join(output_dir, f) for f in os.listdir(output_dir) if f.endswith('.png')]

# 打包源码和图片为zip
def package_results(source_code: str, image_paths: List[str], zip_path: str):
    with zipfile.ZipFile(zip_path, 'w') as zipf:
        zipf.writestr('source.java', source_code)
        for img in image_paths:
            zipf.write(img, os.path.basename(img)) 