## 介绍

**“phoenix”** 是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、数据库、网络、tcp端口和http接口，通过实时收集、汇聚和分析监控信息，实现在发现异常时立刻推送告警信息，并且提供了可视化系统进行配置、管理、查看。

- 应用程序

  默认支持Java应用程序，监控内容包括：在线状态、JVM、业务埋点。其它应用程序需要自己开发客户端，来调用接口与服务端或者代理端通信（心跳接口、服务器信息接口、告警接口）；

- JVM

  监控内容包括：内存、线程、类、GC等；

- 服务器

  支持主流服务器，如Linux、Windows、macOS、Unix等；    
  监控内容包括：在线状态、操作系统、CPU、进程、磁盘、内存、网卡、电池、传感器；

- 数据库

  支持MySQL、Oracle、Redis、Mongo；  
  监控内容：  
      &emsp;&emsp;MySQL：会话；  
      &emsp;&emsp;Oracle：会话、表空间；  
      &emsp;&emsp;Redis：Redis信息全集；   
      &emsp;&emsp;Mongo：Mongo信息全集；

- 网络：支持监控网络状态；

- TCP：支持监控TCP服务状态；

- HTTP：支持监控HTTP服务状态；

- 告警：默认支持电子邮件。

## 特点

1. 分布式；  
2. 跨平台；  
3. 支持docker部署；
4. 实时监测告警；  
5. 数据加密传输；  
6. 灵活可配置；  
7. 用户界面支持PC端、移动端。

## 设计

- 功能架构

  ![功能导图](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E5%8A%9F%E8%83%BD%E5%AF%BC%E5%9B%BE.png "功能导图")

- 逻辑架构

  ![逻辑架构图](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E9%80%BB%E8%BE%91%E6%9E%B6%E6%9E%84%E5%9B%BE.png "逻辑架构图")

- 运行环境

  Maven3+  
  Jdk1.8(1.8.0_131到1.8.0_241)  
  Lombok  
  Mysql5.7+
  
- 技术选型

  核心框架：SpringBoot  
  安全框架：SpringSecurity、SpringSession  
  任务调度：JUC、SpringTask、Quartz  
  持久层框架：MyBatis、 MyBatis-Plus  
  数据库连接池：Alibaba Druid  
  日志管理：SLF4J、Logback  
  前端框架：Layui、ECharts  
  监控框架：Sigar、oshi
  
- 模块结构

  平台使用Java + Layui + ECharts开发，数据库采用MySQL。

  phoenix（监控平台父工程）  
  ├── phoenix-common（监控公共模块父工程）  
  │ ├── phoenix-common-core（监控核心公共模块）  
  │ └── phoenix-common-web（监控WEB公共模块）  
  ├── phoenix-client（监控客户端父工程）  
  │ ├── phoenix-client-core（监控客户端）  
  │ ├── phoenix-client-spring-boot-starter（监控客户端与springboot集成的starter）  
  │ └── phoenix-client-spring-mvc-integrator（监控客户端与springmvc集成的integrator）  
  ├── phoenix-agent（监控代理端）  
  ├── phoenix-server（监控服务端）  
  ├── phoenix-ui（监控UI端）  
  └── doc（文档）

  phoenix：监控平台父工程，管理平台的依赖、构建、插件等；  
  phoenix-common：监控公共模块，提供平台所有的公共代码，包含一个监控核心公共模块（phoenix-common-core）和一个监控WEB公共模块（phoenix-common-web）；  
  phoenix-client：监控客户端，用于集成到Java应用程序中实现业务埋点和Java应用程序监控信息收集，包含一个通用模块（phoenix-client-core）和与springboot集成的starter（phoenix-client-spring-boot-starter）、与springmvc集成的integrator（phoenix-client-spring-mvc-integrator）两个拓展模块；  
  phoenix-agent：监控代理端，用于收集服务器信息和汇聚、转发来自监控客户端的信息；  
  phoenix-server：监控服务端，是监控平台的核心模块，用于汇聚、分析监控信息，在发现异常时实时推送告警信息；  
  phoenix-ui：监控可视化系统，用于平台配置、用户管理、监控信息查看、图表展示等；  
  doc：包含平台的设计文档、服务启停脚本、数据库脚本等。

## 下载

