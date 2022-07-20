#!/usr/bin/env bash

#包名
packageName="phoenix-agent.jar"
#程序名
programName="phoenix-agent"

#检测状态
function status() {
  echo "---------------------------------------检测状态---------------------------------------"
  pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
  if [ ${pid} ]; then
    echo "${programName}正在运行，进程ID：${pid}"
  else
    echo "${programName}未运行！"
  fi
}

#停止程序
function stop() {
  echo "---------------------------------------停止程序---------------------------------------"
  #打印出当前的进程，grep -v grep 去掉grep进程
  pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
  #查询进程个数：wc -l 返回行数
  count=$(ps -ef | grep -n ${packageName} | grep -v grep | wc -l)
  echo "${programName}进程ID：$pid，进程个数：$count"
  #关闭进程
  if (($count > 0)); then
    kill $pid
  else
    echo "${programName}未运行！"
    exit 0
  fi
  #打印关掉的进程ID
  echo "关闭进程：$pid"
  count=$(ps -ef | grep -n ${packageName} | grep -v grep | wc -l)
  sec=5
  sum=12
  #开始一个循环
  while (($sum > 0)); do
    if (($count > 0)); then
      #若进程还未关闭，则脚本sleep几秒
      sleep $sec
      count=$(ps -ef | grep -n ${packageName} | grep -v grep | wc -l)
    else
      #若进程已经关闭，则跳出循环
      echo "${programName}已经关闭！"
      break
    fi
    sum=$(($sum - 1))
  done
  #超时不能停止，强制杀掉进程
  if (($count > 0)); then
    kill -9 $pid
    echo "${programName}被强制关闭！"
    sleep 1
  fi
}

#启动程序
function start() {
  echo "---------------------------------------启动程序---------------------------------------"
  pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
  if [ ${pid} ]; then
    echo "${programName}正在运行，请先停止程序！"
  else
    #启动进程
    nohup java -jar ${packageName} --spring.profiles.active=prod >/dev/null 2>&1 &
    pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
    if [ ${pid} ]; then
      echo "${programName}已经启动，进程ID为：$pid"
    else
      #等待5秒
      sleep 5
      pid=$(ps -ef | grep -n ${packageName} | grep -v grep | awk '{print $2}')
      if [ !${pid} ]; then
        echo "${programName}启动失败！"
      fi
    fi
  fi
}

#启动时带参数，根据参数执行
if [ ${#} -ge 1 ]; then
  case ${1} in
  "start")
    start
    ;;
  "restart")
    stop
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  *)
    echo "${1}无任何操作"
    ;;
  esac
else
  echo "
    start：启动
    stop：停止
    restart：重启
    status：检查状态
    示例命令如：./phoenix_agent start
    "
fi
