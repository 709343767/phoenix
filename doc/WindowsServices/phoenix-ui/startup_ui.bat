::::::::::::::::::::::::::::::::::::::::::::运行后出现窗口，窗口关闭后服务关闭::::::::::::::::::::::::::::::::::::::::::::::
@echo off
java -jar -Ddruid.mysql.usePingMethod=false phoenix-ui.jar --spring.profiles.active=prod
@pause