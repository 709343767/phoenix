@echo off
curl -i -X POST http://localhost:12000/monitoring-agent/actuator/shutdown
@pause
