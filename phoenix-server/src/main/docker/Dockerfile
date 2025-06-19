# 使用 JDK 17 基础镜像（推荐使用 Adoptium 官方镜像）
FROM eclipse-temurin:17-jdk-focal
# 声明目录作为容器的挂载点
VOLUME ["/liblog4phoenix", "/config"]
# 维护者信息
LABEL maintainer="皮锋 <709343767@qq.com>"
# 复制本地 jar 包到容器中
COPY phoenix-server.jar phoenix-server.jar
# 复制本地 许可证文件 到容器中
COPY license.txt license.txt
# 暴露应用端口
EXPOSE 16000
# 容器启动命令
ENTRYPOINT ["java", \
"-Djava.security.egd=file:/dev/./urandom", \
"-Duser.timezone=GMT+08", \
"-Ddruid.mysql.usePingMethod=false", \
"-Dspring.profiles.active=prod", \
"-jar", \
"phoenix-server.jar"]