#!/bin/bash

#关闭进程
./shutdown_prod_server.sh

#打印出当前的进程，grep -v "grep" 去掉grep进程
# shellcheck disable=SC2009
monitoringServerPid=$(ps -ef | grep monitoring-server.jar | grep -v "grep" | awk '{print $2}')
echo "监控服务端进程pid：$monitoringServerPid"

#查询进程个数：wc -l 返回行数
# shellcheck disable=SC2126
# shellcheck disable=SC2009
count=$(ps -ef | grep monitoring-server.jar | grep -v "grep" | wc -l)
echo "监控服务端进程个数：$count"

sec=5
sum=12
#开始一个循环
while (($sum > 0)); do
  if (($count > 0)); then
    #若进程还未关闭，则脚本sleep几秒
    sleep $sec
    # shellcheck disable=SC2009
    # shellcheck disable=SC2126
    count=$(ps -ef | grep monitoring-server.jar | grep -v "grep" | wc -l)
  else
    #若进程已经关闭，则跳出循环
    echo "monitoring-server.jar进程已经关闭！"
    break
  fi
  sum=$(($sum - 1))
  echo "monitoring-server.jar进程未关闭，等待剩余$sum次！"
done
#超时不能停止，强制杀掉进程
if (($count > 0)); then
  kill -9 "$monitoringServerPid"
  echo "monitoring-server.jar进程已经强制关闭！"
  sleep 1
fi

#启动进程
./startup_prod_server.sh
echo "monitoring-server.jar进程已经启动！"
