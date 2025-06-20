#!/bin/sh
# 出错立即退出
set -e
# 宿主机目录
HOST_DATA_DIR="/data/phoenix/phoenix-server"
# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/liblog4phoenix" "${HOST_DATA_DIR}/config"
#赋予读写权限
chmod -R o+rw "${HOST_DATA_DIR}"
# 删除已存在的容器（如果有的话）
if [ "$(docker ps -a -f "name=phoenix-server" --format "{{.Status}}")" ]; then
  echo "Removing existing container 'phoenix-server'..."
  docker rm -f phoenix-server
fi
# 启动容器
echo "Starting phoenix-server container..."
docker run -itd \
  -v /etc/localtime:/etc/localtime:ro \
  -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix" \
  -v "${HOST_DATA_DIR}/config:/app/config" \
  -p 16000:16000 \
  --net host \
  --name phoenix-server \
  phoenix/phoenix-server:1.2.6.RELEASE-CR3
# 启动容器成功
echo "Container started successfully."