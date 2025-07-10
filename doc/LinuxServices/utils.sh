#!/usr/bin/env bash

# Script Name: utils.sh
# Description: 工具函数库
# Version: 1.0.0
# Author: 皮锋

# 出错立即退出
set -e

# 引入 logger.sh
source ./logger.sh

# =============================================
# 函数名: download
# 描述: 使用 wget 下载文件，并支持 User-Agent 设置和失败重试机制
# 参数:
#   $1: 保存的本地文件路径（含文件名）
#   $2: 要下载的远程 URL 地址
#   $3: 是否使用 User-Agent（"true"/"false"），可选，默认为 "true"
# 返回值:
#   0: 下载成功
#   1: 所有尝试均失败
# 示例:
#   download "nginx.tar.gz" "https://example.com/nginx.tar.gz"
#   download "file.zip" "https://example.com/file.zip" "false"
# =============================================
download() {
  local tar_file="$1"
  local url="$2"
  # 第三个参数可选，默认 true
  local use_ua="${3:-true}"
  # 重试3次
  local retries=3
  for i in $(seq 1 $retries); do
    log_info  "=> 尝试下载 ${tar_file}（第${i}次尝试）..."
    if [[ "${use_ua}" == "true" ]]; then
      if wget --user-agent="Mozilla/5.0" -O "${tar_file}" "${url}" --progress=bar:force:noscroll; then
        log_info "✅ ${tar_file} 下载成功..."
        return 0
      fi
    else
      if wget -O "${tar_file}" "${url}" --progress=bar:force:noscroll; then
        log_info "✅ ${tar_file} 下载成功..."
        return 0
      fi
    fi
    sleep 2
  done
  log_error "❌ ${tar_file} 下载失败，请检查网络或 URL 是否正确..."
  return 1
}

# =============================================
# 函数名: decompress_tar_gz_file
# 描述: 解压 .tar.gz 格式的压缩包到指定目录，并自动去除一级目录结构
# 参数:
#   $1: 要解压的 .tar.gz 文件路径（含文件名）
#   $2: 目标解压目录路径
# 返回值:
#   0: 解压成功
#   1: 解压失败（如文件损坏、权限不足等）
# 示例:
#   decompress_tar_gz_file "nginx.tar.gz" "/opt/nginx"
#   decompress_tar_gz_file "/tmp/app.tar.gz" "./app"
# =============================================
decompress_tar_gz_file() {
  local tar_file="$1"
  local target_dir="$2"
  log_info "=> 正在解压 ${tar_file} 到 ${target_dir} 目录..."
  if tar -zxf "${tar_file}" -C "${target_dir}" --overwrite --strip-components=1; then
    log_info "✅ 成功解压 ${tar_file} 到 ${target_dir} 目录..."
    return 0
  fi
  log_error "❌ 解压失败，请检查文件完整性或权限..."
  return 1
}

# =============================================
# 函数名: decompress_zip_file
# 描述: 解压 .zip 格式的压缩包到指定目录
# 参数:
#   $1: 要解压的 .zip 文件路径（含文件名）
#   $2: 目标解压目录路径
# 返回值:
#   0: 解压成功
#   1: 解压失败（如文件损坏、权限不足等）
# 示例:
#   decompress_zip_file "app.zip" "./app"
#   decompress_zip_file "/tmp/data.zip" "/var/data"
# =============================================
decompress_zip_file() {
  local zip_file="$1"
  local target_dir="$2"
  log_info "=> 正在解压 ${zip_file} 到 ${target_dir} 目录..."
  if unzip -q -o "${zip_file}" -d "${target_dir}"; then
    log_info "✅ 成功解压 ${zip_file} 到 ${target_dir} 目录..."
    return 0
  else
    log_error "❌ 解压失败，请检查文件完整性或权限..."
    return 1
  fi
}

