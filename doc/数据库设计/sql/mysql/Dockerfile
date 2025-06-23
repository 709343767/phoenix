# 使用官方 MySQL 8.0 镜像
FROM mysql:8.0

# 设置默认数据库信息（可被 docker run 覆盖）
ENV MYSQL_ROOT_PASSWORD=phoenix@2025
ENV MYSQL_DATABASE=phoenix
ENV MYSQL_USER=phoenix
ENV MYSQL_PASSWORD=phoenix@2025

# 复制本地 SQL 初始化脚本到容器内指定路径
COPY ./init-scripts/ /docker-entrypoint-initdb.d/

# 设置权限（确保 mysql 用户能读取这些 SQL 文件）
RUN chmod a+r /docker-entrypoint-initdb.d/*.sql

# 确保使用 mysql 用户运行容器（非 root）
USER mysql