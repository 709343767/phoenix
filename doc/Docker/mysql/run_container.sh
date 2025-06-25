#!/usr/bin/env bash
# 出错立即退出
set -e
# 宿主机目录
HOST_DATA_DIR="/data/phoenix/mysql"
# 检查父目录是否有写权限
if [ ! -w "$(dirname "$HOST_DATA_DIR")" ]; then
  echo "Error: No write permission to $(dirname "$HOST_DATA_DIR")" >&2
  exit 1
fi
# 创建目录（如果不存在）
mkdir -p "${HOST_DATA_DIR}/data"
#赋予读写权限
chmod -R o+rw "${HOST_DATA_DIR}"
# 删除已存在的容器（如果有的话）
if [ "$(docker inspect --format='{{.Id}}' phoenix-mysql 2>/dev/null)" ]; then
  echo "Removing existing container 'phoenix-mysql'..."
  docker stop phoenix-mysql > /dev/null 2>&1 || true
  docker rm phoenix-mysql > /dev/null 2>&1
fi
# 启动容器
echo "Starting phoenix-mysql container..."
docker run -itd \
  -p 3306:3306 \
  --restart unless-stopped \
  -v "${HOST_DATA_DIR}/data:/var/lib/mysql" \
  --name phoenix-mysql \
  crpi-4iaxdbbs1euymfiu.cn-shanghai.personal.cr.aliyuncs.com/pifeng_phoenix/mysql:1.2.6.RELEASE-CR3
# 启动容器成功
echo "Container 'phoenix-mysql' started successfully."