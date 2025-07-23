import json
import os
import subprocess
from collections import deque  # 导入deque用于实现队列

def load_json(path):
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)

def find_all_paths(graph, start_node, end_nodes):
    """
    使用广度优先搜索查找从起点到终点的所有路径
    避免环路，同时保证路径顺序更符合控制流的自然顺序
    """
    paths = []
    # 使用队列而不是栈，实现广度优先搜索
    queue = deque([(start_node, [start_node])])
    while queue:
        (node, path) = queue.popleft()  # 从队列左侧弹出，保证广度优先
        if node in end_nodes:
            paths.append(path)
        else:
            # 按照节点ID排序，使路径更具确定性
            next_nodes = sorted(graph.get(node, []))
            for next_node in next_nodes:
                if next_node not in path:  # 避免环路
                    queue.append((next_node, path + [next_node]))
    return paths

def wrap_label(label, width=40):
    # 按空格分割，拼接时每行不超过width字符
    words = label.split(' ')
    lines = []
    current = ''
    for word in words:
        if len(current) + len(word) + 1 > width:
            lines.append(current)
            current = word
        else:
            if current:
                current += ' '
            current += word
    if current:
        lines.append(current)
    return '\n'.join(lines)

def generate_dot_for_path(path_nodes, nodes_info, edges_info, output_dot_path):
    with open(output_dot_path, 'w', encoding='utf-8') as f:
        f.write('digraph {\n')
        # 全局图形设置 - 使用垂直布局，允许高度自适应，优化短方法的显示
        f.write('  graph [\n')
        f.write('    rankdir=TB,\n')         # 从上到下布局
        f.write('    splines=ortho,\n')      # 使用直角连线
        f.write('    nodesep=0.5,\n')        # 增加节点间水平间距
        f.write('    ranksep=0.7,\n')        # 增加层级垂直间距
        f.write('    newrank=true,\n')       # 改进的排名算法
        f.write('    fontsize=12,\n')        # 增大默认字体
        f.write('    dpi=300\n')             # 设置高DPI
        f.write('    fontname="WenQuanYi Zen Hei",\n')
        f.write('  ];\n')
        
        # 优化节点样式，提高清晰度
        f.write('  node [\n')
        f.write('    shape=box,\n')
        f.write('    style=filled,\n')
        f.write('    fillcolor=lightblue,\n')
        f.write('    fontname="WenQuanYi Zen Hei",\n')
        f.write('    fontsize=12,\n')        # 增大节点字体
        f.write('    margin="0.25,0.15",\n') # 增加内边距
        f.write('    penwidth=1.5,\n')       # 增加边框粗细
        f.write('    height=0.4\n')          # 增加最小高度
        f.write('  ];\n')
        
        # 优化边线样式
        f.write('  edge [\n')
        f.write('    fontname="WenQuanYi Zen Hei",\n')
        f.write('    fontsize=10,\n')        # 边标签字体大小
        f.write('    penwidth=1.2,\n')       # 增加线条粗细
        f.write('    arrowsize=0.8\n')       # 增大箭头
        f.write('  ];\n')
        
        # 写入节点
        for node_id in path_nodes:
            node = nodes_info[node_id]
            label = node['label'].replace('"', '\\"')
            label = wrap_label(label, width=40)  # 自动换行
            type_label = node.get('type_label', '')
            
            # 根据节点类型设置不同样式
            node_style = 'filled'
            node_color = 'lightblue'
            if type_label == 'start':
                node_color = 'lightgreen'
                node_style = 'filled,bold'
            elif type_label == 'end' or node_id in path_nodes[-1:]:
                node_color = 'lightpink'
                node_style = 'filled,bold'
            
            f.write(f'  {node_id} [label="{label}", type_label="{type_label}", style="{node_style}", fillcolor="{node_color}"];\n')
        
        # 创建路径节点顺序的映射，用于确定边是否在路径上
        path_order = {node_id: idx for idx, node_id in enumerate(path_nodes)}
        path_nodes_set = set(path_nodes)
        
        # 写入边 - 改进的边选择逻辑
        for edge in edges_info:
            src = edge['source']
            tgt = edge['target']
            
            # 检查源节点和目标节点是否都在路径中
            if src in path_nodes_set and tgt in path_nodes_set:
                src_idx = path_order.get(src)
                tgt_idx = path_order.get(tgt)
                
                # 检查这条边是否是路径的一部分（目标节点在源节点之后）
                if src_idx is not None and tgt_idx is not None and src_idx < tgt_idx:
                    # 检查是否是直接相连的节点，或者是跳转边（如循环、条件跳转等）
                    is_direct_edge = tgt_idx == src_idx + 1
                    is_jump_edge = not is_direct_edge
                    
                    label = edge.get('label', '').replace('"', '\\"')
                    color = edge.get('color', 'black')
                    controlflow_type = edge.get('controlflow_type', '').replace('"', '\\"')
                    
                    # 设置边样式
                    edge_style = "solid" if is_direct_edge else "dashed"
                    
                    if "true" in controlflow_type.lower():
                        color = "darkgreen"
                        edge_style = "bold"
                    elif "false" in controlflow_type.lower():
                        color = "red"
                        edge_style = "bold"
                    
                    # 为跳转边添加更明显的标记
                    if is_jump_edge:
                        label = f"jump: {label}" if label else "jump"
                    
                    # 将label改为xlabel，以解决正交边与标签冲突的问题
                    f.write(f'  {src} -> {tgt} [color="{color}", controlflow_type="{controlflow_type}", xlabel="{label}", style="{edge_style}"];\n')
        f.write('}\n')

