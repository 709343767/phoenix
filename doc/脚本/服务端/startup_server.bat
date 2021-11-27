::::::::::::::::::::::::::::::::::::::::::::运行后出现窗口，窗口关闭后服务关闭::::::::::::::::::::::::::::::::::::::::::::::
@echo off
java -jar -Xms128m -Xmx512m monitoring-server.jar --spring.profiles.active=prod
@pause