from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import router
import os
from fastapi.staticfiles import StaticFiles

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

os.makedirs("uploads", exist_ok=True)
os.makedirs("results", exist_ok=True)

# 挂载静态文件目录
app.mount("/results", StaticFiles(directory="results"), name="results")

app.include_router(router) 