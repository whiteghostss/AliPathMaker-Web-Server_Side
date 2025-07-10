# AiPathMaker-Web-Server_Side

# AiPathMaker-Web-Server_Side

##结构
```
backend/
├── __init__.py
├── main.py              # FastAPI初始化、CORS、路由注册
├── routers.py           # 统一注册所有功能模块的路由
├── upload.py            # 上传与解压相关API
├── source.py            # 获取源码相关API
├── path.py              # comex路径与图片生成相关API
├── package.py           # 打包下载相关API
├── utils.py             # 工具函数
├── models.py            # 数据模型
├── requirements.txt
```

## 运行
```
pip install -r requirements.txt
uvicorn main:app --reload  # 用 Uvicorn 作为 ASGI 服务器
```
