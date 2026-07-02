#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
FRONTEND_DIR="${FRONTEND_DIR:-"$ROOT_DIR/../springboot-web"}"
BRANCH="${DEPLOY_BRANCH:-master}"

wait_for_port() {
  local name="$1"
  local host="$2"
  local port="$3"
  local max_attempts="${4:-60}"

  echo "等待 ${name} 端口就绪: ${host}:${port}"
  for ((i = 1; i <= max_attempts; i++)); do
    if timeout 1 bash -c "cat < /dev/null > /dev/tcp/${host}/${port}" 2>/dev/null; then
      echo "${name} 已就绪"
      return 0
    fi
    sleep 2
  done

  echo "${name} 在 ${max_attempts} 次检查后仍未就绪"
  docker compose logs --tail=80 "$name" || true
  return 1
}

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
  echo "2. 未找到前端仓库 $FRONTEND_DIR，跳过前端拉取。"
fi

echo "3. 构建并启动基础服务..."
docker compose up -d --build --remove-orphans db redis rabbitmq

echo "4. 构建并启动业务服务..."
docker compose up -d --build user-service biz-service

wait_for_port "user-service" "127.0.0.1" "8081" 90
wait_for_port "biz-service" "127.0.0.1" "8082" 90

echo "5. 启动网关和前端..."
docker compose up -d --build gateway web

wait_for_port "gateway" "127.0.0.1" "8080" 60

echo "6. 当前服务状态:"
docker compose ps

echo "==================================="
echo "部署完成"
echo "前端入口:   http://<服务器IP>"
echo "网关接口:   http://<服务器IP>:8080"
echo "后端服务:   gateway, user-service, biz-service, db, redis, rabbitmq"
echo ""
echo "查看日志:   docker compose logs -f gateway user-service biz-service"
echo "查看状态:   docker compose ps"
echo "==================================="
