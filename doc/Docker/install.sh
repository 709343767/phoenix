#!/usr/bin/env bash
# 出错立即退出
set -e

echo "Starting to execute remote scripts..."

# 定义三个远程脚本的 URL
RUN_CONTAINER_PHOENIX_MYSQL_URL="https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/mysql/run_container.sh"
RUN_CONTAINER_PHOENIX_SERVER_URL="https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/phoenix-server/run_container.sh"
RUN_CONTAINER_PHOENIX_UI_URL="https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/phoenix-ui/run_container.sh"

# 执行脚本
echo "Executing MySQL container script..."
bash -c "$(curl -fsSL "$RUN_CONTAINER_PHOENIX_MYSQL_URL")"

echo "Executing Phoenix Server container script..."
bash -c "$(curl -fsSL "$RUN_CONTAINER_PHOENIX_SERVER_URL")"

echo "Executing Phoenix UI container script..."
bash -c "$(curl -fsSL "$RUN_CONTAINER_PHOENIX_UI_URL")"

echo "All remote scripts have been executed successfully."