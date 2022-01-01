#!/bin/bash
nohup java -jar -Xms128m -Xmx512m -Ddruid.mysql.usePingMethod=false phoenix-server.jar --spring.profiles.active=prod >/dev/null 2>&1 &
pid=$(ps -ef | grep phoenix-server.jar | grep -v "grep" | awk '{print $2}')
echo "监控服务端进程已经启动，进程ID为：$pid"
