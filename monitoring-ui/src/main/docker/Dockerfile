#指定基镜像
FROM openjdk:8-jdk-alpine
#指定数据卷
VOLUME ["/tmp","/persistent-monitoring"]
#作者
MAINTAINER 皮锋 <709343767@qq.com>
#添加项目
ADD monitoring-ui.jar app.jar
#修改这个文件的访问时间为当前时间
RUN sh -c 'touch /app.jar'
#暴露端口
EXPOSE 443
#指定执行启动项目
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","app.jar"]
