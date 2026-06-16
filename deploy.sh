#!/bin/bash
# 进入脚本所在的目录 (确保在项目根目录下执行)
cd "$(dirname "$0")"

echo "==================================="
echo "🚀 开始自动化部署流程（前后端）"
echo "==================================="

echo "1. 拉取后端最新代码..."
git pull origin master || echo "⚠️  后端 git pull 失败，将使用本地已有代码构建"

echo "2. 拉取前端最新代码..."
cd ../springboot-web
git pull origin master || echo "⚠️  前端 git pull 失败，将使用本地已有代码构建"
cd ../springboot-demo

echo "3. 重新构建并重启所有容器..."
# --build: 强制重新构建镜像（Docker 层缓存会自动跳过没变的部分）
# -d: 后台运行
docker compose up -d --build

echo "==================================="
echo "✅ 部署已完成！所有服务已在后台运行。"
echo ""
echo "  前端入口:  http://服务器IP (端口80)"
echo "  后端API:   通过 Nginx 代理 /api/"
echo ""
echo "  查看日志:  docker compose logs -f"
echo "  查看状态:  docker compose ps"
echo "==================================="
