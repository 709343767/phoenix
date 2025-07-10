#!/usr/bin/env bash
# 出错立即退出
set -e

# 引入 logger.sh
source ./logger.sh
# 引入 utils.sh
source ./utils.sh

# 定义主机目录
HOST_DATA_DIR="/data/phoenix/.env"

# 定义 phoenix 压缩包名字
PHOENIX_ZIP_FILE="master.zip"
# 定义 phoenix 解压到的目标目录
PHOENIX_TARGET_DIR="${HOST_DATA_DIR}/phoenix"
# 定义 phoenix 下载路径
PHOENIX_URL="https://gitee.com/monitoring-platform/phoenix/repository/archive/${PHOENIX_ZIP_FILE}"

# 清空目录
if [[ -d "${PHOENIX_TARGET_DIR}" ]]; then
  log_info "=> 正在清空目录：${PHOENIX_TARGET_DIR}"
  shopt -s dotglob
  rm -rf "${PHOENIX_TARGET_DIR:?}"/* &>/dev/null
  shopt -u dotglob
  log_info "✅ 清空目录：${PHOENIX_TARGET_DIR}"
fi

# 创建目录（如果不存在）
log_info "=> 正在创建目录：${PHOENIX_TARGET_DIR}"
mkdir -p "${PHOENIX_TARGET_DIR}"
# 赋予权限
chmod -R 755 "${PHOENIX_TARGET_DIR}"
log_info "✅ 创建目录：${PHOENIX_TARGET_DIR}，并赋予 读、写、执行 权限..."

# 进入 HOST_DATA_DIR 执行后续操作
cd "${HOST_DATA_DIR}" || {
  log_error "❌ 无法切换到目录：${HOST_DATA_DIR}"
  exit 1
}
log_info "✅ 当前工作目录已切换到：$(pwd)"

################################################## 下载 解压 phoenix ##################################################

# 下载 phoenix
if ! download "${PHOENIX_ZIP_FILE}" "${PHOENIX_URL}" false; then
  exit 1
fi

# 解压 phoenix
if ! decompress_file "${PHOENIX_ZIP_FILE}" "${PHOENIX_TARGET_DIR}"; then
  exit 1
fi
