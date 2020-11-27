#!/bin/bash
curl --connect-timeout 5 -m 5 -i -X POST http://localhost:12000/monitoring-agent/actuator/shutdown
#换行
echo ""
echo "stop succeeded!"
