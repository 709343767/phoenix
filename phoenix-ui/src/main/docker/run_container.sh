#!/usr/bin/env bash
# 出错立即退出
set -e
# 定义容器名称和宿主机目录
CONTAINER_NAME="phoenix-ui"
HOST_DATA_DIR="/data/phoenix/phoenix-ui"
# 检查父目录是否有写权限
if [ ! -w "$(dirname "$HOST_DATA_DIR")" ]; then
  echo "Error: No write permission to $(dirname "$HOST_DATA_DIR")" >&2
  exit 1
fi
# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/liblog4phoenix" "${HOST_DATA_DIR}/config"
# 赋予读写权限
chmod -R o+rw "${HOST_DATA_DIR}"
# 删除已存在的容器（如果有的话）
if [ "$(docker inspect --format='{{.Id}}' ${CONTAINER_NAME} 2>/dev/null)" ]; then
  echo "Removing existing container '${CONTAINER_NAME}'..."
  docker stop ${CONTAINER_NAME} > /dev/null 2>&1 || true
  docker rm ${CONTAINER_NAME} > /dev/null 2>&1
fi
# 启动容器
echo "Starting '${CONTAINER_NAME}' container..."
docker run -itd \
  -v /etc/localtime:/etc/localtime:ro \
  -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix" \
  -v "${HOST_DATA_DIR}/config:/app/config" \
  -p 80:80 \
  --net host \
  --cap-add=NET_BIND_SERVICE \
  --name ${CONTAINER_NAME} \
  phoenix/phoenix-ui:1.2.6.RELEASE-CR3
# 启动容器成功
echo "Container '${CONTAINER_NAME}' started successfully."