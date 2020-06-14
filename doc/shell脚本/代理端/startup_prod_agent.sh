#!/bin/bash
nohup java -jar monitoring-agent.jar --spring.profiles.active=prod >/dev/null 2>&1 &
# shellcheck disable=SC2009
pid=$(ps -ef | grep monitoring-agent.jar | grep -v "grep" | awk '{print $2}')
echo "start succeeded,pid is $pid !"
