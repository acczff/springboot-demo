#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
FRONTEND_DIR="${FRONTEND_DIR:-"$ROOT_DIR/../springboot-web"}"
BRANCH="${DEPLOY_BRANCH:-master}"

cd "$ROOT_DIR"

echo "==================================="
echo "开始后端自动化部署"
echo "部署分支: $BRANCH"
echo "==================================="

echo "1. 拉取后端最新代码..."
git pull origin "$BRANCH"

if [ -d "$FRONTEND_DIR/.git" ]; then
  echo "2. 拉取前端最新代码..."
  git -C "$FRONTEND_DIR" pull origin "$BRANCH"
else
  echo "2. 未找到前端仓库: $FRONTEND_DIR，跳过前端拉取。"
fi

echo "3. 构建并启动 Docker Compose 服务..."
docker compose up -d --build

echo "4. 当前服务状态:"
docker compose ps

echo "==================================="
echo "部署完成"
echo "前端入口:   http://<服务器IP> 或已配置的域名"
echo "网关接口:   http://<服务器IP>:8080"
echo "后端服务:   gateway, user-service, biz-service, db, redis, rabbitmq"
echo ""
echo "查看日志:   docker compose logs -f gateway user-service biz-service"
echo "查看状态:   docker compose ps"
echo "==================================="
