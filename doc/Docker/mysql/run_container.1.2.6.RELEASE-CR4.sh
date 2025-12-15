#!/usr/bin/env bash
# 出错立即退出
set -e
# 未定义变量报错
set -u

# 定义 镜像名、容器名、宿主机目录
IMAGE_NAME="crpi-4iaxdbbs1euymfiu.cn-shanghai.personal.cr.aliyuncs.com/pifeng_phoenix/mysql:1.2.6.RELEASE-CR4"
CONTAINER_NAME="phoenix-mysql"
DATA_PATH="/data/phoenix/mysql/data"

# 动态获取 UID/GID
echo "Detecting mysql user UID/GID from image..."
# MYSQL_UID=$(docker run --rm "${IMAGE_NAME}" id -u mysql 2>/dev/null || echo "999")
# MYSQL_GID=$(docker run --rm "${IMAGE_NAME}" id -g mysql 2>/dev/null || echo "999")
MYSQL_UID=999
MYSQL_GID=999
echo "Detected UID=${MYSQL_UID}, GID=${MYSQL_GID}"

# 创建目录（如果不存在）
mkdir -p "${DATA_PATH}"
# 设置属主
chown -R "${MYSQL_UID}:${MYSQL_GID}" "${DATA_PATH}"

# 删除已存在的容器（如果有的话）
if [ "$(docker inspect --format='{{.Id}}' "${CONTAINER_NAME}" 2>/dev/null)" ]; then
  echo "Removing existing container '${CONTAINER_NAME}'..."
  docker stop "${CONTAINER_NAME}" > /dev/null 2>&1 || true
  docker rm "${CONTAINER_NAME}" > /dev/null 2>&1
fi

# 启动容器
echo "Starting '${CONTAINER_NAME}' container..."
docker run -itd \
  -p 3307:3306 \
  -v "${DATA_PATH}:/var/lib/mysql" \
  --restart unless-stopped \
  --name "${CONTAINER_NAME}" \
  "${IMAGE_NAME}"
# 启动容器成功
echo "Container '${CONTAINER_NAME}' started successfully."
echo "User: phoenix"
echo "Pass: phoenix@2025"
echo "Remember to change your password later!"