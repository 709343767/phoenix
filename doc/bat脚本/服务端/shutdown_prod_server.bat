@echo off
curl -i -X POST http://localhost:16000/monitoring-server/actuator/shutdown
@pause
