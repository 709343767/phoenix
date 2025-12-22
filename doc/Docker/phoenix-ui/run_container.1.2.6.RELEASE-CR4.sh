#!/usr/bin/env bash
# 出错立即退出
set -e
# 未定义变量报错
set -u

# 定义 镜像名、容器名、宿主机目录
IMAGE_NAME="crpi-4iaxdbbs1euymfiu.cn-shanghai.personal.cr.aliyuncs.com/pifeng_phoenix/phoenix-ui:1.2.6.RELEASE-CR4"
CONTAINER_NAME="phoenix-ui"
HOST_DATA_DIR="/data/phoenix/phoenix-ui"

# 定义 UID/GID
PHOENIX_UI_UID=999
PHOENIX_UI_GID=999
echo "UID=${PHOENIX_UI_UID}, GID=${PHOENIX_UI_GID}"

# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/liblog4phoenix" "${HOST_DATA_DIR}/config"
# 赋予读写权限
chown -R "${PHOENIX_UI_UID}:${PHOENIX_UI_GID}" "${HOST_DATA_DIR}"

# 删除已存在的容器（如果有的话）
if [ "$(docker inspect --format='{{.Id}}' "${CONTAINER_NAME}" 2>/dev/null)" ]; then
  echo "Removing existing container '${CONTAINER_NAME}'..."
  docker stop "${CONTAINER_NAME}" > /dev/null 2>&1 || true
  docker rm "${CONTAINER_NAME}" > /dev/null 2>&1
fi

# 启动容器
echo "Starting '${CONTAINER_NAME}' container..."
docker run -d \
  -v /etc/localtime:/etc/localtime:ro \
  -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix" \
  -v "${HOST_DATA_DIR}/config:/app/config" \
  --net host \
  --restart unless-stopped \
  --cap-add=NET_BIND_SERVICE \
  --name "${CONTAINER_NAME}" \
  "${IMAGE_NAME}"
# 启动容器成功
echo "Container '${CONTAINER_NAME}' started successfully."