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
    强制使用极高的垂直布局，确保图片尽可能高且窄
    """
    if not os.path.exists(dot_file_path):
        return False
    
    try:
        # 读取原始dot文件
        with open(dot_file_path, 'r', encoding='utf-8') as f:
            dot_content = f.read()
            
        # 在digraph定义后添加全局样式，强制使用极高的垂直布局
        styled_content = dot_content.replace('digraph {', '''digraph {
  // 强制使用极高的垂直布局
  graph [
    rankdir=TB,             // 从上到下布局
    splines=ortho,          // 使用直角连线
    nodesep=0.2,            // 最小节点水平间距
    ranksep=0.8,            // 增大层级垂直间距
    concentrate=true,       // 集中边线
    compound=true,          // 支持复合节点
    newrank=true,           // 更好的排名算法
    margin=0,               // 无边距
    ratio=compress,         // 压缩比例
    size="5,1000",          // 强制极窄极高
  ];
  // 节点样式 - 与分路径图保持一致
  node [
    shape=box, 
    style=filled, 
    fillcolor=lightblue, 
    fontname="WenQuanYi Zen Hei", 
    fontsize=10, 
    margin="0.05,0.05",     // 最小内部边距
    height=0.2,             // 最小高度
    width=0.1,              // 最小宽度
    fixedsize=false         // 允许根据内容调整大小
  ];
  // 边线样式
  edge [
    fontname="WenQuanYi Zen Hei", 
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
        
        # 使用dot命令生成最终图像，强制使用极高的垂直布局
        try:
            print("生成极高垂直布局的主图")
            # 首先尝试使用最强垂直布局参数，提高DPI到300以增强清晰度
            cmd = ["dot", "-Tpng", 
                   "-Gdpi=300",          # 提高DPI到300以增强清晰度
                   "-Gnodesep=0.4",      # 增加节点水平间隔以提高可读性
                   "-Granksep=0.8",      # 较大的垂直间隔
                   "-Gratio=compress",   # 压缩比例
                   "-Gsize=5,1000",      # 强制极窄极高
                   "-Gfontsize=12",      # 增大字体大小
                   "-Nfontsize=12",      # 增大节点字体大小
                   "-Efontsize=10",      # 增大边标签字体大小
                   dot_file_path, 
                   "-o", output_image_path]
            subprocess.run(cmd, check=True)
            
            # 检查生成的图片是否符合期望的宽高比
            width, height = get_image_dimensions(output_image_path)
            ratio = width / height if height else float('inf')
            
            # 如果比例不符合预期，尝试使用neato布局引擎
            if ratio > 1.0:  # 如果宽度仍然大于高度
                print(f"宽高比仍然过大({ratio})，尝试使用不同引擎")
                
                # 备份当前文件
                backup_path = output_image_path + ".backup.png"
                if os.path.exists(output_image_path):
                    shutil.copy(output_image_path, backup_path)
                
                # 尝试使用fdp引擎（力导向布局）
                cmd = ["fdp", "-Tpng", 
                       "-Gdpi=300",              # 提高DPI到300
                       "-Gorientation=portrait",  # 强制纵向
                       "-Gsize=5,1000!",         # 强制尺寸
                       "-Gfontsize=12",          # 增大字体
                       dot_file_path, 
                       "-o", output_image_path]
                subprocess.run(cmd, check=True)
                
                # 再次检查比例
                new_width, new_height = get_image_dimensions(output_image_path)
                new_ratio = new_width / new_height if new_height else float('inf')
                
                # 如果新比例更差，恢复备份
                if new_ratio > ratio and os.path.exists(backup_path):
                    print(f"新布局比例更差({new_ratio})，恢复之前的布局")
                    shutil.copy(backup_path, output_image_path)
                
                # 清理备份
                if os.path.exists(backup_path):
                    os.remove(backup_path)
                
        except Exception as e:
            print(f"优化主图布局失败: {e}，使用默认布局")
            # 如果优化失败，回退到简单的dot命令，但仍使用高DPI
            subprocess.run(["dot", "-Tpng", "-Gdpi=300", 
                           "-Gsize=5,1000", "-Gratio=compress",
                           "-Gfontsize=12", 
                           dot_file_path, "-o", output_image_path], check=True)
        
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

# ===================== 新增：AST预处理与路径评分辅助函数 =====================

def _iter_ast_nodes(obj):
    if isinstance(obj, dict):
        yield obj
        for v in obj.values():
            for n in _iter_ast_nodes(v):
                yield n
    elif isinstance(obj, list):
        for item in obj:
            for n in _iter_ast_nodes(item):
                yield n

_def_type_keys = ("type", "nodeType", "kind", "node_type")

_branch_types = set([
    # CamelCase
    "IfStatement", "SwitchStatement", "ConditionalExpression",
    "ForStatement", "WhileStatement", "DoStatement", "EnhancedForStatement",
    "TryStatement", "CatchClause",
    # snake_case
    "if_statement", "switch_statement", "conditional_expression",
    "for_statement", "while_statement", "do_statement", "enhanced_for_statement",
    "try_statement", "catch_clause"
])

_call_types = set([
    # CamelCase
    "MethodInvocation", "MethodCallExpr", "CallExpression", "Invocation",
    # snake_case
    "method_invocation"
])

_assign_types = set([
    # CamelCase
    "Assignment", "AssignExpr", "VariableDeclarator", "VariableDeclarationFragment",
    # snake_case
    "assignment", "assignment_expression", "variable_declarator", "local_variable_declaration"
])

_identifier_types = set([
    # CamelCase
    "SimpleName", "Identifier", "NameExpr",
    # snake_case
    "identifier"
])

def _get_node_type(node):
    if not isinstance(node, dict):
        return None
    for k in _def_type_keys:
        t = node.get(k)
        if isinstance(t, str):
            return t
    return None

def _get_node_name(node):
    if not isinstance(node, dict):
        return None
    # 常见命名字段
    for key in ("name", "identifier", "methodName", "member", "id"):
        val = node.get(key)
        if isinstance(val, str) and val:
            return val
        if isinstance(val, dict):
            # 有些结构 name:{identifier:"foo"}
            inner = val.get("identifier") or val.get("name")
            if isinstance(inner, str) and inner:
                return inner
    # 对图结构AST：identifier类节点的名称写在 label 字段
    t = _get_node_type(node)
    if t in _identifier_types:
        lbl = node.get("label")
        if isinstance(lbl, str) and lbl:
            return lbl
    return None

def _extract_identifiers(node):
    names = []
    for sub in _iter_ast_nodes(node):
        t = _get_node_type(sub)
        if t in identifier_types or 'name' in sub:
            nm = _get_node_name(sub)
            if isinstance(nm, str) and nm:
                names.append(nm)
    return names

# 修正：引用正确的集合名
identifier_types = _identifier_types

# ---------- 图结构 AST 工具 ----------

def _build_ast_graph(ast_data):
    """从图结构AST构建索引: id->node, children邻接表"""
    if not isinstance(ast_data, dict):
        return {}, {}
    nodes = ast_data.get('nodes') or []
    links = ast_data.get('links') or []
    id_to_node = {}
    children = {}
    for n in nodes:
        nid = n.get('id')
        if nid is not None:
            id_to_node[nid] = n
            children[nid] = []
    for e in links:
        if e.get('edge_type') == 'AST_edge':
            s = e.get('source')
            t = e.get('target')
            if s in children and t is not None:
                children[s].append(t)
    return id_to_node, children


def _collect_descendants(start_id, id_to_node, children, limit=200):
    """BFS 收集从 start_id 出发的后代节点(包含自身)"""
    if start_id not in id_to_node:
        return []
    visited = set()
    order = []
    queue = [start_id]
    while queue and len(order) < limit:
        cur = queue.pop(0)
        if cur in visited:
            continue
        visited.add(cur)
        order.append(id_to_node[cur])
        for nxt in children.get(cur, []):
            if nxt not in visited:
                queue.append(nxt)
    return order


def _extract_identifiers_from_subtree(nodes):
    names = []
    for n in nodes:
        if not isinstance(n, dict):
            continue
        t = _get_node_type(n)
        if t in _identifier_types:
            nm = _get_node_name(n)
            if nm:
                names.append(nm)
    return names


def _collect_assign_left_names_graph(subtree_nodes):
    """在子树中查找赋值/声明左值名(近似：variable_declarator/.. 下的 identifier)"""
    names = []
    for n in subtree_nodes:
        if not isinstance(n, dict):
            continue
        t = _get_node_type(n)
        if t in _assign_types:
            # 在其子树中尝试找 identifier 名称
            nm = _get_node_name(n)
            if nm and t in ("VariableDeclarator", "variable_declarator"):
                names.append(nm)
    # 同时所有 identifier 也收集，避免漏掉
    names.extend([x for x in _extract_identifiers_from_subtree(subtree_nodes)])
    return names


def _subtree_has_branch_graph(subtree_nodes):
    for n in subtree_nodes:
        if not isinstance(n, dict):
            continue
        t = _get_node_type(n)
        if t in _branch_types:
            return True
    return False

# ---------- 结束 图结构 AST 工具 ----------


def _node_line_span(node):
    """返回(node_line, start, end) 三元组，若不可得则返回(None, None, None)"""
    if not isinstance(node, dict):
        return (None, None, None)
    # 常见键
    line = node.get('line') or node.get('lineNumber') or node.get('lineno') or node.get('line_no')
    start_line = node.get('startLine') or node.get('beginLine') or node.get('start_line')
    end_line = node.get('endLine') or node.get('end_line') or node.get('finishLine')
    # loc结构
    loc = node.get('loc') or node.get('position') or {}
    if isinstance(loc, dict):
        start = loc.get('start') or {}
        end = loc.get('end') or {}
        sl = start.get('line') or start.get('lineNumber')
        el = end.get('line') or end.get('lineNumber')
        if sl and not start_line:
            start_line = sl
        if el and not end_line:
            end_line = el
    # 兜底：如果只有line则视为[start_line=end_line=line]
    if line and not start_line and not end_line:
        start_line = line
        end_line = line
    return (line, start_line, end_line)


def _covers_line(node, line_number):
    if not isinstance(line_number, int) or line_number <= 0:
        return False
    line, start, end = _node_line_span(node)
    if line and line == line_number:
        return True
    if start and end and isinstance(start, int) and isinstance(end, int):
        return start <= line_number <= end
    return False


def _find_subtrees_for_line(ast_root, line_number, max_results=10):
    matches = []
    for sub in _iter_ast_nodes(ast_root):
        if isinstance(sub, dict) and _covers_line(sub, line_number):
            matches.append(sub)
            if len(matches) >= max_results:
                break
    return matches


def _collect_method_invocations(node):
    names = []
    for sub in _iter_ast_nodes(node):
        if not isinstance(sub, dict):
            continue
        t = _get_node_type(sub)
        if t in _call_types:
            nm = _get_node_name(sub)
            if isinstance(nm, str) and nm:
                names.append(nm)
    return names


def _collect_assign_left_names(node):
    names = []
    for sub in _iter_ast_nodes(node):
        if not isinstance(sub, dict):
            continue
        t = _get_node_type(sub)
        if t in _assign_types:
            # 尝试从左值提取
            left = sub.get('left') or sub.get('lhs') or sub.get('name') or {}
            if isinstance(left, dict):
                nm = _get_node_name(left)
                if nm:
                    names.append(nm)
            elif isinstance(left, str):
                names.append(left)
            # VariableDeclarator/Fragment 场景
            if not names:
                nm = _get_node_name(sub)
                if nm:
                    names.append(nm)
    return names


def _is_branch_node(node):
    t = _get_node_type(node)
    return t in _branch_types


def _guess_line_from_path_node(n):
    if not isinstance(n, dict):
        return None
    for key in ("line", "lineNumber", "lineno", "line_no", "startLine", "beginLine"):
        v = n.get(key)
        if isinstance(v, int) and v > 0:
            return v
        # 有些是字符串
        if isinstance(v, str) and v.isdigit():
            return int(v)
    # 嵌套位置
    pos = n.get('position') or n.get('loc') or {}
    if isinstance(pos, dict):
        start = pos.get('start') or {}
        sl = start.get('line')
        if isinstance(sl, int) and sl > 0:
            return sl
    # 尝试从label前缀提取：如 "5_ xxx"
    lbl = n.get('label')
    if isinstance(lbl, str) and '_' in lbl:
        prefix = lbl.split('_', 1)[0].strip()
        if prefix.isdigit():
            return int(prefix)
    return None


def _extract_key_variables_from_ast(ast_root):
    """通过 MethodDeclaration/ReturnStatement 或图结构提取初始关键变量集合"""
    key_vars = set()
    if not ast_root:
        return key_vars
    # 图结构AST
    if isinstance(ast_root, dict) and 'nodes' in ast_root and 'links' in ast_root:
        id_to_node, children = _build_ast_graph(ast_root)
        # 方法参数
        for node in id_to_node.values():
            t = _get_node_type(node)
            if t in ("MethodDeclaration", "method_declaration"):
                subtree = _collect_descendants(node.get('id'), id_to_node, children)
                # formal_parameters 子树里的 identifier
                for sub in subtree:
                    st = _get_node_type(sub)
                    if st in ("formal_parameters", "formal_parameter"):
                        sub_sub = _collect_descendants(sub.get('id'), id_to_node, children)
                        for nm in _extract_identifiers_from_subtree(sub_sub):
                            key_vars.add(nm)
        # return 表达式中的identifier
        for node in id_to_node.values():
            t = _get_node_type(node)
            if t in ("ReturnStatement", "ReturnExpr", "Return", "return_statement"):
                subtree = _collect_descendants(node.get('id'), id_to_node, children)
                for nm in _extract_identifiers_from_subtree(subtree):
                    key_vars.add(nm)
        return key_vars
    # 嵌套对象AST（旧逻辑）
    for node in _iter_ast_nodes(ast_root):
        if not isinstance(node, dict):
            continue
        t = _get_node_type(node)
        if t == 'MethodDeclaration' or t == 'method_declaration':
            # 方法参数名
            params = node.get('parameters') or []
            if isinstance(params, list):
                for p in params:
                    nm = _get_node_name(p)
                    if nm:
                        key_vars.add(nm)
                    if isinstance(p, dict):
                        inner_nm = _get_node_name(p.get('name') or {})
                        if inner_nm:
                            key_vars.add(inner_nm)
        if t in ('ReturnStatement', 'ReturnExpr', 'Return', 'return_statement'):
            for nm in _extract_identifiers(node):
                if nm:
                    key_vars.add(nm)
    return key_vars


def _load_key_methods_config(output_dir):
    """可选：results/<sessionId>/key_methods.json，形如 {"save":2.0, "delete":2.5}"""
    cfg_path = os.path.join(output_dir, 'key_methods.json')
    if os.path.exists(cfg_path):
        try:
            with open(cfg_path, 'r', encoding='utf-8') as f:
                data = json.load(f)
            if isinstance(data, dict):
                cleaned = {}
                for k, v in data.items():
                    try:
                        cleaned[str(k)] = float(v)
                    except Exception:
                        continue
                return cleaned
        except Exception as e:
            print(f"加载关键方法配置失败: {e}")
    return {
        "save": 2.0,
        "update": 2.0,
        "delete": 2.5,
        "send": 1.5,
        "execute": 1.5,
        "query": 1.2,
        "insert": 2.0,
        "create": 1.5,
        "select": 1.2,
        "remove": 2.0,
        "commit": 1.8,
        "publish": 1.6,
        "dispatch": 1.6,
        "post": 1.4,
        "call": 1.0,
        "process": 1.2,
        "get": 0.8,
        "set": 0.8
    }


def _score_method_call(method_name, key_methods):
    if not isinstance(method_name, str) or not method_name:
        return 0.0
    if method_name in key_methods:
        return float(key_methods[method_name])
    name_lower = method_name.lower()
    best = 0.0
    for k, w in key_methods.items():
        k_lower = k.lower()
        if name_lower == k_lower or name_lower.startswith(k_lower) or k_lower in name_lower:
            best = max(best, float(w) * 0.9)
    return best


def _compute_scores_for_path(path_data, ast_root, key_vars, key_methods):
    score_calls = 0.0
    score_vars = 0.0
    score_branch = 0.0
    # 节点列表
    nodes = []
    if isinstance(path_data, dict):
        if 'nodes' in path_data and isinstance(path_data['nodes'], list):
            nodes = path_data['nodes']
        elif 'path_nodes' in path_data and isinstance(path_data['path_nodes'], list):
            nodes = path_data['path_nodes']
    # 图结构AST优先
    if isinstance(ast_root, dict) and 'nodes' in ast_root and 'links' in ast_root:
        id_to_node, children = _build_ast_graph(ast_root)
        for n in nodes:
            nid = n.get('id')
            if nid is None:
                continue
            subtree = _collect_descendants(nid, id_to_node, children)
            # 分支
            if _subtree_has_branch_graph(subtree):
                score_branch += 1.0
            # 赋值命中关键变量
            lefts = _collect_assign_left_names_graph(subtree)
            for lv in lefts:
                if lv in key_vars:
                    score_vars += 1.0
        # 忽略方法调用分
        score_calls = 0.0
    else:
        # 旧的嵌套对象 AST：通过行号近似
        for n in nodes:
            line_no = _guess_line_from_path_node(n)
            if not ast_root or not isinstance(line_no, int):
                continue
            subtrees = _find_subtrees_for_line(ast_root, line_no, max_results=5)
            for st in subtrees:
                # 忽略方法调用分
                # 赋值命中关键变量
                for lv in _collect_assign_left_names(st):
                    if lv in key_vars:
                        score_vars += 1.0
                # 分支结构
                if _is_branch_node(st):
                    score_branch += 1.0
    # 长度分
    path_length = path_data.get('path_length')
    if not isinstance(path_length, int):
        if isinstance(path_data, dict):
            if 'nodes' in path_data and isinstance(path_data['nodes'], list):
                path_length = len(path_data['nodes'])
            else:
                path_length = 0
        else:
            path_length = 0
    score_length = float(path_length)
    return {
        'score_calls': float(score_calls),
        'score_vars': float(score_vars),
        'score_branch': float(score_branch),
        'score_length': float(score_length)
    }


def _weighted_total(score_dict, weights=None):
    # 默认权重（暂不考虑关键方法分）
    if weights is None:
        weights = {
            'score_calls': 0.0,
            'score_vars': 0.5,
            'score_branch': 0.2,
            'score_length': 0.3,
        }
    total = 0.0
    for k, w in weights.items():
        total += float(score_dict.get(k, 0.0)) * float(w)
    return total

# ===================== 辅助函数定义结束 =====================

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
                 for old_file in utils.natural_sort(os.listdir(old_dir_path)):
                    os.remove(os.path.join(old_dir_path, old_file))
    except Exception as e:
        print(f"清理旧文件失败: {e}")
    
    # 1. 先执行AST分析
    try:
        ast_cmd = [
            'comex',
            '--lang', 'java',
            '--code-file', 'PathAnalysis.java',
            '--graphs', 'ast',
            '--output', 'all'
        ]
        subprocess.run(ast_cmd, check=True, timeout=60, cwd=output_dir)
        
        # 重命名AST分析结果
        ast_files = ['output.png', 'output.dot', 'output.json']
        ast_new_names = ['ast_output.png', 'ast_output.dot', 'ast_output.json']
        
        for old_name, new_name in zip(ast_files, ast_new_names):
            old_path = os.path.join(output_dir, old_name)
            new_path = os.path.join(output_dir, new_name)
            if os.path.exists(old_path):
                os.rename(old_path, new_path)
                
    except subprocess.CalledProcessError as e:
        print(f"AST分析失败: {str(e)}")
        # AST分析失败不影响CFG分析，继续执行
    except Exception as e:
        print(f"AST分析异常: {str(e)}")
        # AST分析异常不影响CFG分析，继续执行

    # 2. 执行CFG分析（保持原有逻辑不变）
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

    pictures_files = [os.path.join('results', sessionId, 'paths_img', f) for f in utils.natural_sort(os.listdir(paths_img_dir)) if f.endswith('.png')]
    paths_files = [os.path.join('results', sessionId, 'paths_json', f) for f in utils.natural_sort(os.listdir(paths_json_dir)) if f.endswith('.json')]
    dots_files = [os.path.join('results', sessionId, 'paths_dot', f) for f in utils.natural_sort(os.listdir(paths_dot_dir)) if f.endswith('.dot')]

    # 5. 组装 paths 信息
    paths = []
    for idx, json_file in enumerate(paths_files):
        json_path_full = os.path.join(paths_json_dir, os.path.basename(json_file))
        with open(json_path_full, 'r', encoding='utf-8') as f:
            path_data = json.load(f)
        
        img_file = pictures_files[idx] if idx < len(pictures_files) else ''
        dot_file = dots_files[idx] if idx < len(dots_files) else ''
        
        # 统一处理节点标签 - 确保从nodes字段中获取
        labels = []
        if isinstance(path_data, dict):
            if 'nodes' in path_data:
                # 从节点数据中提取标签
                labels = [n.get('label', '') for n in path_data['nodes']]
            elif 'paths' in path_data:
                # 如果已经有处理过的paths字段，直接使用
                labels = path_data['paths']
        
        # 提取图片尺寸信息
        img_info = path_data.get('image', {})
        width = img_info.get('width', 0)
        height = img_info.get('height', 0)
        aspect_ratio = img_info.get('aspect_ratio', 1.0)
        
        # 计算路径长度
        path_length = path_data.get('path_length', len(labels))
            
        paths.append({
            "path_json": json_file,
            "image_url": img_file,
            "dot_file": dot_file,
            "path": ' -> '.join(labels) if labels else '',
            "image_width": width,
            "image_height": height,
            "aspect_ratio": aspect_ratio,
            "path_length": path_length
        })

    # 6. 将paths信息写入每个paths_json文件 - 避免数据重复
    for path_info in paths:
        json_file = path_info["path_json"]
        json_path = os.path.join(paths_json_dir, os.path.basename(json_file))
        with open(json_path, 'r', encoding='utf-8') as f:
            original_data = json.load(f)
        
        # 提取 label 顺序
        label_list = path_info["path"].split(" -> ") if path_info["path"] else []
        
        # 避免重复添加paths字段
        if 'paths' not in original_data:
            original_data['paths'] = label_list
            
            # 保存更新后的数据
            with open(json_path, 'w', encoding='utf-8') as f:
                json.dump(original_data, f, ensure_ascii=False, indent=2)

    # ===================== 新增：预处理与路径评分阶段 =====================
    ast_json_path = os.path.join(output_dir, 'ast_output.json')
    ast_data = None
    if os.path.exists(ast_json_path):
        try:
            with open(ast_json_path, 'r', encoding='utf-8') as f:
                ast_data = json.load(f)
        except Exception as e:
            print(f"加载AST JSON失败: {e}")
            ast_data = None
    else:
        print("警告：未找到 ast_output.json，路径评分将退化为length打分")

    # 自动生成关键变量
    key_variables = _extract_key_variables_from_ast(ast_data) if ast_data else set()
    # 可选：加载人工配置的关键方法及权重
    key_methods = _load_key_methods_config(output_dir)

    # 遍历路径并打分
    path_scores = []  # 与 paths 下标对齐
    for idx, json_file in enumerate(paths_files):
        json_path_full = os.path.join(paths_json_dir, os.path.basename(json_file))
        try:
            with open(json_path_full, 'r', encoding='utf-8') as f:
                path_data = json.load(f)
        except Exception as e:
            print(f"读取路径JSON失败: {json_file}: {e}")
            path_scores.append(0.0)
            continue
        scores = _compute_scores_for_path(path_data, ast_data, key_variables, key_methods)
        total = _weighted_total(scores)
        path_scores.append(float(total))

    # 选出Top5路径下标
    # 生成完整降序索引数组（分数最高在最前）
    ranked = []
    if path_scores:
        ranked = sorted(range(len(path_scores)), key=lambda i: path_scores[i], reverse=True)

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
        "dots_files": dots_files,
        # 新增：打分与完整降序索引
        "path_scores": path_scores,
        "ranked_path_indices": ranked,
        # 新增：关键变量列表（可用于前端展示或调试）
        "key_variables": sorted(list(key_variables))
    }  
