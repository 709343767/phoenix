#!/bin/bash
curl -i -X POST http://localhost:16000/monitoring-server/actuator/shutdown
#换行
echo ""
echo "stop succeeded!"
