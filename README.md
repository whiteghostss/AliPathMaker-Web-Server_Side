## 运行

### 推荐方式（Docker）：

```
bash deploy.sh
# 可能出现 python:3.10-slim拉取的网络问题，可以先docker pull python:3.10-slim再运行bash deploy.sh
```

### 本地运行（不使用 Docker）：

```
pip install -r requirements.txt
uvicorn main:app --reload
```

## 常用调试命令

- 查看容器日志（实时输出后端 print/logging）：

  ```
  docker logs -f aipathmaker-backend
  ```

- 进入容器内部（需要 Dockerfile 已安装 bash）：

  ```
  docker exec -it aipathmaker-backend bash
  ```

- 查看解压后的上传目录（容器内操作，sessionId 替换为实际值）：

  ```
  cd uploads/你的sessionId
  ls -l
  ```

- 停止并删除容器：
  ```
  docker rm -f aipathmaker-backend
  ```



## 路径分析API与打包API接口

### 1. 路径分析API

#### 接口地址

```
POST /api/analyze-paths
```

#### 功能

分析指定 sessionId 下的 Java 文件，生成主控流图、所有路径的图片、dot、json 文件，并返回所有相关文件的路径信息。

#### 请求参数

| 参数名    | 类型   | 必填 | 说明          | 示例值                                    |
| --------- | ------ | ---- | ------------- | ------------------------------------------ |
| sessionId | string | 是   | 任务唯一标识  | d52831af-6365-4ccf-9476-4231feb7be4a       |

> Content-Type: `application/x-www-form-urlencoded`

#### 返回结构

```json
{
  "all_image": "results/d52831af-6365-4ccf-9476-4231feb7be4a/output.png",
  "paths": [
    {
      "path_json": "results/d52831af-6365-4ccf-9476-4231feb7be4a/paths_json/path_1.json",
      "image_url": "results/d52831af-6365-4ccf-9476-4231feb7be4a/paths_img/path_1.png",
      "dot_file": "results/d52831af-6365-4ccf-9476-4231feb7be4a/paths_dot/path_1.dot",
      "path": "1_start_node -> 2_... -> 3_..."
    }
    // ...更多路径
  ],
  "pictures_files": [
    "results/d52831af-6365-4ccf-9476-4231feb7be4a/paths_img/path_1.png"
    // ...
  ],
  "paths_files": [
    "results/d52831af-6365-4ccf-9476-4231feb7be4a/paths_json/path_1.json"
    // ...
  ],
  "dots_files": [
    "results/d52831af-6365-4ccf-9476-4231feb7be4a/paths_dot/path_1.dot"
    // ...
  ]
}
```

##### 字段说明

- `all_image`：主控流图图片路径（output.png）
- `paths`：每条路径的详细信息（见下表）
- `pictures_files`：所有路径图片文件路径
- `paths_files`：所有路径json文件路径
- `dots_files`：所有路径dot文件路径

###### paths 子项结构

| 字段名    | 类型   | 说明                        |
| --------- | ------ | --------------------------- |
| path_json | string | 路径json文件路径            |
| image_url | string | 路径图片文件路径            |
| dot_file  | string | 路径dot文件路径             |
| path      | string | 路径节点label顺序（->分隔） |

##### 错误返回示例

```json
{
  "error": "未找到Java文件: results/d52831af-6365-4ccf-9476-4231feb7be4a/PathAnalysis.java"
}
```

---

### 2. 打包API

#### 接口地址

```
POST /api/package-and-download
```

#### 功能

根据前端选择，打包指定 sessionId 下的 PathAnalysis.java、output.png 及其它可选文件为 zip，返回下载链接。

#### 请求参数（application/json，Pydantic模型）

```json
{
  "sessionId": "d52831af-6365-4ccf-9476-4231feb7be4a",
  "output_json": "output.json",
  "output_dot": "output.dot",
  "selected_paths": [
    { "png": "paths_img/path_1.png", "json": "paths_json/path_1.json" },
    { "png": "paths_img/path_2.png" }
  ]
}
```

##### 字段说明

| 参数名         | 类型           | 必填 | 说明                                   |
| -------------- | -------------- | ---- | -------------------------------------- |
| sessionId      | string         | 是   | 任务唯一标识                           |
| output_json    | string         | 否   | 是否打包 output.json                   |
| output_dot     | string         | 否   | 是否打包 output.dot                    |
| selected_paths | array<object>  | 否   | 多选路径文件，每项可选 png/dot/json    |

###### selected_paths 子项结构

| 字段名 | 类型   | 必填 | 说明                   |
| ------ | ------ | ---- | ---------------------- |
| png    | string | 否   | 路径图片文件名         |
| dot    | string | 否   | 路径dot文件名          |
| json   | string | 否   | 路径json文件名         |

#### 返回结构

```json
{
  "zip_url": "/results/d52831af-6365-4ccf-9476-4231feb7be4a.zip"
}
```

#### 错误返回示例

```json
{
  "error": "未找到文件: results/d52831af-6365-4ccf-9476-4231feb7be4a/output.json"
}
```

---

### 3. 其他说明

- 所有文件路径均以 `results/{sessionId}` 为根目录，前端只需传递相对路径。
- PathAnalysis.java 和 output.png 必定被自动打包，无需前端传递。
- 所有可选文件（output.json、output.dot、各路径的 .png/.dot/.json）可多选，前端按需传递。
- 返回的 zip_url 可直接用于前端下载。
