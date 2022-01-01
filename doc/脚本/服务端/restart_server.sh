#!/bin/bash

#打印出当前的进程，grep -v "grep" 去掉grep进程
pid=$(ps -ef | grep phoenix-server.jar | grep -v "grep" | awk '{print $2}')
echo "监控服务端进程PID：$pid"
#查询进程个数：wc -l 返回行数
count=$(ps -ef | grep phoenix-server.jar | grep -v "grep" | wc -l)
echo "监控服务端进程个数：$count"
#关闭进程
if (($count > 0)); then
  kill $pid
fi
#打印关掉的进程ID
echo "关闭进程：$pid"
count=$(ps -ef | grep phoenix-server.jar | grep -v "grep" | wc -l)
sec=5
sum=12
#开始一个循环
while (($sum > 0)); do
  if (($count > 0)); then
    #若进程还未关闭，则脚本sleep几秒
    sleep $sec
    count=$(ps -ef | grep phoenix-server.jar | grep -v "grep" | wc -l)
  else
    #若进程已经关闭，则跳出循环
    echo "监控服务端进程已经关闭！"
    break
  fi
  sum=$(($sum - 1))
done
#超时不能停止，强制杀掉进程
if (($count > 0)); then
  kill -9 $pid
  echo "监控服务端进程被强制关闭！"
  sleep 1
fi

#启动进程
nohup java -jar -Xms128m -Xmx512m -Ddruid.mysql.usePingMethod=false phoenix-server.jar --spring.profiles.active=prod >/dev/null 2>&1 &
pid=$(ps -ef | grep phoenix-server.jar | grep -v "grep" | awk '{print $2}')
echo "监控服务端进程已经启动，进程ID为：$pid"
