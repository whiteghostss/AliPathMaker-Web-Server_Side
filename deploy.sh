#!/bin/bash
# 一键构建镜像并启动容器

# 构建镜像
docker build -t aipathmaker-backend .

# 若已存在同名容器则先删除
docker rm -f aipathmaker-backend 2>/dev/null

# 启动容器
docker run -d --rm -p 8000:8000 --name aipathmaker-backend aipathmaker-backend
