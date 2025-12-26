#!/usr/bin/env bash
# 出错立即退出
set -e

# ==============================
# 配置区（可根据需要调整）
# ==============================
COMPOSE_FILE_URL="https://gitee.com/monitoring-platform/phoenix/raw/master/doc/DockerCompose/docker-compose.1.2.6.RELEASE-CR5.yml"
LOCAL_COMPOSE_FILE="./docker-compose.yml"
HOST_DATA_DIR="/data/phoenix"

# 定义 UID/GID
CONTAINER_UID=999
CONTAINER_GID=999

# ==============================
# 工具函数
# ==============================

log() {
  echo "[$(date +'%Y-%m-%d %H:%M:%S')] $*"
}

error_exit() {
  log "ERROR: $*" >&2
  exit 1
}

# ==============================
# 主流程开始
# ==============================

log "Starting Phoenix platform deployment..."

# 1. 确定使用哪个 compose 命令
if command -v docker-compose &> /dev/null; then
  COMPOSE_CMD="docker-compose"
elif docker compose version &> /dev/null; then
  COMPOSE_CMD="docker compose"
else
  error_exit "Neither 'docker-compose' nor 'docker compose' is installed."
fi

log "Using compose command: ${COMPOSE_CMD}"

# 2. 下载 docker-compose.yml
log "Downloading docker-compose.yml from: ${COMPOSE_FILE_URL}"
if ! curl --retry 3 --retry-delay 2 -fsSL -o "${LOCAL_COMPOSE_FILE}" "${COMPOSE_FILE_URL}"; then
  error_exit "Failed to download docker-compose.yml after 3 retries"
fi

# 验证文件是否有效（非空且不是 HTML 错误页）
if [ ! -s "${LOCAL_COMPOSE_FILE}" ]; then
  error_exit "Downloaded file is empty."
fi

if head -n 5 "${LOCAL_COMPOSE_FILE}" | grep -qi "<html"; then
  error_exit "Downloaded file appears to be an HTML error page (e.g., 404)."
fi

log "docker-compose.yml downloaded successfully."

# 3. 创建宿主机数据目录
log "Creating host data directories..."
mkdir -p "${HOST_DATA_DIR}/mysql/data"
mkdir -p "${HOST_DATA_DIR}/phoenix-server/liblog4phoenix" "${HOST_DATA_DIR}/phoenix-server/config"
mkdir -p "${HOST_DATA_DIR}/phoenix-ui/liblog4phoenix" "${HOST_DATA_DIR}/phoenix-ui/config"

# 4. 设置权限
log "Setting ownership of entire data directory to ${CONTAINER_UID}:${CONTAINER_GID}..."
if chown -R "${CONTAINER_UID}:${CONTAINER_GID}" "${HOST_DATA_DIR}"; then
  log "Ownership set successfully."
else
  log "Warning: Failed to change ownership of ${HOST_DATA_DIR}."
  log "This may cause permission issues if containers run as UID ${CONTAINER_UID}."
  log "Consider running this script with 'sudo' if needed."
fi

# 5. 停止旧服务
log "Stopping and removing existing containers (if any)..."
if [ -f "${LOCAL_COMPOSE_FILE}" ]; then
  ${COMPOSE_CMD} -f "${LOCAL_COMPOSE_FILE}" down --remove-orphans || true
fi

# 6. 拉取最新镜像
log "Pulling latest images..."
if ! ${COMPOSE_CMD} -f "${LOCAL_COMPOSE_FILE}" pull; then
  error_exit "Failed to pull one or more images. Please check network connectivity, image tags, and registry access."
fi

# 7. 启动服务
log "Starting services in detached mode..."
if ! ${COMPOSE_CMD} -f "${LOCAL_COMPOSE_FILE}" up -d; then
  error_exit "Failed to start services."
fi

log "Phoenix platform started successfully!"
log "Data directory: ${HOST_DATA_DIR}"
log "Compose file: ${LOCAL_COMPOSE_FILE}"