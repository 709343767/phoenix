#!/usr/bin/env bash
# 出错立即退出
set -e

# =============================================
# 函数名：log_base
# 描述：内部日志输出函数，用于统一格式化输出带时间戳、级别和颜色的日志信息。
# 参数：
#   $1：日志级别（INFO / ERROR / UNKNOWN）
#   $@：合并后的日志消息内容（自动合并所有参数）
# 返回值:
#   无返回值，直接输出到 stdout 或 stderr
# 示例：
#   log_base INFO "服务启动成功"
#   log_base ERROR "连接失败"
#
# 注意事项：
#   - 支持终端彩色输出（自动检测是否为 TTY）；
#   - 自动添加时间戳；
#   - 若消息为空，则显示 "[空消息]"；
#   - 颜色支持绿色(INFO)、红色(ERROR)、黄色(UNKNOWN)；
#   - 此函数通常不直接调用，而是由 log_info / log_error 调用。
# =============================================
log_base() {
  local level="$1"
  local prefix color timestamp message
  shift

  # 获取统一时间戳
  timestamp=$(date '+%Y-%m-%d %H:%M:%S')

  # 根据级别设置前缀和颜色
  case "$level" in
    INFO)
      prefix="INFO"
      color="\033[32m" # 绿色
      ;;
    ERROR)
      prefix="ERROR"
      color="\033[31m" # 红色
      ;;
    *)
      prefix="UNKNOWN"
      color="\033[33m" # 黄色
      ;;
  esac

  # 合并所有参数为消息
  message="$*"

  # 空消息保护
  if [ -z "$message" ]; then
    message="[空消息]"
  fi

  # 彩色输出格式 (支持终端显示)
  if [ -t 1 ]; then
    printf "${color}[%s] %s: %s\033[0m\n" "$timestamp" "$prefix" "$message"
  else
    printf "[%s] %s: %s\n" "$timestamp" "$prefix" "$message"
  fi
}

# =============================================
# 函数名：log_info
# 描述：输出 INFO 级别的日志信息，支持多参数合并和管道输入。
# 参数：
#   $@：多个参数将被合并为一条日志输出，如果没有参数，则从标准输入中逐行读取并输出
# 返回值：
#   无返回值
# 示例：
#   log_info "服务正在运行..."
#   echo "来自管道的信息" | log_info
#   log_info "用户：" "$USER"
#
# 注意事项：
#   - 输出到 stdout；
#   - 支持任意数量参数；
#   - 支持管道输入（例如来自 echo 或 cat）；
#   - 实际输出调用的是 log_base 函数；
#   - 消息会自动加上时间戳和 [INFO] 标记。
# =============================================
log_info() {
  if [ $# -eq 0 ]; then
    while IFS= read -r line; do
      log_base INFO "$line"
    done
  else
    log_base INFO "$*"
  fi
}

# =============================================
# 函数名：log_error
# 描述：输出 ERROR 级别的日志信息，支持多参数合并和管道输入。
# 参数：
#   $@：多个参数将被合并为一条日志输出，如果没有参数，则从标准输入中逐行读取并输出
# 返回值：
#   无返回值
# 示例：
#   log_error "文件不存在"
#   echo "错误详情" | log_error
#   log_error "配置加载失败：" "/etc/app.conf"
#
# 注意事项：
#   - 输出到 stderr；
#   - 支持任意数量参数；
#   - 支持管道输入（例如来自 echo 或 cat）；
#   - 实际输出调用的是 log_base 函数；
#   - 消息会自动加上时间戳和 [ERROR] 标记；
#   - 使用红色字体突出显示错误信息（支持终端）。
# =============================================
log_error() {
  if [ $# -eq 0 ]; then
    while IFS= read -r line; do
      log_base ERROR "$line" >&2
    done
  else
    log_base ERROR "$*" >&2
  fi
}