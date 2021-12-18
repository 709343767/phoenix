#!/bin/bash
nohup java -jar -Xms64m -Xmx128m phoenix-agent.jar --spring.profiles.active=prod >/dev/null 2>&1 &
pid=$(ps -ef | grep phoenix-agent.jar | grep -v "grep" | awk '{print $2}')
echo "监控代理端进程已经启动，进程ID为：$pid"