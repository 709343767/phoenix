@echo off
curl -i -X POST http://localhost:16000/monitoring-agent/actuator/shutdown
@pause
