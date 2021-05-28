#!/bin/bash
nohup java -jar monitoring-server.jar --spring.profiles.active=prod >/dev/null 2>&1 &
pid=$(ps -ef | grep monitoring-server.jar | grep -v "grep" | awk '{print $2}')
echo "监控服务端进程已经启动，进程ID为：$pid"