- 源码仓库地址

  [https://gitee.com/monitoring-platform/phoenix](https://gitee.com/monitoring-platform/phoenix)
  
  [https://github.com/709343767/phoenix](https://github.com/709343767/phoenix)  
  
  **注意：一定要下载最新发行版源码！**

- 示例代码仓库地址

  [https://gitee.com/monitoring-platform/phoenix-example](https://gitee.com/monitoring-platform/phoenix-example)

- 中央仓库地址

1. 客户端为普通Java程序

```xml
<!-- https://mvnrepository.com/artifact/com.gitee.pifeng/phoenix-client-core -->
<dependency>
  <groupId>com.gitee.pifeng</groupId>
  <artifactId>phoenix-client-core</artifactId>
  <version>${最新稳定版本}</version>
</dependency>
```

2. 客户端为springboot程序

```xml
<!-- https://mvnrepository.com/artifact/com.gitee.pifeng/phoenix-client-spring-boot-starter -->
<dependency>
  <groupId>com.gitee.pifeng</groupId>
  <artifactId>phoenix-client-spring-boot-starter</artifactId>
  <version>${最新稳定版本}</version>
</dependency>
```

3. 客户端为springmvc程序

```xml
<!-- https://mvnrepository.com/artifact/com.gitee.pifeng/phoenix-client-spring-mvc-integrator -->
<dependency>
  <groupId>com.gitee.pifeng</groupId>
  <artifactId>phoenix-client-spring-mvc-integrator</artifactId>
  <version>${最新稳定版本}</version>
</dependency>
```

- 最新稳定版本  

  1.2.2.RELEASE

## 使用

### 初始化“监控数据库”

下载项目源码并解压，进入目录：**/phoenix/doc/数据库设计/sql/mysql** ，找到SQL脚本并执行即可。

```
phoenix.sql
```

### 编译源码

解压源码,按照maven格式将源码导入IDE, 使用maven进行编译即可。

### 配置

#### 监控配置

> 监控配置文件为： **monitoring.properties** ，放在 **classpath:/** 下会自动加载，UI端、服务端、代理端、客户端都需要有这个配置文件。如果是springboot项目也可以分环境配置，示例配置代码如下：

```java
/**
* 开发环境监控配置
*/
@Configuration
@Profile("dev")
@EnableMonitoring(configFileName = "monitoring-dev.properties")
public class MonitoringUiDevConfig {
}

/**
* 生产环境监控配置
*/
@Configuration
@Profile("prod")
@EnableMonitoring(configFileName = "monitoring-prod.properties")
public class MonitoringUiProdConfig {
}
```

> 监控配置项说明：

  |配置项                                          |含义                                                                   |必须项       |默认值| 
  |-----------------------------------------------|-----------------------------------------------------------------------|-------------|-----|
  |monitoring.server.url                          |监控服务端（代理端）url                                                  |是           |      |
  |monitoring.server.connect-timeout              |连接超时时间（毫秒）                                                     |否           |15000 |
  |monitoring.server.socket-timeout               |等待数据超时时间（毫秒）                                                  |否           |15000|
  |monitoring.server.connection-request-timeout   |从连接池获取连接的等待超时时间（毫秒）                                      |否           |15000|
  |monitoring.own.instance.order                  |实例次序（整数），用于在集群中区分应用实例，配置“1”就代表集群中的第一个应用实例  |否           |1     |
  |monitoring.own.instance.endpoint               |实例端点类型（server、agent、client、ui）                                 |否           |client|
  |monitoring.own.instance.name                   |实例名称，一般为项目名                                                    |是           |      |
  |monitoring.own.instance.desc                   |实例描述                                                                |否           |      |
  |monitoring.own.instance.language               |程序语言                                                                |否           |Java  |
  |monitoring.heartbeat.rate                      |与服务端或者代理端发心跳包的频率（秒），最小不能小于30秒                      |否           |30    |
  |monitoring.server-info.enable                  |是否采集服务器信息                                                        |否           |false |
  |monitoring.server-info.rate                    |与服务端或者代理端发服务器信息包的频率（秒），最小不能小于30秒                 |否           |60    |
  |monitoring.server-info.ip                      |被监控服务器本机ip地址                                                    |否（自动获取） |      |
  |monitoring.jvm-info.enable                     |是否采集Java虚拟机信息                                                    |否           |false |
  |monitoring.jvm-info.rate                       |与服务端或者代理端发送Java虚拟机信息的频率（秒），最小不能小于30秒             |否           |60    |

1. 监控UI端

    除了在 **monitoring-{profile}.properties** 文件修改监控配置外，还需要在 **application-{profile}.yml** 文件修改数据库配置。

2. 监控服务端

    需要在 **application-{profile}.yml** 文件修改数据库配置和邮箱配置。

3. 监控代理端

    只需在 **monitoring-{profile}.properties** 文件修改监控配置。

4. 监控客户端

    只需添加监控配置。
    
#### 加解密配置

除了监控配置文件外，还可以在 **classpath:/** 下加入 **monitoring-secure.properties** 加解密配置文件，用来修改监控平台的加解密方式。但是注意各监控端加解密配置参数必须相同。这个配置不是必须的，没有此配置文件将使用默认加解密配置，加入此配置文件则必须正确配置配置项。

> 加解密配置项说明：

  |配置项                 |含义                                        |必须项                         |默认值| 
  |----------------------|-------------------------------------------|------------------------------|-----|
  |secret.type           |加解密类型，值只能是 des、aes、sm4 之一         |否，为空则不进行加解密           |      |
  |secret.key.des        |DES密钥                                     |否，secret.type=des时，需要配置     |      |
  |secret.key.aes        |AES密钥                                     |否，secret.type=aes时，需要配置     |      |
  |secret.key.sm4        |国密SM4密钥                                  |否，secret.type=ms4时，需要配置      |      |

秘钥可通过 **com.gitee.pifeng.monitoring.common.util.secure.SecureUtilsTest#testGenerateKey** 方法生成，然后填入配置文件。

#### 第三方登录认证配置

监控UI端除了支持直接登录认证外，还支持第三方登录认证，只需在application.yml（或者application-{profile}.yml）配置文件中增加对应配置项即可使用。

> 第三方登录认证配置说明：

  |配置项                 |含义                                        |必须项                         |默认值| 
  |----------------------|--------------------------------------------|-------------------------------|-----|
  |third-auth.enable     |是否开启第三方认证                            |否                             |false |
  |third-auth.type       |第三方认证类型（CAS）                         |否                             |      |
 
> apereo cas登录认证配置说明：
 
  如果 third-auth.enable=true && third-auth.type=cas ，则需要进行cas配置。

  |配置项                     |含义                                        |必须项                         |默认值 | 
  |--------------------------|--------------------------------------------|------------------------------|-------|
  |cas.key                   |秘钥                                        |否                             |phoenix|
  |cas.server-url-prefix     |cas服务端地址                                |是                             |       |
  |cas.server-login-url      |cas登录地址                                  |是                             |      |
  |cas.server-logout-url     |cas登出地址                                  |是                             |      |
  |cas.client-host-url       |cas客户端地址                                |是                             |      |
  |cas.validation-type       |CAS协议验证类型（CAS、CAS3）                  |否                             |CAS3  |
  
### 客户端开启监控

- 普通Java程序

  在 **main** 方法中，调用方法 **Monitor.start()** 来开启监控功能，或者调用重载的方法 **Monitor.start(configPath, configName)** 指定监控配置文件的路径和名字来开启监控功能，如果未指定配置文件路径和名字，则配置文件需要放在 **classpath:/** 下，名字必须为 **monitoring.properties** 。

- springboot程序

  在启动类上加上注解 **@EnableMonitoring** 来开启监控功能，或者通过注解的两个参数来指定配置文件的路径和名字，如果未指定配置文件路径和名字，则配置文件需要放在 **classpath:/** 下，名字必须为 **monitoring.properties** 。

- springmvc程序

  在 **web.xml** 文件中配置一个监听器，来开启监控功能：

```xml
<!-- 开启监控功能 -->
<web-app>
<context-param>
    <param-name>configLocation</param-name>
    <param-value>classpath:monitoring.properties</param-value>
</context-param>
<listener>
  <listener-class>
    com.gitee.pifeng.monitoring.integrator.listener.MonitoringPlugInitializeListener
  </listener-class>
</listener>
</web-app>
```

### 业务埋点

Java应用程序只要集成了监控客户端，就具有业务埋点监控的能力，通过 **Monitor.buryingPoint()** 方法定时监控业务运行情况，通过 **Monitor.sendAlarm()** 发送告警。具体使用示例如下：
  
```
// 业务埋点监控
ScheduledExecutorService service = Monitor.buryingPoint(() -> {
  // 假如发现了业务异常，用下面的代码发送告警
  Alarm alarm = new Alarm();
  alarm.setAlarmLevel(AlarmLevelEnums.ERROR);
  alarm.setTitle("业务埋点监控");
  alarm.setTest(false);
  alarm.setCharset(Charsets.UTF_8);
  alarm.setMsg("测试普通maven程序业务埋点监控！");
  // alarm.setCode("001");
  alarm.setMonitorType(MonitorTypeEnums.CUSTOM);
  Result result = Monitor.sendAlarm(alarm);
  System.out.println("发送业务告警结果：" + result.toJsonString());
}, 0, 1, TimeUnit.HOURS, ThreadTypeEnums.IO_INTENSIVE_THREAD);
```

### 时钟同步

部署监控程序（监控UI端、监控服务端、监控代理端、监控客户端）的服务器集群需要进行时钟同步（NTP），保证时间的一致性！。  

### 打包部署运行

#### Jar包部署
1. 打包  
   **监控UI端、监控服务端、监控代理端** 直接打成可执行jar。

```shell script
mvn -Dmaven.test.skip=true clean package
```

2. 上传jar、脚本  

   a.jar路径：**phoenix/target**  
    
   b.脚本路径：**phoenix/doc/脚本/**
   
3. 运行，脚本说明如下表：  

    <table>
    <thead>
    <tr>
        <th>程序</th>
        <th>脚本</th>
        <th>命令</th>
        <th>含义</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td rowspan="4">监控UI端</td>
        <td rowspan="4">phoenix_ui.sh</td>
        <td>./phoenix_ui.sh start</td>
        <td>启动</td>
    </tr>
    <tr>
        <td>./phoenix_ui.sh stop</td>
        <td>停止</td>
    </tr>
    <tr>
        <td>./phoenix_ui.sh restart</td>
        <td>重启</td>
    </tr>
    <tr>
        <td>./phoenix_ui.sh status</td>
        <td>检查状态</td>
    </tr>
    <tr>
        <td rowspan="4">监控服务端</td>
        <td rowspan="4">phoenix_server.sh</td>
        <td>./phoenix_ui.sh start</td>
        <td>启动</td>
    </tr>
    <tr>
        <td>./phoenix_server.sh stop</td>
        <td>停止</td>
    </tr>
    <tr>
        <td>./phoenix_server.sh restart</td>
        <td>重启</td>
    </tr>
    <tr>
        <td>./phoenix_server.sh status</td>
        <td>检查状态</td>
    </tr>
    <tr>
        <td rowspan="4">监控代理端</td>
        <td rowspan="4">phoenix_agent.sh</td>
        <td>./phoenix_agent.sh start</td>
        <td>启动</td>
    </tr>
    <tr>
        <td>./phoenix_agent.sh stop</td>
        <td>停止</td>
    </tr>
    <tr>
        <td>./phoenix_agent.sh restart</td>
        <td>重启</td>
    </tr>
    <tr>
        <td>./phoenix_agent.sh status</td>
        <td>检查状态</td>
    </tr>
    </tbody>
    </table> 

#### Docker部署

- 方式一：Maven打包远程部署  

1. 有一台已经安装好docker环境的服务器，并且允许远程连接（以centos7下的yum方式安装的docker且使用service方式运行为例开启远程连接）：

```shell script
vi /usr/lib/systemd/system/docker.service  
#确保：ExecStart 的后面有： -H tcp://0.0.0.0:2375  
#修改完成后保存退出，刷新并重启docker服务   
systemctl daemon-reload  
systemctl restart docker  
```  

2. 在系统环境变量中添DOCKER_HOST，如下图所示：  

![docker_host_config](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E5%85%B6%E5%AE%83/docker_host_config.png "docker_host_config")  

3. 编译项目打包项目并构建镜像

```shell script
 mvn -Dmaven.test.skip=true clean package docker:build  
```

4. 运行  
>监控UI端：
   
```shell script
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 443:443 --pid host --net host --name phoenix-ui phoenix/phoenix-ui /bin/bash
```

>监控服务端：

```shell script
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --pid host --net host --name phoenix-server phoenix/phoenix-server /bin/bash
```

>监控代理端：

```shell script
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --pid host --net host --name phoenix-agent phoenix/phoenix-agent /bin/bash
```

- 方式二：服务器本地构建docker镜像  

1. 打包  
   **监控UI端、监控服务端、监控代理端** 直接打成可执行jar。

```shell script
mvn -Dmaven.test.skip=true clean package
```

2. 上传jar、Dockerfile 
 
   a.jar路径：**phoenix/target**   
   
   b.Dockerfile路径：**phoenix/phoenix-ui/src/main/docker/Dockerfile、  
                    phoenix/phoenix-agent/src/main/docker/Dockerfile、  
                    phoenix/phoenix-server/src/main/docker/Dockerfile**，  
   **Dockerfile** 要与对应的jar包放在同一目录下； 
    
3. 构建docker镜像
>监控UI端：

```shell script
docker build -t phoenix/phoenix-ui .
```

>监控服务端：

```shell script
docker build -t phoenix/phoenix-server .
```

>监控代理端：

```shell script
docker build -t phoenix/phoenix-agent .
```
 
4. 运行
>监控UI端：
   
```shell script
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 443:443 --pid host --net host --name phoenix-ui phoenix/phoenix-ui /bin/bash
```

>监控服务端：

```shell script
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --pid host --net host --name phoenix-server phoenix/phoenix-server /bin/bash
```

>监控代理端：

```shell script
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --pid host --net host --name phoenix-agent phoenix/phoenix-agent /bin/bash
```

### 集群部署

**监控服务端、监控UI端**支持集群部署，提升系统的容灾和可用性。

集群部署时的几点要求和建议：   
1. DB配置保持一致；  
2. 集群机器时钟保持一致（单机集群忽视）；  
3. 建议：推荐通过nginx为集群做负载均衡。监控服务端、监控UI端均通过nginx进行访问。  

![集群部署架构](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E6%9E%B6%E6%9E%84.png "集群部署架构")

### 访问  
**监控UI端** 访问URL：**https://localhost/phoenix-ui/index** ，初始账号/密码：**admin/admin123**，**guest/guest123**。 

## 功能截图

![首页1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E9%A6%96%E9%A1%B51.png "首页1")
  
![首页2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E9%A6%96%E9%A1%B52.png "首页2")
  
![服务器1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%9C%8D%E5%8A%A1%E5%99%A81.png "服务器1")

![服务器2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%9C%8D%E5%8A%A1%E5%99%A82.png "服务器2")
  
![应用程序1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BA%94%E7%94%A8%E7%A8%8B%E5%BA%8F1.png "应用程序1")
  
![应用程序2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BA%94%E7%94%A8%E7%A8%8B%E5%BA%8F2.png "应用程序2")

![数据库1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%95%B0%E6%8D%AE%E5%BA%931.png "数据库1")

![数据库2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%95%B0%E6%8D%AE%E5%BA%932.png "数据库2")

![数据库3](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%95%B0%E6%8D%AE%E5%BA%933.png "数据库3")

![数据库4](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%95%B0%E6%8D%AE%E5%BA%934.png "数据库4")

![网络1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E7%BD%91%E7%BB%9C1.png "网络1")

![网络2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E7%BD%91%E7%BB%9C2.png "网络2")

![TCP1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/TCP1.png "TCP1")

![TCP2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/TCP2.png "TCP2")

![HTTP1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/HTTP1.png "HTTP1")

![HTTP2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/HTTP2.png "HTTP2")

![告警定义](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%91%8A%E8%AD%A6%E5%AE%9A%E4%B9%89.png "告警定义")

![告警记录](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%91%8A%E8%AD%A6%E8%AE%B0%E5%BD%95.png "告警记录")

![用户管理](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png "用户管理")

![操作日志1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%971.png "操作日志1")

![操作日志2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%972.png "操作日志2")

![异常日志1](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BC%82%E5%B8%B8%E6%97%A5%E5%BF%971.png "异常日志1")

![异常日志2](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BC%82%E5%B8%B8%E6%97%A5%E5%BF%972.png "异常日志2")

![监控设置](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E6%88%AA%E5%9B%BE/%E7%9B%91%E6%8E%A7%E8%AE%BE%E7%BD%AE.png "监控设置")

## 常见问题

[https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=4438763&doc_id=935794](https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=4438763&doc_id=935794)

## 升级日志

[https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=4420016&doc_id=935794](https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=4420016&doc_id=935794)

## 期望

欢迎提出更好的意见，帮助完善 phoenix

## 版权

[GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.txt)

## 联系

QQ群：[773127639](https://qm.qq.com/cgi-bin/qm/qr?k=a0yY8EZMVTwvt8Tc1uWuk2hGpvhnyp3C&authKey=nvLNq0pw1yo32ZxbW8rxkYa6yyDn4Vc7f4R65CiifQ+RAgyWXuhszxIKSCB+eb5q&noverify=0)  

## 捐赠

> [捐赠记录,感谢你们的支持！](https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=5547585&doc_id=935794)
 
![捐赠](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E5%85%B6%E5%AE%83/donate.jpg "捐赠")

