from fastapi import APIRouter, Form, HTTPException
from fastapi.responses import JSONResponse
import os
import utils
import json
import subprocess
import chardet
import sys
from PIL import Image
import shutil

router = APIRouter()

def optimize_main_graph(dot_file_path, output_image_path):
    """
    优化主图的dot文件，添加样式和布局参数，并重新生成图片
    使用和分路径一致的颜色方案，保持窄高比例，去除左右空白
    """
    if not os.path.exists(dot_file_path):
        return False
    
    try:
        # 读取原始dot文件
        with open(dot_file_path, 'r', encoding='utf-8') as f:
            dot_content = f.read()
            
        # 在digraph定义后添加全局样式，设置垂直布局参数，尽量减小宽度
        styled_content = dot_content.replace('digraph {', '''digraph {
  // 使用自上而下布局，并允许垂直方向自由扩展，水平方向尽量窄
  graph [
    rankdir=TB,             // 从上到下布局
    splines=ortho,          // 使用直角连线
    nodesep=0.3,            // 减小节点水平间距
    ranksep=0.5,            // 层级垂直间距
    concentrate=true,       // 集中边线
    compound=true,          // 支持复合节点
    newrank=true,           // 更好的排名算法
    margin=0.1,             // 减小图表外边距
    ratio=fill,             // 填充可用空间
    page="8.5,11",          // 指定页面比例为A4纸比例
  ];
  // 节点样式 - 与分路径图保持一致
  node [
    shape=box, 
    style=filled, 
    fillcolor=lightblue, 
    fontname=Arial, 
    fontsize=10, 
    margin="0.1,0.1",       // 减小内部边距
    height=0.3,             // 控制高度
    width=0                 // 宽度自动调整
  ];
  // 边线样式
  edge [
    fontname=Arial, 
    fontsize=8, 
    arrowsize=0.6
  ];
''')
        
        # 添加节点样式（与分路径图相同的颜色规则）
        lines = styled_content.split('\n')
        new_lines = []
        for line in lines:
            if '[' in line and ']' in line:
                if 'type_label=' in line or '"type_label"' in line:
                    # 这可能是一个节点定义行
                    if 'type_label="start"' in line or '"type_label":"start"' in line:
                        line = line.replace('fillcolor=lightblue', 'fillcolor=lightgreen')
                        line = line.replace('style=filled', 'style=filled, penwidth=2')
                    elif 'type_label="end"' in line or '"type_label":"end"' in line:
                        line = line.replace('fillcolor=lightblue', 'fillcolor=lightpink')
                        line = line.replace('style=filled', 'style=filled, penwidth=2')
                
                # 不缩短标签，保留完整节点信息
            new_lines.append(line)
        
        styled_content = '\n'.join(new_lines)
        
        # 保存优化后的dot文件
        with open(dot_file_path, 'w', encoding='utf-8') as f:
            f.write(styled_content)
        
        # 使用dot命令生成最终图像，强制使用垂直布局
        try:
            print("生成窄高比例的主图")
            cmd = ["dot", "-Tpng", 
                   "-Gdpi=150",
                   "-Gnodesep=0.3",  # 控制水平间隔
                   "-Granksep=0.5",  # 控制垂直间隔
                   "-Gmargin=0.1",   # 减小外边距
                   dot_file_path, 
                   "-o", output_image_path]
            subprocess.run(cmd, check=True)
            
            # 检查生成的图片是否符合期望的宽高比
            width, height = get_image_dimensions(output_image_path)
            ratio = width / height if height else float('inf')
            
            # 如果比例不符合预期，尝试重新生成
            if ratio > 1.5:  # 如果宽高比超过1.5，尝试使用更强的垂直压缩
                print(f"宽高比过大({ratio})，尝试使用不同参数重新生成")
                cmd = ["dot", "-Tpng", 
                       "-Gdpi=150",
                       "-Gnodesep=0.2",  # 更小的水平间隔
                       "-Granksep=0.6",  # 更大的垂直间隔
                       "-Gsize=7,0!",    # 限制最大宽度
                       dot_file_path, 
                       "-o", output_image_path]
                subprocess.run(cmd, check=True)
                
        except Exception as e:
            print(f"优化主图布局失败: {e}，使用默认布局")
            # 如果优化失败，回退到简单的dot命令
            subprocess.run(["dot", "-Tpng", "-Gdpi=150", dot_file_path, "-o", output_image_path], check=True)
        
        return True
    except Exception as e:
        print(f"优化主图失败: {e}")
        return False

def get_image_dimensions(image_path):
    """获取图片尺寸"""
    try:
        with Image.open(image_path) as img:
            return img.size  # (width, height)
    except Exception as e:
        print(f"获取图片尺寸失败: {e}")
        return (0, 0)

