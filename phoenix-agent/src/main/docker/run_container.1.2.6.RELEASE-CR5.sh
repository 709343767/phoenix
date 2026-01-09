#!/usr/bin/env bash
# 出错立即退出
set -e
# 未定义变量报错
set -u

# 定义 镜像名、容器名、宿主机目录
IMAGE_NAME="phoenix/phoenix-agent:1.2.6.RELEASE-CR5"
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
docker run -itd \
  -v /dev:/dev:ro \
  -v /sys:/sys:ro \
  -v /etc/localtime:/etc/localtime:ro \
  -v /etc/os-release:/etc/os-release:ro \
  -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix" \
  -v "${HOST_DATA_DIR}/config:/app/config" \
  -p 12000:12000 \
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


# -v /dev:/dev:ro                                                 # 设备信息
# -v /proc:/proc:ro                                               # 进程与系统状态
# -v /sys:/sys:ro                                                 # 硬件设备
# -v /etc/localtime:/etc/localtime:ro                             # 时区同步
# -v /etc/os-release:/etc/os-release:ro                           # 系统版本
# -v "${HOST_DATA_DIR}/liblog4phoenix:/app/liblog4phoenix"        # 动态库
# -v "${HOST_DATA_DIR}/config:/app/config"                        # 配置
# --pid=host                                                      # 共享进程视图
# --net host                                                      # 共享网络
# --uts=host                                                      # 共享宿主机 UTS 命名空间（使容器内 hostname 与宿主机一致）
# --security-opt label=disable                                    # 关 SELinux（CentOS）
# --security-opt apparmor=unconfined                              # 禁用 AppArmor 对容器的限制
# --cap-add=SYS_RAWIO                                             # 原始 I/O 权限
# --cap-add=SYS_ADMIN                                             # 访问 /sys、/proc
# --restart unless-stopped                                        # 自启（除非手动停）