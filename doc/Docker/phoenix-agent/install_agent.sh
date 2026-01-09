#!/usr/bin/env bash
# 出错立即退出
set -e

echo "Starting to execute remote script..."

# 定义一个远程脚本的 URL
RUN_CONTAINER_PHOENIX_AGENT_URL="https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/phoenix-agent/run_container.1.2.6.RELEASE-CR5.sh"

# 执行脚本
echo "Executing Phoenix Agent container script..."
bash -c "$(curl -fsSL "${RUN_CONTAINER_PHOENIX_AGENT_URL}")"

echo "Remote scripts have been executed successfully..."