@router.post("/analyze-paths", summary="分析已保存的Java文件，生成路径图片和路径数据", response_description="返回图片URL和路径数据列表")
async def analyze_paths(sessionId: str = Form(...)):
    if not sessionId:
        raise HTTPException(status_code=400, detail="sessionId为必填！")
    output_dir = os.path.join("results", sessionId)
    code_file = os.path.join(output_dir, "PathAnalysis.java")
    if not os.path.exists(code_file):
        return JSONResponse(status_code=404, content={"error": f"未找到Java文件: {code_file}"})
    
    # 清理旧的分析结果文件
    try:
        # 删除已存在的输出文件
        for old_file in ['output.png', 'output.dot', 'output.json']:
            old_path = os.path.join(output_dir, old_file)
            if os.path.exists(old_path):
                os.remove(old_path)
                
        # 删除已存在的路径目录
        for old_dir in ['paths_dot', 'paths_img', 'paths_json']:
            old_dir_path = os.path.join(output_dir, old_dir)
            if os.path.exists(old_dir_path):
                for old_file in os.listdir(old_dir_path):
                    os.remove(os.path.join(old_dir_path, old_file))
    except Exception as e:
        print(f"清理旧文件失败: {e}")
    
    # 1. 在 results/{sessionId} 目录下执行 comex，生成所有输出
    try:
        cmd = [
            'comex',
            '--lang', 'java',
            '--code-file', 'PathAnalysis.java',
            '--graphs', 'cfg',
            '--output', 'all'
        ]
        subprocess.run(cmd, check=True, timeout=60, cwd=output_dir)
    except subprocess.CalledProcessError as e:
        return JSONResponse(status_code=500, content={"error": f"comex failed: {str(e)}"})
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": f"comex error: {str(e)}"})
    # 2. 检查输出文件
    img_path = os.path.join(output_dir, "output.png")
    dot_path = os.path.join(output_dir, "output.dot")
    json_path = os.path.join(output_dir, "output.json")
    if not os.path.exists(img_path):
        return JSONResponse(status_code=500, content={"error": "No images generated by comex"})
    if not os.path.exists(dot_path):
        return JSONResponse(status_code=500, content={"error": "No dot file generated by comex"})
    if not os.path.exists(json_path):
        return JSONResponse(status_code=500, content={"error": "No json generated by comex"})
        
    # 2.1 优化主图的样式并重新生成
    optimize_main_graph(dot_path, img_path)
    
    # 获取主图尺寸
    main_img_width, main_img_height = get_image_dimensions(img_path)
    main_img_aspect_ratio = main_img_width / main_img_height if main_img_height != 0 else 1.0
    
    # 3. 调用extract_paths_and_generate_dot.py脚本进行路径拆解和文件生成
    try:
        result = subprocess.run(
            [sys.executable, '../../extract_paths_and_generate_dot.py'],
            check=True,
            cwd=output_dir,
            capture_output=True,
            text=True
        )
    except subprocess.CalledProcessError as e:
        return JSONResponse(status_code=500, content={"error": f"路径拆解脚本执行失败: {e.stderr or e.stdout or str(e)}"})
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": f"路径拆解脚本执行异常: {str(e)}"})

    # 4. 收集脚本生成的结果文件
    paths_dot_dir = os.path.join(output_dir, 'paths_dot')
    paths_img_dir = os.path.join(output_dir, 'paths_img')
    paths_json_dir = os.path.join(output_dir, 'paths_json')

    pictures_files = sorted([os.path.join('results', sessionId, 'paths_img', f) for f in os.listdir(paths_img_dir) if f.endswith('.png')])
    paths_files = sorted([os.path.join('results', sessionId, 'paths_json', f) for f in os.listdir(paths_json_dir) if f.endswith('.json')])
    dots_files = sorted([os.path.join('results', sessionId, 'paths_dot', f) for f in os.listdir(paths_dot_dir) if f.endswith('.dot')])

    # 5. 组装 paths 信息
    paths = []
    for idx, json_file in enumerate(paths_files):
        with open(os.path.join(paths_json_dir, os.path.basename(json_file)), 'r', encoding='utf-8') as f:
            path_data = json.load(f)
        img_file = pictures_files[idx] if idx < len(pictures_files) else ''
        dot_file = dots_files[idx] if idx < len(dots_files) else ''
        # 尝试获取label顺序
        labels = []
        if isinstance(path_data, dict) and 'nodes' in path_data:
            labels = [n['label'] for n in path_data['nodes']]
        elif isinstance(path_data, list):
            labels = path_data
            
        # 提取图片尺寸信息
        img_info = path_data.get('image', {})
        width = img_info.get('width', 0)
        height = img_info.get('height', 0)
        aspect_ratio = img_info.get('aspect_ratio', 1.0)
            
        paths.append({
            "path_json": json_file,
            "image_url": img_file,
            "dot_file": dot_file,
            "path": ' -> '.join(labels) if labels else '',
            "image_width": width,
            "image_height": height,
            "aspect_ratio": aspect_ratio,
            "path_length": path_data.get('path_length', len(labels))
        })

    # 6. 将paths信息写入每个paths_json文件
    for path_info in paths:
        json_file = path_info["path_json"]
        json_path = os.path.join(paths_json_dir, os.path.basename(json_file))
        with open(json_path, 'r', encoding='utf-8') as f:
            original_data = json.load(f)
        # 提取 label 顺序
        label_list = path_info["path"].split(" -> ") if path_info["path"] else []
        new_data = {"paths": label_list}
        new_data.update(original_data)
        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump(new_data, f, ensure_ascii=False, indent=2)

    # 6. 返回
    all_image = os.path.relpath(os.path.join(output_dir, "output.png"), '.')
    return {
        "all_image": all_image,
        "all_image_width": main_img_width,
        "all_image_height": main_img_height,
        "all_image_aspect_ratio": main_img_aspect_ratio,
        "paths": paths,
        "pictures_files": pictures_files,
        "paths_files": paths_files,
        "dots_files": dots_files
    }  