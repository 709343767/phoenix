::::::::::::::::::::::::::::::::::::::::::::运行后出现窗口，窗口关闭后服务关闭::::::::::::::::::::::::::::::::::::::::::::::
@echo off
java -jar -Xms64m -Xmx128m monitoring-agent.jar --spring.profiles.active=prod
@pause