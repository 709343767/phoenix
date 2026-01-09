#!/usr/bin/env bash
# 出错立即退出
set -e
# 未定义变量报错
set -u

# 定义 镜像名、容器名、宿主机目录
IMAGE_NAME="crpi-4iaxdbbs1euymfiu.cn-shanghai.personal.cr.aliyuncs.com/pifeng_phoenix/phoenix-agent:1.2.6.RELEASE-CR5"
CONTAINER_NAME="phoenix-agent"
HOST_DATA_DIR="/data/phoenix/phoenix-agent"

# 定义 UID/GID
PHOENIX_AGENT_UID=999
PHOENIX_AGENT_GID=999
echo "UID=${PHOENIX_AGENT_UID}, GID=${PHOENIX_AGENT_GID}"

# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/liblog4phoenix" "${HOST_DATA_DIR}/config"
# 赋予读写权限
chown -R "${PHOENIX_AGENT_UID}:${PHOENIX_AGENT_GID}" "${HOST_DATA_DIR}"

# 删除已存在的容器（如果有的话）
if [ "$(docker inspect --format='{{.Id}}' "${CONTAINER_NAME}" 2>/dev/null)" ]; then
  echo "Removing existing container '${CONTAINER_NAME}'..."
  docker stop "${CONTAINER_NAME}" > /dev/null 2>&1 || true
  docker rm "${CONTAINER_NAME}" > /dev/null 2>&1
fi

# 启动容器
echo "Starting '${CONTAINER_NAME}' container..."
docker run -d \
  -v /dev:/dev:ro \
  -v /sys:/sys:ro \
  -v /etc/localtime:/etc/localtime:ro \
  -v /etc/os-release:/etc/os-release:ro \
  -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix" \
  -v "${HOST_DATA_DIR}/config:/app/config" \
  --pid=host \
  --net host \
  --uts=host \
  --security-opt label=disable \
  --security-opt apparmor=unconfined \
  --cap-add=SYS_RAWIO \
  --cap-add=SYS_ADMIN \
  --restart unless-stopped \
  --name "${CONTAINER_NAME}" \
  "${IMAGE_NAME}"
# 启动容器成功
echo "Container '${CONTAINER_NAME}' started successfully."