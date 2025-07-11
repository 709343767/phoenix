#!/usr/bin/env bash

# 出错立即退出
set -e
# 未定义变量报错
set -u

# ================== 配置项 ==================

# 定义可执行 JAR 文件名
PACKAGE_NAME="phoenix-agent.jar"
# 定义程序的名称
PROGRAM_NAME="phoenix-agent"
# 定义启动程序的命令
START_CMD="java -jar ${PACKAGE_NAME} --spring.profiles.active=prod"
# 定义在停止程序时，最大重试次数
MAX_RETRY=12
# 定义每次重试之间的等待时间
SLEEP_SEC=5

# ================== 工具函数 ==================

# 获取当前运行进程的 PID
get_pid() {
  pgrep -f "${PACKAGE_NAME}" 2>/dev/null
}

# 判断程序是否正在运行
is_running() {
  [ -n "$(get_pid)" ]
}

# ================== 功能函数 ==================

# 查看状态
status() {
  echo "---------------------------------------检测状态---------------------------------------"
  if is_running; then
    local pid
    pid=$(get_pid)
    echo "${PROGRAM_NAME} 正在运行，进程ID：${pid}"
  else
    echo "${PROGRAM_NAME} 未运行！"
  fi
}

# 停止程序
stop() {
  echo "---------------------------------------停止程序---------------------------------------"
  if ! is_running; then
    echo "${PROGRAM_NAME} 未运行！"
    return 0
  fi

  local pid
  pid=$(get_pid)
  echo "${PROGRAM_NAME} 进程ID：${pid}"

  kill "${pid}" 2>/dev/null
  echo "发送关闭信号：kill ${pid}"

  local retry=${MAX_RETRY}
  while ((retry > 0)); do
    if ! is_running; then
      echo "${PROGRAM_NAME} 已经关闭！"
      return 0
    fi
    sleep ${SLEEP_SEC}
    ((retry--))
  done

  echo "等待超时，仍未关闭，尝试强制终止..."
  kill -9 "${pid}" 2>/dev/null
  echo "${PROGRAM_NAME} 被强制关闭！"
}

# 启动程序
start() {
  echo "---------------------------------------启动程序---------------------------------------"
  if is_running; then
    echo "${PROGRAM_NAME} 正在运行，请先停止程序！"
    return 1
  fi

  echo "启动命令：${START_CMD}"
  nohup "${START_CMD}" >/dev/null 2>&1 &
  local pid=$!

  echo "启动中，PID: ${pid}"

  # 给一点时间让进程启动
  sleep 3

  if ! is_running; then
    echo "${PROGRAM_NAME} 启动失败！"
    return 1
  else
    echo "${PROGRAM_NAME} 已启动，PID: $(get_pid)"
  fi
}

# 重启程序
restart() {
  stop
  start
}

# ================== 主程序入口 ==================
if [ $# -lt 1 ]; then
  echo "
用法: $0 {start|stop|restart|status}
示例:
  ./phoenix_agent.sh start     # 启动服务
  ./phoenix_agent.sh stop      # 停止服务
  ./phoenix_agent.sh restart   # 重启服务
  ./phoenix_agent.sh status    # 检查状态
"
  exit 1
fi

case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    restart
    ;;
  status)
    status
    ;;
  *)
    echo "无效参数: ${1}"
    echo "请使用: start | stop | restart | status"
    exit 1
    ;;
esac