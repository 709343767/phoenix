#!/bin/bash
nohup java -jar monitoring-ui.jar --spring.profiles.active=prod >/dev/null 2>&1 &
pid=$(ps -ef | grep monitoring-ui.jar | grep -v "grep" | awk '{print $2}')
echo "监控UI端进程已经启动，进程ID为：$pid"
