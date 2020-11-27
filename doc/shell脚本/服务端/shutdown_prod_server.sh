#!/bin/bash
curl --connect-timeout 5 -m 5 -i -X POST http://localhost:16000/monitoring-server/actuator/shutdown
#换行
echo ""
echo "stop succeeded!"
