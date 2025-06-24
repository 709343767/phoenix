# 使用官方 MySQL 8.0.37 镜像
FROM mysql:8.0.37

# 设置默认数据库信息（可通过docker run覆盖）
ENV MYSQL_ROOT_PASSWORD=phoenix@2025 \
    MYSQL_DATABASE=phoenix \
    MYSQL_USER=phoenix \
    MYSQL_PASSWORD=phoenix@2025 \
    TZ=Asia/Shanghai

# 设置系统时区
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 复制初始化脚本（自动处理权限问题）
COPY --chown=mysql:mysql ./init-scripts/ /docker-entrypoint-initdb.d/

# 添加健康检查
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD mysqladmin ping -u root -p${MYSQL_ROOT_PASSWORD} || exit 1

# 确保容器以非root用户运行
USER mysql

# 暴露MySQL默认端口（显式声明）
EXPOSE 3306

# 数据卷声明（推荐在运行时映射）
VOLUME /var/lib/mysql