# 基础镜像
FROM python:3.10-slim

# 设置工作目录
WORKDIR /app

# 复制依赖文件并安装
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 安装系统依赖
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    build-essential \
    git \
    graphviz \
    p7zip-full \
    bash \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# 安装 comex 和升级 typer
RUN pip install comex 
RUN pip install --upgrade typer

# 复制项目所有代码
COPY . .

# 创建上传和结果目录（容器内）
RUN mkdir -p uploads results

# 确保目录权限正确
RUN chmod -R 777 uploads results

# 暴露 FastAPI 默认端口
EXPOSE 8000

# 启动 FastAPI 服务
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