# =============================================
# 函数名: decompress_file
# 描述: 根据文件扩展名自动选择解压方式（支持 .tar.gz, .tgz 和 .zip）
# 参数:
#   $1: 要解压的压缩文件路径（含文件名）
#   $2: 目标解压目录路径
# 返回值:
#   0: 解压成功
#   1: 不支持的压缩格式或解压失败
# 示例:
#   decompress_file "nginx.tar.gz" "/opt/nginx"
#   decompress_file "app.zip" "./app"
# 注意:
#   - 需要提前定义 decompress_tar_gz_file 和 decompress_zip_file 函数
#   - 确保目标目录存在或在函数中创建它
# =============================================
decompress_file() {
  local file="$1"
  local target_dir="$2"
  case "${file}" in
    *.tar.gz|*.tgz)
      decompress_tar_gz_file "${file}" "${target_dir}"
      ;;
    *.zip)
      decompress_zip_file "${file}" "${target_dir}"
      ;;
    *)
      log_error "❌ 不支持的压缩格式: ${file}"
      return 1
      ;;
  esac
}

# =============================================
# 函数名: build_with_maven
# 描述: 使用指定的 Java 和 Maven 二进制文件，在指定项目路径下执行 Maven 构建（clean package），支持自定义本地仓库。
# 参数:
#   $1: Java 安装目录路径（即 JAVA_HOME），例如：/data/phoenix/.env/OpenJDK17
#   $2: Maven 可执行文件路径（mvn），例如：/data/phoenix/.env/Maven3.8.9/bin/mvn
#   $3: pom.xml 文件的完整路径，例如：/data/project/myapp/pom.xml
#   $4: 可选参数，指定 Maven 本地仓库路径，默认为项目目录下的 .m2/repository
# 返回值:
#   0: 构建成功
#   1: 构建失败或参数校验不通过
# 示例:
#   build_with_maven "/data/phoenix/.env/OpenJDK17" \
#                    "/data/phoenix/.env/Maven3.8.9/bin/mvn" \
#                    "/data/project/myapp/pom.xml" \
#                    "/data/phoenix/.env/m2_repository"
#
# 注意事项:
#   - 需要提前安装好 Java 和 Maven，并确保可执行权限；
#   - 若未提供 local_repo，则会在项目目录下自动创建 .m2/repository；
#   - 自动设置临时环境变量 JAVA_HOME 和 PATH；
#   - 默认跳过测试阶段（-Dmaven.test.skip=true）；
#   - 构建结果（如 JAR 包）会输出到项目目录下的 target/ 子目录中。
# =============================================
build_with_maven() {
  local java_path="$1"
  local maven_bin="$2"
  local project_pom="$3"
  local local_repo="$4"

  # 参数检查
  if [ ! -x "${maven_bin}" ]; then
    log_error "❌ Maven 可执行文件不存在或不可执行: ${maven_bin}"
    return 1
  fi

  if [ ! -f "${project_pom}" ]; then
    log_error "❌ pom.xml 文件不存在: ${project_pom}"
    return 1
  fi

  if [ ! -d "$(dirname "${project_pom}")" ]; then
    log_error "❌ 项目目录不存在: $(dirname "${project_pom}")"
    return 1
  fi

  if [ -n "${local_repo}" ]; then
    mkdir -p "${local_repo}"
  else
    local_repo="$(dirname "${project_pom}")/.m2/repository"
    mkdir -p "${local_repo}"
  fi

  # 进入项目目录再执行构建更可靠
  local project_dir
  project_dir="$(dirname "${project_pom}")"
  cd "${project_dir}" || return 1
  log_info "=> 当前工作目录已切换到：$(pwd)"

  # 设置临时环境变量
  export JAVA_HOME="${java_path}"
  export PATH="${JAVA_HOME}/bin:${PATH}"
  log_info "=> 临时环境变量设置："
  log_info "    JAVA_HOME=${JAVA_HOME}"
  log_info "    PATH=${PATH}"
  log_info "    Java 版本：$(java -version 2>&1 | head -1)"

  log_info "=> 开始使用 Maven 构建项目..."
  log_info "   Java 路径: ${java_path}"
  log_info "   Maven 路径: ${maven_bin}"
  log_info "   项目 pom.xml: ${project_pom}"
  log_info "   本地仓库路径: ${local_repo}"

  # 执行构建
  if "${maven_bin}" \
    -Dmaven.repo.local="${local_repo}" \
    -f "${project_pom}" \
    -Dmaven.test.skip=true clean package; then
    log_info "✅ Maven 构建成功！JAR 文件位于：$(dirname "${project_pom}")/target/"
    return 0
  else
    log_error "❌ Maven 构建失败，请检查网络连接、依赖配置或 pom.xml 内容..."
    return 1
  fi
}