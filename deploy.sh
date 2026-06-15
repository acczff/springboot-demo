#!/bin/bash
# 进入脚本所在的目录 (确保在项目根目录下执行)
cd "$(dirname "$0")"

echo "==================================="
echo "🚀 开始自动化部署流程"
echo "==================================="

echo "1. 从 GitHub 拉取最新代码..."
git pull origin master

echo "2. 重新构建并重启 Docker 容器..."
# --build: 强制重新构建镜像，Docker 会自动利用层缓存（比如只要 pom.xml 没变，就不需要重新下载依赖）
# -d: 后台运行容器
docker compose up -d --build

echo "==================================="
echo "✅ 部署已完成！项目已在后台运行。"
echo "可以使用 'docker compose logs -f app' 查看运行日志。"
echo "==================================="
