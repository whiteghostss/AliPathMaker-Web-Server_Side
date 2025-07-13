import json
import os
import subprocess

def load_json(path):
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)

def find_all_paths(graph, start_node, end_nodes):
    paths = []
    stack = [(start_node, [start_node])]
    while stack:
        (node, path) = stack.pop()
        if node in end_nodes:
            paths.append(path)
        else:
            for next_node in graph.get(node, []):
                if next_node not in path:  # 避免环路
                    stack.append((next_node, path + [next_node]))
    return paths

def generate_dot_for_path(path_nodes, nodes_info, edges_info, output_dot_path):
    with open(output_dot_path, 'w', encoding='utf-8') as f:
        f.write('digraph {\n')
        # 写入节点
        for node_id in path_nodes:
            node = nodes_info[node_id]
            label = node['label'].replace('"', '\\"')
            type_label = node.get('type_label', '')
            f.write(f'{node_id} [label="{label}", type_label="{type_label}"];\n')
        # 写入边
        for edge in edges_info:
            src = edge['source']
            tgt = edge['target']
            if src in path_nodes and tgt in path_nodes:
                # 仅保留路径上的边（tgt 必须是 src 的下一个节点）
                src_index = path_nodes.index(src)
                if src_index + 1 < len(path_nodes) and path_nodes[src_index + 1] == tgt:
                    label = edge.get('label', '').replace('"', '\\"')
                    color = edge.get('color', 'black')
                    controlflow_type = edge.get('controlflow_type', '').replace('"', '\\"')
                    f.write(f'{src} -> {tgt} [color="{color}", controlflow_type="{controlflow_type}", label="{label}"];\n')
        f.write('}\n')

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
    # 终点：没有出边的节点
    all_targets = set(link['target'] for link in links)
    all_sources = set(link['source'] for link in links)
    end_nodes = list(all_targets - all_sources)
    if not end_nodes:
        # 如果没有没有出边的节点，则找出邻接表中没有出边的节点
        end_nodes = [node_id for node_id in nodes_info if node_id not in graph]

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

        # 使用 dot 命令生成图片
        try:
            subprocess.run(['dot', '-Tpng', dot_path, '-o', img_path], check=True)
        except Exception as e:
            print(f'生成图片出错: {dot_path}: {e}')

        # 生成该路径的 json 文件
        path_nodes_set = set(path)
        path_nodes = [nodes_info[nid] for nid in path]
        path_links = [link for link in links if link['source'] in path_nodes_set and link['target'] in path_nodes_set]

        path_data = {
            'directed': data.get('directed', True),
            'multigraph': data.get('multigraph', True),
            'graph': data.get('graph', {}),
            'nodes': path_nodes,
            'links': path_links
        }

        with open(json_path, 'w', encoding='utf-8') as f:
            json.dump(path_data, f, ensure_ascii=False, indent=2)

    print(f'共找到路径数: {len(all_paths)}')
    print(f'DOT 文件保存在: {dot_output_dir}')
    print(f'图片保存在: {img_output_dir}')
    print(f'JSON 文件保存在: {json_output_dir}')
if __name__ == '__main__':
    main()
