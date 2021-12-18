#!/bin/bash
nohup java -jar -Xms128m -Xmx128m phoenix-ui.jar --spring.profiles.active=prod >/dev/null 2>&1 &
pid=$(ps -ef | grep phoenix-ui.jar | grep -v "grep" | awk '{print $2}')
echo "监控UI端进程已经启动，进程ID为：$pid"