# 添加获取图片尺寸的函数
def get_image_dimensions(image_path):
    try:
        from PIL import Image
        with Image.open(image_path) as img:
            return img.size  # (width, height)
    except Exception as e:
        print(f"获取图片尺寸失败: {e}")
        return (0, 0)



def main():
    json_path = 'output.json'
    dot_output_dir = 'paths_dot'
    img_output_dir = 'paths_img'
    json_output_dir = 'paths_json'

    os.makedirs(dot_output_dir, exist_ok=True)
    os.makedirs(img_output_dir, exist_ok=True)
    os.makedirs(json_output_dir, exist_ok=True)

    data = load_json(json_path)
    nodes = data['nodes']
    links = data['links']

    nodes_info = {node['id']: node for node in nodes}

    # 构建邻接表
    graph = {}
    for link in links:
        src = link['source']
        tgt = link['target']
        graph.setdefault(src, []).append(tgt)

    # 找到所有起点和终点
    start_nodes = [node['id'] for node in nodes if node.get('type_label') == 'start']
    # 优先使用type_label为end的节点作为终点
    end_nodes = [node['id'] for node in nodes if node.get('type_label') == 'end']
    
    # 如果没有明确标记的终点，则使用没有出边的节点作为终点
    if not end_nodes:
        all_targets = set(link['target'] for link in links)
        all_sources = set(link['source'] for link in links)
        end_nodes = list(all_targets - all_sources)
        
        # 如果仍然没有终点，则找出邻接表中没有出边的节点
        if not end_nodes:
            end_nodes = [node_id for node_id in nodes_info if node_id not in graph]
    
    print(f"找到起点节点: {start_nodes}")
    print(f"找到终点节点: {end_nodes}")

    # 对每个起点，查找所有到任意终点的路径
    all_paths = []
    for start in start_nodes:
        paths = find_all_paths(graph, start, end_nodes)
        all_paths.extend(paths)

    # 为每条路径生成 dot 文件、图片和 json 文件
    for i, path in enumerate(all_paths):
        dot_path = os.path.join(dot_output_dir, f'path_{i+1}.dot')
        img_path = os.path.join(img_output_dir, f'path_{i+1}.png')
        json_path = os.path.join(json_output_dir, f'path_{i+1}.json')

        # 生成 dot 文件
        generate_dot_for_path(path, nodes_info, links, dot_path)
        

        # 使用 dot 命令生成图片，添加参数以优化图像
        try:
            # 使用垂直布局引擎，允许高度自适应，提高DPI和清晰度
            cmd = ['dot', '-Tpng', 
                   '-Gdpi=300',       # 提高DPI到300以增强清晰度
                   '-Gnodesep=0.5',   # 增加节点间隔以提高可读性
                   '-Granksep=0.7',   # 增加层级间隔
                   '-Gfontsize=12',   # 增大字体大小
                   '-Nfontsize=12',   # 增大节点字体大小
                   '-Efontsize=10',   # 增大边标签字体大小
                   dot_path, 
                   '-o', img_path]
            
            import subprocess
            subprocess.run(cmd, check=True)
            
            # 如果dot命令失败，尝试使用不同的布局引擎
            if not os.path.exists(img_path) or os.path.getsize(img_path) == 0:
                print(f"dot引擎生成图片失败，尝试使用fdp引擎: {dot_path}")
                fdp_cmd = ['fdp', '-Tpng', 
                          '-Gdpi=300',
                          '-Gorientation=portrait',
                          '-Gfontsize=12',
                          dot_path, 
                          '-o', img_path]
                subprocess.run(fdp_cmd, check=True)
                
                # 如果fdp也失败，尝试使用neato引擎
                if not os.path.exists(img_path) or os.path.getsize(img_path) == 0:
                    print(f"fdp引擎也失败，尝试使用neato引擎: {dot_path}")
                    neato_cmd = ['neato', '-Tpng', 
                               '-Gdpi=300',
                               dot_path, 
                               '-o', img_path]
                    subprocess.run(neato_cmd, check=True)
        except Exception as e:
            print(f'生成图片出错: {dot_path}: {e}')
            # 尝试使用最简单的命令
            try:
                simple_cmd = ['dot', '-Tpng', dot_path, '-o', img_path]
                subprocess.run(simple_cmd, check=True)
            except Exception as e2:
                print(f'简单命令也失败: {e2}')
                # 最后尝试使用circo引擎，它对某些图形结构更友好
                try:
                    circo_cmd = ['circo', '-Tpng', dot_path, '-o', img_path]
                    subprocess.run(circo_cmd, check=True)
                except Exception as e3:
                    print(f'所有引擎都失败: {e3}')

        # 获取图片尺寸
        img_width, img_height = get_image_dimensions(img_path)
        aspect_ratio = img_width / img_height if img_height != 0 else 1.0

        # 生成该路径的 json 文件
        path_nodes_set = set(path)
        path_nodes = [nodes_info[nid] for nid in path]
        path_links = [link for link in links if link['source'] in path_nodes_set and link['target'] in path_nodes_set]

        path_data = {
            'directed': data.get('directed', True),
            'multigraph': data.get('multigraph', True),
            'graph': data.get('graph', {}),
            'nodes': path_nodes,
            'links': path_links,
            'path_length': len(path),
            'image': {
                'width': img_width,
                'height': img_height,
                'aspect_ratio': aspect_ratio
            }
        }

        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump(path_data, f, ensure_ascii=False, indent=2)

    print(f'共找到路径数: {len(all_paths)}')
    print(f'DOT 文件保存在: {dot_output_dir}')
    print(f'图片保存在: {img_output_dir}')
    print(f'JSON 文件保存在: {json_output_dir}')



if __name__ == '__main__':
    main()
