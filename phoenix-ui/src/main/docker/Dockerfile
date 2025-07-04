# 使用 JDK 17 基础镜像（推荐使用 Adoptium 官方镜像）
FROM eclipse-temurin:17-jdk-focal
# 设置编码环境（设置容器内的系统编码为 UTF-8，避免中文乱码或字符集问题）
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
# 设置系统时区为中国上海
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo Asia/Shanghai > /etc/timezone
# 安装 curl 及 authbind 工具
RUN apt-get update && \
    apt-get install -y curl authbind && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
# 创建用户和组（用户名为 phoenix，组名为 phoenix-group）
RUN groupadd -r phoenix-group && \
    useradd -r -g phoenix-group -d /app -s /bin/bash phoenix
# 创建配置目录和文件（允许非root用户绑定 80 端口）
RUN mkdir -p /etc/authbind/byport && \
    touch /etc/authbind/byport/80 && \
    chmod 755 /etc/authbind/byport/80 && \
    chown phoenix:phoenix-group /etc/authbind/byport/80
# 设置容器工作目录（COPY, ADD, RUN 等默认都在 WORKDIR 下执行）
WORKDIR /app
# 创建挂载目录，设置目录权限
RUN mkdir -p /app/liblog4phoenix /app/config && \
    chown -R phoenix:phoenix-group /app
# 维护者信息
LABEL maintainer="皮锋 <709343767@qq.com>" \
      title="Phoenix UI Application"
# 复制本地 jar 包到容器中
COPY --chown=phoenix:phoenix-group phoenix-ui.jar phoenix-ui.jar
# 复制本地 许可证文件 到容器中
COPY --chown=phoenix:phoenix-group license.txt license.txt
# 声明容器挂载点（必须在 USER 切换前）
VOLUME ["/app/liblog4phoenix", "/app/config"]
# 暴露应用端口
EXPOSE 80
# 切换到非 root 用户
USER phoenix
# 健康检查
HEALTHCHECK --interval=30s --timeout=5s \
    CMD curl -fsS http://localhost/phoenix-ui/actuator/health | grep -q '"status":"UP"' || exit 1
# 容器启动命令
ENTRYPOINT ["authbind", \
"--deep", \
"java", \
"--add-opens", \
"java.base/java.lang=ALL-UNNAMED", \
"--add-opens", \
"java.base/java.lang.reflect=ALL-UNNAMED", \
"-Djava.security.egd=file:/dev/./urandom", \
"-Duser.timezone=GMT+08", \
"-Ddruid.mysql.usePingMethod=false", \
"-Dspring.profiles.active=prod", \
"-jar", \
"phoenix-ui.jar"]