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
