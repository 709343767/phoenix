#!/usr/bin/env bash
# 出错立即退出
set -e
# 未定义变量报错
set -u

# 定义 镜像名、容器名、宿主机目录
IMAGE_NAME="phoenix/phoenix-server:1.2.6.RELEASE-CR4"
CONTAINER_NAME="phoenix-server"
HOST_DATA_DIR="/data/phoenix/phoenix-server"

# 定义 UID/GID
PHOENIX_SERVER_UID=999
PHOENIX_SERVER_GID=999
echo "Detected UID=${PHOENIX_SERVER_UID}, GID=${PHOENIX_SERVER_GID}"

# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/liblog4phoenix" "${HOST_DATA_DIR}/config"
# 赋予读写权限
chown -R "${PHOENIX_SERVER_UID}:${PHOENIX_SERVER_GID}" "${HOST_DATA_DIR}"

# 删除已存在的容器（如果有的话）
if [ "$(docker inspect --format='{{.Id}}' "${CONTAINER_NAME}" 2>/dev/null)" ]; then
  echo "Removing existing container '${CONTAINER_NAME}'..."
  docker stop "${CONTAINER_NAME}" > /dev/null 2>&1 || true
  docker rm "${CONTAINER_NAME}" > /dev/null 2>&1
fi

# 启动容器
echo "Starting '${CONTAINER_NAME}' container..."
docker run -itd \
  -v /etc/localtime:/etc/localtime:ro \
  -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix" \
  -v "${HOST_DATA_DIR}/config:/app/config" \
  -p 16000:16000 \
  --net host \
  --restart unless-stopped \
  --name "${CONTAINER_NAME}" \
  "${IMAGE_NAME}"
# 启动容器成功
echo "Container '${CONTAINER_NAME}' started successfully."