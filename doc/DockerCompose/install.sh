#!/usr/bin/env bash
# 出错立即退出
set -e

# 定义 docker-compose.yml 文件的URL
COMPOSE_FILE_URL="https://gitee.com/monitoring-platform/phoenix/raw/master/doc/DockerCompose/docker-compose.1.2.6.RELEASE-CR3.yml"

# 定义本地存储路径
LOCAL_COMPOSE_FILE="./docker-compose.yml"

# 下载 docker-compose.yml 文件
echo "Downloading docker-compose.yml..."
curl -o $LOCAL_COMPOSE_FILE -L $COMPOSE_FILE_URL
if [ $? -ne 0 ]; then
  echo "Failed to download docker-compose.yml"
  exit 1
fi

# 检查是否已安装 docker-compose
if ! command -v docker-compose &> /dev/null
then
    echo "docker-compose could not be found, please install it."
    exit 1
fi

# 定义宿主机目录
HOST_DATA_DIR="/data/phoenix"
# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/mysql/data"
mkdir -p "${HOST_DATA_DIR}/phoenix-server/liblog4phoenix" "${HOST_DATA_DIR}/phoenix-server/config"
mkdir -p "${HOST_DATA_DIR}/phoenix-ui/liblog4phoenix" "${HOST_DATA_DIR}/phoenix-ui/config"
# 赋予读写权限
chmod -R o+rw "${HOST_DATA_DIR}" || echo "Warning: Failed to set permissions on ${HOST_DATA_DIR}"

# 停止并删除现有容器（如果有的话）
echo "Stopping and removing existing containers..."
docker-compose -f $LOCAL_COMPOSE_FILE down

# 拉取最新的镜像
echo "Pulling the latest images..."
docker-compose -f $LOCAL_COMPOSE_FILE pull

# 使用 docker-compose 启动服务
echo "Starting services with docker-compose..."
docker-compose -f $LOCAL_COMPOSE_FILE up -d

# 检查 docker-compose 命令是否成功
if [ $? -eq 0 ]; then
  echo "Services started successfully."
else
  echo "Failed to start services."
  exit 1
fi