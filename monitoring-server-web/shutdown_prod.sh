#!/bin/bash
curl -i -X POST http://localhost:16000/monitoring-server-web/actuator/shutdown
#换行
echo ""
echo "stop succeeded!"
