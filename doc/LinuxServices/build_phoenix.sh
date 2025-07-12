#!/usr/bin/env bash
# 出错立即退出
set -e

# 引入 logger.sh
source ./logger.sh
# 引入 utils.sh
source ./utils.sh

##################################################### 构建 phoenix #####################################################

# 定义主机目录
HOST_DATA_DIR="/data/phoenix/.env"
# 定义 Java 安装目录路径
JAVA_PATH="${HOST_DATA_DIR}/OpenJDK17"
# 定义 Maven 可执行文件路径
MAVEN_BIN="${HOST_DATA_DIR}/Maven3.8.9/bin/mvn"
# 定义 pom.xml 文件的完整路径
PHOENIX_POM="${HOST_DATA_DIR}/phoenix/phoenix-master/pom.xml"
#定义 Maven 本地仓库路径
LOCAL_REPO="${HOST_DATA_DIR}/.m2/repository"

# 调用函数构建项目
if ! build_with_maven "${JAVA_PATH}" "${MAVEN_BIN}" "${PHOENIX_POM}" "${LOCAL_REPO}"; then
  exit 1
fi

# 进入 HOST_DATA_DIR 执行后续操作
cd "${HOST_DATA_DIR}" || {
  log_error "❌ 无法切换到目录：${HOST_DATA_DIR}"
  exit 1
}
log_info "✅ 当前工作目录已切换到：$(pwd)"

# 定义可执行 JAR 的源路径和目标路径定义
EXECUTABLE_JAR_SRC_DIR="$(dirname "${PHOENIX_POM}")/target"
EXECUTABLE_JAR_DEST_DIR="../"

mv "${EXECUTABLE_JAR_SRC_DIR}/phoenix-server.jar" "${EXECUTABLE_JAR_DEST_DIR}/"
mv "${EXECUTABLE_JAR_SRC_DIR}/phoenix-ui.jar"     "${EXECUTABLE_JAR_DEST_DIR}/"
mv "${EXECUTABLE_JAR_SRC_DIR}/phoenix-agent.jar"  "${EXECUTABLE_JAR_DEST_DIR}/"

# 去掉执行权限
chmod -x "${EXECUTABLE_JAR_DEST_DIR}/phoenix-server.jar"
chmod -x "${EXECUTABLE_JAR_DEST_DIR}/phoenix-ui.jar"
chmod -x "${EXECUTABLE_JAR_DEST_DIR}/phoenix-agent.jar"

log_info "✅ 可执行 JAR 已转移到：$(realpath "${EXECUTABLE_JAR_DEST_DIR}")"