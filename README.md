**源码仓库地址**

[![Gitee仓库](https://img.shields.io/badge/Gitee%E4%BB%93%E5%BA%93-blue)](https://gitee.com/monitoring-platform/phoenix "Gitee仓库") [![GitHub仓库](https://img.shields.io/badge/GitHub%E4%BB%93%E5%BA%93-blue)](https://github.com/709343767/phoenix "GitHub仓库")

**JavaDoc**

[![JavaDoc](https://img.shields.io/badge/JavaDoc-blue "JavaDoc")](https://kacper.fun/modules/javadoc/phoenix/index.html "JavaDoc")

**演示地址**

[![演示地址](https://img.shields.io/badge/phoenix-%E6%BC%94%E7%A4%BA%E5%9C%B0%E5%9D%80-red "演示地址")](http://124.222.235.43/phoenix-ui/index "演示地址")

账号：guest  
密码：guest123  
注意：1.演示项目只提供非管理员账号，只有查看权限！  
   2.演示项目访问比较慢是因为服务器网络带宽太小！

# 1 介绍
“phoenix” 是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、docker、数据库、网络、tcp 端口和 http 接口，通过实时收集、汇聚和分析监控信息，实现在发现异常时立刻推送告警信息，并且提供了可视化系统进行配置、管理、查看。
## 1.1 应用程序
默认支持 Java 应用程序，监控内容包括：在线状态、JVM（内存、线程、类、GC 等）、埋点监控（业务告警、异常日志）。其它应用程序需要自己开发客户端，来调用接口与服务端或者代理端通信（心跳接口、服务器信息接口、下线接口、告警接口）；
## 1.2 服务器
支持主流服务器，如 Linux、Windows、macOS、Unix 等；  
监控内容包括：在线状态、操作系统、CPU、平均负载、进程、磁盘、内存、网卡、电池、传感器；
## 1.3 Docker
监控内容包括：服务、容器、镜像、事件、资源；
## 1.4 数据库
支持 MySQL、Oracle、Redis、Mongo；  
监控内容：  
&nbsp;&nbsp;MySQL：会话；  
&nbsp;&nbsp;Oracle：会话、表空间；  
&nbsp;&nbsp;Redis：Redis信息全集；  
&nbsp;&nbsp;Mongo：Mongo信息全集；
## 1.5 网络
支持监控网络状态；
## 1.6 TCP
支持监控 TCP 服务状态；
## 1.7 HTTP
支持监控 HTTP 服务状态；
## 1.8 告警
默认支持电子邮件、钉钉、企业微信。
# 2 特点
1）分布式；  
2）跨平台；  
3）支持 docker 部署；  
4）实时监测告警；  
5）数据加密传输；  
6）灵活可配置；  
7）用户界面支持 PC 端、移动端；  
8）基于 http 接口，支持拓展监控其它编程语言编写的程序。
# 3 设计
## 3.1 功能架构

**红旗标注部分为收费功能，需向作者购买（500￥，给源码，不开发票）。**

[![功能架构图](https://file.kacper.fun/mblog/2024/04/23/3618cbce0b2b2250d80973781ebc3158.webp "功能架构图")](https://file.kacper.fun/mblog/2024/04/23/3618cbce0b2b2250d80973781ebc3158.webp "功能架构图")

## 3.2 系统架构

[![系统架构图](https://file.kacper.fun/mblog/2023/12/05/75cdc8ff1c007fa0876852520ffd1f76.webp "系统架构图")](https://file.kacper.fun/mblog/2023/12/05/75cdc8ff1c007fa0876852520ffd1f76.webp "系统架构图")

## 3.3 运行环境
1）Maven3+  
2）Jdk >=1.8，若使用 Sigar 监控服务器，则 Jdk 版本要用 1.8(1.8.0_131到1.8.0_241)  
3）Lombok  
4）Mysql5.7+
## 3.4 技术选型
1）核心框架：SpringBoot  
2）安全框架：SpringSecurity、SpringSession  
3）任务调度：JUC、SpringTask、Quartz  
4）持久层框架：MyBatis-Plus  
5）数据库连接池：Alibaba druid  
6）日志管理：SLF4J、Logback  
7）前端框架：Layui、ECharts 、jtopo、xterm  
8）监控框架：Sigar、oshi、Alibaba arthas、docker-java
## 3.5 模块结构
平台使用 Java + Layui + ECharts 开发，数据库采用 MySQL。

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
├── doc（文档）  
└── script（Maven打包脚本）

phoenix：监控平台父工程，管理平台的依赖、构建、插件等；  
phoenix-common：监控公共模块，提供平台所有的公共代码，包含一个监控核心公共模块（phoenix-common-core）和一个监控WEB公共模块（phoenix-common-web）；  
phoenix-client：监控客户端，用于集成到Java应用程序中实现业务埋点和Java应用程序监控信息收集，包含一个通用模块（phoenix-client-core）和与springboot集成的starter（phoenix-client-spring-boot-starter）、与springmvc集成的integrator（phoenix-client-spring-mvc-integrator）两个拓展模块；  
phoenix-agent：监控代理端，用于收集服务器信息、Docker信息，汇聚、转发来自监控客户端的信息，若部署在跳板机上可打通网络壁垒；  
phoenix-server：监控服务端，是监控平台的核心模块，用于汇聚、分析监控信息，在发现异常时实时推送告警信息；  
phoenix-ui：监控可视化系统，用于平台配置、用户管理、监控信息查看、图表展示等；  
doc：包含平台的设计文档、服务启停脚本、数据库脚本等；  
script：包含平台的Maven打包脚本。

# 4 配置
## 4.1 初始化“监控数据库”
下载项目源码并解压，进入目录：**/phoenix/doc/数据库设计/sql/mysql/**，找到 SQL 脚本，用 Navicat 或者其它 MySQL 数据库管理工具连上数据库并执行此文件即可。
```
phoenix.sql
```
## 4.2 编译源码
解压源码，按照 maven 项目格式将源码导入 IDE，使用 maven 进行编译即可。
## 4.3 监控配置
● 监控配置文件为： **monitoring.properties** 或者 **monitoring-{profile}.properties** ，UI 端、服务端、代理端、客户端都需要监控配置。  
● 监控配置文件放置位置及优先级：**指定的filepath > 当前工作目录/config/ > 当前工作目录/ > 指定的classpath > classpath:/config/ > classpath:/** 。例如下图方式放置配置文件，会被自动加载：

[![配置文件夹位置](https://file.kacper.fun/mblog/2024/04/29/2132397e83467efeb00276949b571384.webp "配置文件夹位置")](https://file.kacper.fun/mblog/2024/04/29/2132397e83467efeb00276949b571384.webp "配置文件夹位置")

[![监控配置文件位置](https://file.kacper.fun/mblog/2024/04/29/25b261a16ba273291a07ccab1f85878b.webp "监控配置文件位置")](https://file.kacper.fun/mblog/2024/04/29/25b261a16ba273291a07ccab1f85878b.webp "监控配置文件位置")

➣ **监控配置项（开源版本）**

| 配置项                | 含义                 | 必须项  | 默认值  |
|----------------------|----------------------|---------|--------|
|monitoring.server.url |监控服务端（或者代理端）url |是   |        |
|monitoring.server.connect-timeout|连接超时时间（毫秒）|否|15000  |
|monitoring.server.socket-timeout|等待数据超时时间（毫秒）|否|15000 |
|monitoring.server.connection-request-timeout|从连接池获取连接的等待超时时间（毫秒）|否|15000|
|monitoring.own.instance.order|实例次序（整数），用于在集群中区分应用实例，配置“1”就代表集群中的第一个应用实例|否|1|
|monitoring.own.instance.endpoint|实例端点类型（server、agent、client、ui）|否|client|
|monitoring.own.instance.name|实例名称，一般为项目名|是||
|monitoring.own.instance.desc|实例描述|否||
|monitoring.own.instance.language|程序语言|否|Java|
|monitoring.heartbeat.rate|与服务端（或者代理端）发心跳包的频率（秒），最小不能小于30秒|否|30|
|monitoring.server-info.enable|是否采集服务器信息|否|false|
|monitoring.server-info.rate|与服务端（或者代理端）发服务器信息包的频率（秒），最小不能小于30秒|否|60|
|monitoring.server-info.ip|被监控服务器本机ip地址|否（自动获取）||
|monitoring.server-info.user-sigar-enable|是否使用Sigar采集服务器信息，Sigar要求Jdk1.8(1.8.0_131到1.8.0_241)|否|false|
|monitoring.jvm-info.enable|是否采集Java虚拟机信息|否|false|
|monitoring.jvm-info.rate|与服务端（或者代理端）发送Java虚拟机信息的频率（秒），最小不能小于30秒|否|60|

➣ **监控配置项（收费版本）**

| 配置项                | 含义                 | 必须项  | 默认值  |
|----------------------|----------------------|---------|--------|
|monitoring.comm.comm-framework-type|与服务端（或者代理端）通信的通信框架类型|否|apacheHttpComponents|
|monitoring.comm.http.url|监控服务端（或者代理端）url |如果通信框架是apacheHttpComponents，则必须配置，否则不用配置||
|monitoring.comm.http.connect-timeout|连接超时时间（毫秒）|否|15000  |
|monitoring.comm.http.socket-timeout|等待数据超时时间（毫秒）|否|15000 |
|monitoring.comm.http.connection-request-timeout|从连接池获取连接的等待超时时间（毫秒）|否|15000|
|monitoring.own.instance.order|实例次序（整数），用于在集群中区分应用实例，配置“1”就代表集群中的第一个应用实例|否|1|
|monitoring.own.instance.endpoint|实例端点类型（server、agent、client、ui）|否|client|
|monitoring.own.instance.name|实例名称，一般为项目名|是||
|monitoring.own.instance.desc|实例描述|否||
|monitoring.own.instance.language|程序语言|否|Java|
|monitoring.heartbeat.rate|与服务端（或者代理端）发心跳包的频率（秒），最小不能小于30秒|否|30|
|monitoring.server-info.enable|是否采集服务器信息|否|false|
|monitoring.server-info.rate|与服务端（或者代理端）发服务器信息包的频率（秒），最小不能小于30秒|否|60|
|monitoring.server-info.ip|被监控服务器本机ip地址|否（自动获取）||
|monitoring.server-info.user-sigar-enable|是否使用Sigar采集服务器信息，Sigar要求Jdk1.8(1.8.0_131到1.8.0_241)|否|false|
|monitoring.jvm-info.enable|是否采集Java虚拟机信息|否|false|
|monitoring.jvm-info.rate|与服务端（或者代理端）发送Java虚拟机信息的频率（秒），最小不能小于30秒|否|60|
|monitoring.docker-info.enable|是否采集docker信息|否|false|
|monitoring.docker-info.rate|与服务端（或者代理端）发送docker信息的频率（秒），最小不能小于30秒|否|60|
|monitoring.docker-info.host|被监控的docker主机|否|tcp://localhost:2375|
|monitoring.docker-info.tls-verify|启用/禁用TLS验证|否|false|
|monitoring.docker-info.cert-path|验证所需证书的路径|否||
|monitoring.docker-info.config|其他docker配置文件的路径|否||
|monitoring.docker-info.api-version|API版本|否||
|monitoring.docker-info.registry-url|注册地址|否||
|monitoring.docker-info.registry-username|注册用户名|否||
|monitoring.docker-info.registry-password|注册密码|否||
|monitoring.docker-info.registry-email|注册电子邮箱|否||
|monitoring.arthas.enable|是否开启arthas|否||
|monitoring.arthas.tunnel-server-url|arthas服务端url地址|否||

1）监控 UI 端  
在 **monitoring-{profile}.properties** 文件配置监控属性。  
2）监控服务端  
在 **monitoring-{profile}.properties** 文件配置监控属性。  
3）监控代理端  
在 **monitoring-{profile}.properties** 文件配置监控属性。  
4）监控客户端  
添加监控配置文件 **monitoring.properties** 或者 **monitoring-{profile}.properties** ，并且正确配置它。

***注意：***  
***1）{profile} 代表环境，源码中有 dev（开发环境）和 prod（生产环境）。***  
***2）在收费版本中，springboot 客户端可以把监控配置写在 application.yml（或者 application-{profile}.yml）文件中，不再需要单独的监控配置文件，如果仍然使用单独的监控配置文件，需要注解 @EnableMonitoring(usingMonitoringConfigFile = true) 。***

**application.yml**
```yaml
##############################phoenix监控配置##############################
phoenix:
  monitoring:
    comm:
      comm-framework-type: apache_http_components
      http:
        url: http://127.0.0.1:16000/phoenix-server
        connect-timeout: 15000
        socket-timeout: 60000
        connection-request-timeout: 15000
    instance:
      name: 
      desc: 
    jvm-info:
      enable: true
    arthas:
      enable: true
      tunnel-server-url: wss://127.0.0.1:8081/phoenix-ui
```

## 4.4 加解密配置
● 除了监控配置文件外，可以加入 **monitoring-secure.properties** 加解密配置文件，用来配置监控平台的加解密方式。注意各监控端加解密配置参数必须相同，否则无法进行正确解密。这个配置不是必须的，没有此配置文件将使用默认加解密配置，加入此配置文件则必须正确配置配置项。  
● 监控加解密配置文件放置位置及优先级：**当前工作目录/config/ > 当前工作目录/ > classpath:/config/ > classpath:/**

➣ **加解密配置项说明**：

| 配置项                | 含义                 | 必须项  | 默认值  |
|----------------------|----------------------|---------|--------|
|secret.type|加解密类型，值只能是 des、aes、sm4 之一|否，为空则不进行加解密||
|secret.key.des|DES密钥|否，secret.type=des时，需要配置||
|secret.key.aes|AES密钥|否，secret.type=aes时，需要配置||
|secret.key.sm4|国密SM4密钥|否，secret.type=ms4时，需要配置||

秘钥可通过 **com.gitee.pifeng.monitoring.common.util.secure.SecureUtilsTest#testGenerateKey()** 方法生成，然后填入配置文件。
## 4.5 UI端自定义配置
监控UI端自定义了一些配置项，只需在 **application.yml（或者application-{profile}.yml）** 配置文件中配置即可。

```yaml
#监控UI端自定义配置
phoenix:
  #认证
  auth:
    #认证类型(self、thrid，默认值：self)
    type: self
    #本平台认证
    self-auth:
      #登录验证码
      login-captcha:
        #是否启用验证码
        enable: false
    #第三方认证
    third-auth:
      #第三方认证类型(cas)
      type: cas
      #CAS
      cas:
        #秘钥(默认值：phoenix)
        key: phoenix
        #cas服务端地址
        server-url-prefix: https://cas.example.org:8443/cas
        #cas登录地址
        server-login-url: ${phoenix.auth.third-auth.cas.server-url-prefix}/login
        #cas登出地址
        server-logout-url: ${phoenix.auth.third-auth.cas.server-url-prefix}/logout?service=${phoenix.auth.third-auth.cas.client-host-url}/index
        #cas客户端地址
        client-host-url: http://127.0.0.1:${server.port}${server.servlet.context-path}
        #CAS协议验证类型(cas、cas3，默认值：cas3)
        validation-type: cas3
```

## 4.6 邮箱配置
使用邮箱告警时，监控服务端需要进行邮箱配置，以 QQ 邮箱为例。
### 4.6.1 QQ邮箱生成授权码
1）在浏览器打开并登录 QQ 邮箱

[![打开并登录QQ邮箱](https://file.kacper.fun/mblog/2023/12/05/80721ba8a9d7dcae0c2a3d18f24b8f82.webp "打开并登录QQ邮箱")](https://file.kacper.fun/mblog/2023/12/05/80721ba8a9d7dcae0c2a3d18f24b8f82.webp "打开并登录QQ邮箱")

2）点击“设置”，打开邮箱设置

[![打开邮箱设置](https://file.kacper.fun/mblog/2023/12/05/28643612bd670dd1f652f123aa8cfde2.webp "打开邮箱设置")](https://file.kacper.fun/mblog/2023/12/05/28643612bd670dd1f652f123aa8cfde2.webp "打开邮箱设置")

3）点击“账户”，进入“账户”选项界面

[![进入“账户”选项界面](https://file.kacper.fun/mblog/2023/12/05/a3bf8029cb824b8eacfe1cbc7c7a27e8.webp "进入“账户”选项界面")](https://file.kacper.fun/mblog/2023/12/05/a3bf8029cb824b8eacfe1cbc7c7a27e8.webp "进入“账户”选项界面")

4）找到“POP3/SMTP服务”旁边的“开启”按钮，开启POP3/SMTP服务

[![开启POP3/SMTP服务](https://file.kacper.fun/mblog/2023/12/05/a63167b5e2c86baf53fa10c03aa1783d.webp "开启POP3/SMTP服务")](https://file.kacper.fun/mblog/2023/12/05/a63167b5e2c86baf53fa10c03aa1783d.webp "开启POP3/SMTP服务")

5）用邮箱绑定的手机号码发送短信到指定号码，发送后点击“我已发送”

[![发送验证短信](https://file.kacper.fun/mblog/2023/12/05/4b2f0f7306df55ba87119cfd685fe9e7.webp "发送验证短信")](https://file.kacper.fun/mblog/2023/12/05/4b2f0f7306df55ba87119cfd685fe9e7.webp "发送验证短信")

6）获取授权码

[![获取授权码](https://file.kacper.fun/mblog/2023/12/05/f117a1cb33cbc51b41b8d2dbe2f59d2d.webp "获取授权码")](https://file.kacper.fun/mblog/2023/12/05/f117a1cb33cbc51b41b8d2dbe2f59d2d.webp "获取授权码")

### 4.6.2 监控服务端配置邮箱
在监控服务端的 **application-{profile}.yml** 文件中，配置邮箱，如下图所示：

[![监控服务端配置邮箱](https://file.kacper.fun/mblog/2023/12/05/17f6fae0f7408b1c897dbaf17e10918c.webp "监控服务端配置邮箱")](https://file.kacper.fun/mblog/2023/12/05/17f6fae0f7408b1c897dbaf17e10918c.webp "监控服务端配置邮箱")


## 4.7 数据源配置

```yaml
###############################################数据源配置##################################################
spring:
  datasource:
    druid:
      #数据源配置
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/phoenix?useSSL=false&serverTimezone=GMT%2b8&useUnicode=true&characterEncoding=utf-8&autoReconnect=true&failOverReadOnly=false&rewriteBatchedStatements=true
      username: xxx
      password: xxx
```

1）监控UI端  
在 **application-{profile}.yml** 文件配置数据源。  
2）监控服务端  
在 **application-{profile}.yml** 文件配置数据源。
## 4.8 客户端启用监控
客户端启用监控分为三步，首先加入监控 jar 包，然后加入监控配置文件，最后开启监控。
### 4.8.1 普通Java程序
在程序中加入监控 jar 包，例如 Maven 项目，则在 pom.xml 文件中增加如下依赖：

```xml
<!-- https://mvnrepository.com/artifact/com.gitee.pifeng/phoenix-client-core -->
<dependency>
  <groupId>com.gitee.pifeng</groupId>
  <artifactId>phoenix-client-core</artifactId>
  <version>${匹配源代码版本}</version>
</dependency>
```

在 main 方法中，调用方法 **Monitor.start()** 来开启监控功能，或者调用重载的方法 **Monitor.start(configPath, configName)** 指定监控配置文件的路径和名字来开启监控功能，如果未指定配置文件路径和名字，则配置文件需要放在 **classpath:/** 下，名字必须为 **monitoring.properties** 。示例配置代码如下：

```java
public class Main {
    public static void main(String[] args) {
        // 开启监控
        Monitor.start();
        // 其它业务代码
    }
}
```
### 4.8.2 Spring boot程序
在程序中加入监控 jar 包，例如 Maven 项目，则在 pom.xml 文件中增加如下依赖：

```xml
<!-- https://mvnrepository.com/artifact/com.gitee.pifeng/phoenix-client-spring-boot-starter -->
<dependency>
  <groupId>com.gitee.pifeng</groupId>
  <artifactId>phoenix-client-spring-boot-starter</artifactId>
  <version>${匹配源代码版本}</version>
</dependency>
```

在启动类上加上注解 **@EnableMonitoring** 来开启监控功能，或者通过注解的两个参数来指定配置文件的路径和名字，如果未指定配置文件路径和名字，则配置文件需要放在 **classpath:/** 下，名字必须为 **monitoring.properties** 。

1）不分环境配置，示例配置代码如下：

```java
@EnableMonitoring
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
2）分环境配置，示例配置代码如下：

```java
/**
* 开发环境监控配置
*/
@Configuration
@Profile("dev")
@EnableMonitoring(configFileName = "monitoring-dev.properties")
public class MonitoringUiDevConfig {
}
```

```java
/**
* 生产环境监控配置
*/
@Configuration
@Profile("prod")
@EnableMonitoring(configFileName = "monitoring-prod.properties")
public class MonitoringUiProdConfig {
}
```
### 4.8.3 Spring mvc程序
在程序中加入监控 jar 包，例如 Maven 项目，则在 pom.xml 文件中增加如下依赖：

```xml
<!-- https://mvnrepository.com/artifact/com.gitee.pifeng/phoenix-client-spring-mvc-integrator -->
<dependency>
  <groupId>com.gitee.pifeng</groupId>
  <artifactId>phoenix-client-spring-mvc-integrator</artifactId>
  <version>${匹配源代码版本}</version>
</dependency>
```

在 web.xml 文件中配置一个监听器，来开启监控功能：

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
## 4.9 时钟同步
部署监控程序（监控UI端、监控服务端、监控代理端、监控客户端）的服务器集群需要进行时钟同步（NTP），保证时间的一致性！
# 5 部署
## 5.1 Jar包部署
1）打包  
监控 UI 端、监控服务端、监控代理端 直接打成可执行 jar。
```shell
mvn -Dmaven.test.skip=true clean package
```
2）上传 jar、脚本  
i.jar在源码中的路径：  
&nbsp;&nbsp;phoenix/target/phoenix-ui.jar  
&nbsp;&nbsp;phoenix/target/phoenix-server.jar  
&nbsp;&nbsp;phoenix/target/phoenix-agent.jar  
ii.脚本在源码中的路径：  
&nbsp;&nbsp;phoenix/doc/脚本/UI端/phoenix_ui.sh  
&nbsp;&nbsp;phoenix/doc/脚本/服务端/phoenix_server.sh  
&nbsp;&nbsp;phoenix/doc/脚本/代理端/phoenix_agent.sh  
&nbsp;&nbsp;phoenix/doc/脚本/UI端/startup_ui.bat  
&nbsp;&nbsp;phoenix/doc/脚本/服务端/startup_server.bat  
&nbsp;&nbsp;phoenix/doc/脚本/代理端/startup_agent.bat  
3）运行  
i.Linux 系统

|程序           |脚本            |命令           |含义            |
|---------------|---------------|---------------|---------------|
|监控UI端       |phoenix_ui.sh   |./phoenix_ui.sh start|启动     |
|监控UI端       |phoenix_ui.sh   |./phoenix_ui.sh stop|停止      |
|监控UI端       |phoenix_ui.sh   |./phoenix_ui.sh restart|重启   |
|监控UI端       |phoenix_ui.sh   |./phoenix_ui.sh status|检查状态|
|监控服务端     |phoenix_server.sh|./phoenix_server.sh start|启动|
|监控服务端     |phoenix_server.sh|./phoenix_server.sh stop|停止 |
|监控服务端     |phoenix_server.sh|./phoenix_server.sh restart|重启|
|监控服务端     |phoenix_server.sh|./phoenix_server.sh status|检查状态|
|监控代理端     |phoenix_agent.sh|./phoenix_agent.sh start|启动  |
|监控代理端     |phoenix_agent.sh|./phoenix_agent.sh stop |停止  |
|监控代理端     |phoenix_agent.sh|./phoenix_agent.sh restart|重启|
|监控代理端     |phoenix_agent.sh|./phoenix_agent.sh status|检查状态|

ii.Windows 系统

|程序           |脚本            |含义           |
|---------------|---------------|---------------|
|监控UI端       |startup_ui.bat |启动            |
|监控服务端     |startup_server.bat|启动         |
|监控代理端     |startup_agent.bat|启动          |

## 5.2 安装成Windows服务
1）打包  
监控 UI 端、监控服务端、监控代理端 直接打成 zip 包。
```shell
mvn -Dmaven.test.skip=true clean package
```
2）上传 zip 包  
zip包在源码中的路径：  
&nbsp;&nbsp;phoenix/target/phoenix-ui-windows.zip  
&nbsp;&nbsp;phoenix/target/phoenix-server-windows.zip  
&nbsp;&nbsp;phoenix/target/phoenix-agent-windows.zip  
3）解压 zip 包  
4）运行

|脚本                 |含义                |
|--------------------|--------------------|
|service_install.cmd  |安装                |
|service_restart.cmd  |重启                |
|service_start.cmd    |启动                |
|service_status.cmd   |查看状态            |
|service_stop.cmd     |停止                |
|service_uninstall.cmd|卸载                |

## 5.3 安装成Linux服务
参考文章[《phoenix UI端程序在 Linux 服务器上设置开机自启动》](https://kacper.fun/blog/86 "《phoenix UI端程序在 Linux 服务器上设置开机自启动》")、[《phoenix代理端程序在 Linux 服务器上设置开机自启动》](https://kacper.fun/blog/85 "《phoenix代理端程序在 Linux 服务器上设置开机自启动》")、[《phoenix服务端程序在 Linux 服务器上设置开机自启动》](https://kacper.fun/blog/87 "《phoenix服务端程序在 Linux 服务器上设置开机自启动》")。

## 5.4 Docker部署
### 5.4.1 方式一：Maven打包远程部署
1）有一台已经安装好 docker 环境的服务器，并且允许远程连接（以 centos7 下的 yum 方式安装的 docker，且使用 service 方式运行为例，开启远程连接）：
```shell
vi /usr/lib/systemd/system/docker.service
#确保：ExecStart 的后面有： -H tcp://0.0.0.0:2375
#修改完成后保存退出，刷新并重启docker服务
systemctl daemon-reload
systemctl restart docker
```
2）在系统环境变量中添 DOCKER_HOST，如下图所示：

[![系统环境变量中添DOCKER_HOST](https://file.kacper.fun/mblog/2023/12/05/7b6e1ec87cf7acbbf6d550b913e02bff.webp "系统环境变量中添DOCKER_HOST")](https://file.kacper.fun/mblog/2023/12/05/7b6e1ec87cf7acbbf6d550b913e02bff.webp "系统环境变量中添DOCKER_HOST")

3）编译项目打包项目并构建镜像
```shell
mvn -Dmaven.test.skip=true clean package docker:build
```
4）运行  
● 监控UI端：
```shell
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 443:443 --pid host --net host --name phoenix-ui phoenix/phoenix-ui /bin/bash
```
● 监控服务端：
```shell
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --pid host --net host --name phoenix-server phoenix/phoenix-server /bin/bash
```
● 监控代理端：
```shell
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --pid host --net host --name phoenix-agent phoenix/phoenix-agent /bin/bash
```
### 5.4.2 方式二：服务器本地构建docker镜像
1）打包  
监控 UI 端、监控服务端、监控代理端 直接打成可执行 jar。
```shell
mvn -Dmaven.test.skip=true clean package
```
2）上传 jar、Dockerfile  
i.jar在源码中的路径：phoenix/target/  
ii.Dockerfile在源代码中的路径：  
&nbsp;&nbsp;phoenix/phoenix-ui/src/main/docker/Dockerfile、  
&nbsp;&nbsp;phoenix/phoenix-agent/src/main/docker/Dockerfile、  
&nbsp;&nbsp;phoenix/phoenix-server/src/main/docker/Dockerfile，  
&nbsp;&nbsp;Dockerfile 要与对应的jar包放在同一目录下；  
3）构建 docker 镜像  
● 监控 UI 端：
```shell
docker build -t phoenix/phoenix-ui .
```
● 监控服务端：
```shell
docker build -t phoenix/phoenix-server .
```
● 监控代理端：
```shell
docker build -t phoenix/phoenix-agent .
```
4）运行  
● 监控 UI 端：
```shell
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 443:443 --pid host --net host --name phoenix-ui phoenix/phoenix-ui /bin/bash
```
● 监控服务端：
```shell
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 16000:16000 --pid host --net host --name phoenix-server phoenix/phoenix-server /bin/bash
```
● 监控代理端：
```shell
docker run -itd -v /tmp:/tmp -v /liblog4phoenix:/liblog4phoenix -v /etc/localtime:/etc/localtime:ro -p 12000:12000 --pid host --net host --name phoenix-agent phoenix/phoenix-agent /bin/bash
```
## 5.5 集群部署
**监控服务端、监控 UI 端** 支持集群部署，提升系统的容灾和可用性。  
集群部署时的几点要求和建议：  
1）DB 配置保持一致；  
2）集群机器时钟保持一致（单机集群忽视）；  
3）建议：推荐通过 nginx 为集群做负载均衡。监控服务端、监控 UI 端均通过 nginx 进行访问。

[![集群部署架构图](https://file.kacper.fun/mblog/2023/12/05/2243ac20e83f7517e1abd06dfe623796.webp "集群部署架构图")](https://file.kacper.fun/mblog/2023/12/05/2243ac20e83f7517e1abd06dfe623796.webp "集群部署架构图")

# 6 访问
监控 UI 端 访问 URL：**http(s)://localhost/phoenix-ui/index** ，开发环境(dev)使用 http，生产环境(prod)使用 https，初始账号/密码：**admin/admin123**，**guest/guest123**。

# 7 使用
## 7.1 配置管理
### 7.1.1 环境管理
环境管理是为了给监控平台中监控的服务项（服务器、应用程序、数据库、网络、TCP 服务、HTTP 服务、Docker 服务）添加环境，从而方便根据这些服务的实际使用环境进行管理，如：筛选、导出。

[![环境管理](https://file.kacper.fun/mblog/2023/12/17/df4e8d064cf3123fadfa14d66e319c06.webp "环境管理")](https://file.kacper.fun/mblog/2023/12/17/df4e8d064cf3123fadfa14d66e319c06.webp "环境管理")

[![设置被监控的服务器环境](https://file.kacper.fun/mblog/2023/12/17/57f7e976cb785fa04394ce5d82d7ec26.webp "设置被监控的服务器环境")](https://file.kacper.fun/mblog/2023/12/17/57f7e976cb785fa04394ce5d82d7ec26.webp "设置被监控的服务器环境")

[![根据环境查询服务器列表](https://file.kacper.fun/mblog/2024/04/30/8dbf2da6b6da46f1d141fa0bb4caed18.webp "根据环境查询服务器列表")](https://file.kacper.fun/mblog/2024/04/30/8dbf2da6b6da46f1d141fa0bb4caed18.webp "根据环境查询服务器列表")

### 7.1.2 分组管理
分组管理与环境管理类似，但它是从另一个维度去管理监控平台中监控的服务项。

### 7.1.3 监控配置
#### 7.1.3.1 阈值
阈值一共分为 10 个等级，从 1 到 10，值越小监控越灵敏。例如 **monitoring.properties** 配置文件中配置的 **monitoring.heartbeat.rate=30**（心跳频率是 30 秒），而监控配置界面中配置的阈值是 2，则 30×2=60秒 内没收到心跳包，判别应用程序离线，反之应用程序在线；若监控配置界面中配置的阈值是3，则 30×3=90秒 内没收到心跳包，判别应用程序离线，反之应用程序在线。

[![告警阈值](https://file.kacper.fun/mblog/2023/12/17/a5876467523d8e0a239145fd622a223f.webp "告警阈值")](https://file.kacper.fun/mblog/2023/12/17/a5876467523d8e0a239145fd622a223f.webp "告警阈值")

#### 7.1.3.2 告警
##### 7.1.3.2.1 是否告警
“是否告警”配置项是告警总开关，若此处关闭，则整个监控平台不发送任何告警信息。

[![告警开关](https://file.kacper.fun/mblog/2023/12/17/616ba6dfe9bf5ef336bb9ef27b78de0c.webp "告警开关")](https://file.kacper.fun/mblog/2023/12/17/616ba6dfe9bf5ef336bb9ef27b78de0c.webp "告警开关")

##### 7.1.3.2.2 级别
告警分为 4 个级别：INFO、WARN、ERROR、FATAL。  
FATAL > ERROR > WARN > INFO，只有具体告警项配置的告警级别大于等于此处配置的级别时，才会在异常时发送告警，反之不发送告警。例如：服务器 CPU 的告警级别是 INFO，而此处配置的告警级别是 WARN，则服务器 CPU 过载也不会发送告警。

[![告警级别](https://file.kacper.fun/mblog/2023/12/17/474253d003cba44f0510164e3d87327b.webp "告警级别")](https://file.kacper.fun/mblog/2023/12/17/474253d003cba44f0510164e3d87327b.webp "告警级别")

##### 7.1.3.2.3 方式
告警方式有邮箱、短信、钉钉、企业微信。是一个多选开关，可以同时配置多种告警方式发送告警。其中短信告警需要自己二次开发去对接自己公司的短信服务接口。

[![告警方式](https://file.kacper.fun/mblog/2023/12/17/27a9e9d2606027d0863532100e0d19dc.webp "告警方式")](https://file.kacper.fun/mblog/2023/12/17/27a9e9d2606027d0863532100e0d19dc.webp "告警方式")

###### 7.1.3.2.3.1 邮箱
可以配置多个收件人邮箱号。

[![邮箱告警](https://file.kacper.fun/mblog/2023/12/17/4f261f3832aae38dd53ab742e10f6331.webp "邮箱告警")](https://file.kacper.fun/mblog/2023/12/17/4f261f3832aae38dd53ab742e10f6331.webp "邮箱告警")

###### 7.1.3.2.3.2 短信
① 可以配置多个收信人手机号；  
② 接口地址填自己公司的短信服务地址，并且在监控平台的 phoenix-server 模块做二次开发以支持短信告警。

[![短信告警](https://file.kacper.fun/mblog/2023/12/17/445179da90f3d0ce45b73cedd94f6f46.webp "短信告警")](https://file.kacper.fun/mblog/2023/12/17/445179da90f3d0ce45b73cedd94f6f46.webp "短信告警")

###### 7.1.3.2.3.3 钉钉(收费版)
(1) 创建群机器人  
① 在 PC 端钉钉选择需要添加机器人的群聊，然后依次单击【群设置 -> 智能群助手 -> 机器人管理】

[![创建群机器人](https://file.kacper.fun/mblog/2023/12/17/13294cb15d1b0c737f2637e07c05d9a1.webp "创建群机器人")](https://file.kacper.fun/mblog/2023/12/17/13294cb15d1b0c737f2637e07c05d9a1.webp "创建群机器人")

[![创建群机器人](https://file.kacper.fun/mblog/2023/12/17/585ab01fd6e24ec73a2472ea0d31916d.webp "创建群机器人")](https://file.kacper.fun/mblog/2023/12/17/585ab01fd6e24ec73a2472ea0d31916d.webp "创建群机器人")

[![创建群机器人](https://file.kacper.fun/mblog/2023/12/17/ed69a288cefd0ac84cb22f8e4d562749.webp "创建群机器人")](https://file.kacper.fun/mblog/2023/12/17/ed69a288cefd0ac84cb22f8e4d562749.webp "创建群机器人")

[![创建群机器人](https://file.kacper.fun/mblog/2023/12/17/c119b2e2b709b730a8645f5329115c70.webp "创建群机器人")](https://file.kacper.fun/mblog/2023/12/17/c119b2e2b709b730a8645f5329115c70.webp "创建群机器人")

② 在机器人管理页面选择自定义机器人，输入机器人名字并选择要发送消息的群，同时可以为机器人设置机器人头像

[![创建群机器人](https://file.kacper.fun/mblog/2023/12/17/8026626fbc58dc2605c9505a32682ab6.webp "创建群机器人")](https://file.kacper.fun/mblog/2023/12/17/8026626fbc58dc2605c9505a32682ab6.webp "创建群机器人")

③ 完成必要的安全设置，勾选**我已阅读并同意《自定义机器人服务及免责条款》**，然后单击**完成**

[![创建群机器人](https://file.kacper.fun/mblog/2023/12/17/33402f39d05616d8c979411adeaf72d9.webp "创建群机器人")](https://file.kacper.fun/mblog/2023/12/17/33402f39d05616d8c979411adeaf72d9.webp "创建群机器人")

(2) 配置钉钉告警

[![配置钉钉告警](https://file.kacper.fun/mblog/2023/12/17/b4079a46f9d0c1ae0f8f1671b56ed26a.webp "配置钉钉告警")](https://file.kacper.fun/mblog/2023/12/17/b4079a46f9d0c1ae0f8f1671b56ed26a.webp "配置钉钉告警")

###### 7.1.3.2.3.4 企业微信(收费版)
(1) 创建群机器人  
① 群机器人添加入口  
【PC内部群聊 -> 右上方三个点 -> 添加群机器人】，可以新建机器人或选择已发布到公司的机器人

[![端添加群机器人](https://file.kacper.fun/mblog/2023/12/17/9ab0fdb9f5b50ca46528499d3f0ab174.webp "端添加群机器人")](https://file.kacper.fun/mblog/2023/12/17/9ab0fdb9f5b50ca46528499d3f0ab174.webp "端添加群机器人")

【手机端内部群聊 -> 右上角三个点 -> 添加群机器人】：

[![手机端添加群机器人](https://file.kacper.fun/mblog/2023/12/17/bcc3ac7e5207f1f6bc82dce613c6e7a7.webp "手机端添加群机器人")](https://file.kacper.fun/mblog/2023/12/17/bcc3ac7e5207f1f6bc82dce613c6e7a7.webp "手机端添加群机器人")

② 获取群机器人Webhook地址

群机器人的创建人，可在查看机器人信息时，获取对应机器人的 Webhook URL。  
手机端【进入群聊 -> 右上角三个点 -> 群机器人 -> 点击对应机器人 -> Webhook地址】

[![手机端获取群机器人Webhook地址](https://file.kacper.fun/mblog/2023/12/17/b45f747e1a32849e0bbbbd04210ee22b.webp "手机端获取群机器人Webhook地址")](https://file.kacper.fun/mblog/2023/12/17/b45f747e1a32849e0bbbbd04210ee22b.webp "手机端获取群机器人Webhook地址")

电脑端【进入群聊 -> 群成员列表 -> 右键对应机器人 -> 查看资料-> Webhook地址】

[![端获取群机器人Webhook地址](https://file.kacper.fun/mblog/2023/12/17/90ad928fc94463d99a3f0fa576b432f3.webp "端获取群机器人Webhook地址")](https://file.kacper.fun/mblog/2023/12/17/90ad928fc94463d99a3f0fa576b432f3.webp "端获取群机器人Webhook地址")

(2) 配置企业微信告警

[![配置企业微信告警](https://file.kacper.fun/mblog/2023/12/17/38e64677fdc96a7795ef815a110cb3af.webp "配置企业微信告警")](https://file.kacper.fun/mblog/2023/12/17/38e64677fdc96a7795ef815a110cb3af.webp "配置企业微信告警")

#### 7.1.3.3 网络
网络有一个是否监控的开关，打开则监控网络，关闭则不监控网络。

[![网络监控开关](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "网络监控开关")](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "网络监控开关")

#### 7.1.3.4 TCP服务
##### 7.1.3.4.1 是否监控
TCP 服务有一个是否监控的开关，打开则监控 TCP 服务，关闭则不监控 TCP 服务。

[![TCP服务监控开关](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "TCP服务监控开关")](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "TCP服务监控开关")

#### 7.1.3.5 HTTP服务
##### 7.1.3.5.1 是否监控
HTTP 服务有一个是否监控的开关，打开则监控 HTTP 服务，关闭则不监控 HTTP 服务。

[![HTTP服务监控开关](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "HTTP服务监控开关")](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "HTTP服务监控开关")

#### 7.1.3.6 数据库
##### 7.1.3.6.1 是否监控
数据库有一个是否监控的开关，打开则监控数据库，关闭则不监控数据库。

[![数据库监控开关](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "数据库监控开关")](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "数据库监控开关")

##### 7.1.3.6.2 表空间(Oracle)
Oracle 数据库支持表空间监控，可以设置表空间的告警阈值和告警级别，满足条件则进行告警。

[![Oracle表空间监控配置](https://file.kacper.fun/mblog/2023/12/17/a60f22cc88e808ce21496d3a3a09522d.webp "Oracle表空间监控配置")](https://file.kacper.fun/mblog/2023/12/17/a60f22cc88e808ce21496d3a3a09522d.webp "Oracle表空间监控配置")

#### 7.1.3.7 服务器
##### 7.1.3.7.1 是否告警
服务器有一个是否监控的开关，打开则监控服务器，关闭则不监控服务器。

[![服务器监控开关](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "服务器监控开关")](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "服务器监控开关")

##### 7.1.3.7.2 CPU
可以设置 CPU 监控的告警阈值和告警级别，满足条件则进行告警。

[![CPU监控](https://file.kacper.fun/mblog/2023/12/17/1e13d042d450aa143eb1db7e0a6f2d2e.webp "CPU监控")](https://file.kacper.fun/mblog/2023/12/17/1e13d042d450aa143eb1db7e0a6f2d2e.webp "CPU监控")

##### 7.1.3.7.3 15分钟负载
平均负载，是衡量一个系统整体负载情况的关键指标，指是处于可运行状态和不可中断状态的进程的平均数量。即单位时间内，系统处于可运行状态和不可中断状态的平均进程数，也就是平均活跃进程数。它和 CPU 使用率并没有直接关系。平均负载值越低越好，负载过高会导致机器无法处理其他请求及操作，甚至导致死机。系统的负载高，主要是由于 CPU 使用、内存使用、IO 消耗三部分构成。任意一项使用过多，都将导致负载的急剧攀升。   
在平均负载的指标中，有三个值：1分钟平均负载、5分钟平均负载，15分钟平均负载。我们在排查问题的时候也是可以参考这三个值。一般情况下，1分钟平均负载表示的是最近的暂时现象，15分钟平均负载表示的是持续现象，并非暂时问题。如果15分钟平均负载较高，而1分钟平均负载较低，可以认为情况有所好转。反之，情况可能在恶化。   
监控平台可以设置15分钟平均负载监控的告警阈值和告警级别，满足条件则进行告警。

[![15分钟负载监控](https://file.kacper.fun/mblog/2023/12/17/0d195ad372a80c144430e8b31ee7f01b.webp "15分钟负载监控")](https://file.kacper.fun/mblog/2023/12/17/0d195ad372a80c144430e8b31ee7f01b.webp "15分钟负载监控")

##### 7.1.3.7.4 内存
可以设置内存监控的告警阈值和告警级别，满足条件则进行告警。

[![内存监控](https://file.kacper.fun/mblog/2023/12/17/afa6df62c1ff779fe5f654dabdf6ba48.webp "内存监控")](https://file.kacper.fun/mblog/2023/12/17/afa6df62c1ff779fe5f654dabdf6ba48.webp "内存监控")

##### 7.1.3.7.5 磁盘
可以设置磁盘监控的告警阈值和告警级别，满足条件则进行告警。

[![磁盘监控](https://file.kacper.fun/mblog/2023/12/17/eaa01e699bdbfa6b9a98678bb48987a6.webp "磁盘监控")](https://file.kacper.fun/mblog/2023/12/17/eaa01e699bdbfa6b9a98678bb48987a6.webp "磁盘监控")

#### 7.1.3.8 Docker
##### 7.1.3.8.1 是否告警
Docker 有一个是否监控的开关，打开则监控 Docker，关闭则不监控 Docker。

[![docker监控开关](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "docker监控开关")](https://file.kacper.fun/mblog/2023/12/17/3c772cbe99e18dfd5d1e4c39bfc11773.webp "docker监控开关")

### 7.1.4 告警定义
告警定义用于自定义业务埋点监控，在此处添加自定义告警，然后就可以在埋点监控的时候使用。

[![添加告警定义](https://file.kacper.fun/mblog/2023/12/17/19ab38a66438c4b2c162e1fa9a7c7ea3.webp "添加告警定义")](https://file.kacper.fun/mblog/2023/12/17/19ab38a66438c4b2c162e1fa9a7c7ea3.webp "添加告警定义")

在埋点监控的时候，使用“添加告警定义”中生成的告警编码，则在满足告警条件的时候就会以告警定义中定义的内容发送告警。

```java
// 业务埋点监控
ScheduledExecutorService service = Monitor.buryingPoint(() -> {
  // 假如发现了业务异常，用下面的代码发送告警
  Alarm alarm = new Alarm();
  alarm.setCode("bb5dd07988ecac50");
  alarm.setMonitorType(MonitorTypeEnums.CUSTOM);
  Result result = Monitor.sendAlarm(alarm);
  System.out.println("发送业务告警结果：" + result.toJsonString());
}, 0, 1, TimeUnit.HOURS, ThreadTypeEnums.IO_INTENSIVE_THREAD);
```
## 7.2 业务埋点监控
Java应用程序只要集成了监控客户端，就具有业务埋点监控的能力。
### 7.2.1 业务告警
在需要业务监控的业务代码位置，通过 **Monitor.buryingPoint(Runnable command, long initialDelay, long period, TimeUnit unit, ThreadTypeEnums threadTypeEnum)** 方法定时监控业务运行情况，通过 **Monitor.sendAlarm(Alarm alarm)** 发送告警。可以参考以下示例代码（不局限于此，可以自己灵活使用）：
```java
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
### 7.2.2 异常日志(收费版)
在需要收集异常日志的业务代码位置，通过 **Monitor.collectException(ExceptionInfo exceptionInfo, boolean alarmEnable)** 收集异常日志，此方法有两个入参，第一个入参为 **异常日志信息**，第二个入参为 **是否开启异常日志信息告警**。可以参考以下示例代码（不局限于此，可以自己灵活使用）：
```java
@ExceptionHandler(Exception.class)
public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
    log.error("Requst URL : {}，Exception : {}", request.getRequestURL(), e.getMessage());
    String method = request.getMethod();
    String requestUri = request.getRequestURI();
    // 异常名称
    String excName = e.getClass().getSimpleName();
    // 转换请求参数
    Map<String, Object> reqParamMap = MapUtils.convertParamMap(request.getParameterMap());
    // 封装异常信息
    ExceptionInfo exceptionInfo = ExceptionInfo.builder()
            .excName(excName)
            .excMessage(ExceptionUtils.stackTraceToString(excName, e.getMessage(), e.getStackTrace()))
            .reqParam(reqParamMap.isEmpty() ? "" : JSON.toJSONString(reqParamMap))
            .reqUri(method + " " + requestUri)
            .reqIp(AccessObjectUtils.getClientAddress(request))
            .userId(SpringSecurityUtil.getCurrentUserRealm() == null ? null : SpringSecurityUtil.getCurrentUserRealm().getUserId())
            .username(SpringSecurityUtil.getCurrentUserRealm() == null ? null : SpringSecurityUtil.getCurrentUserRealm().getUsername())
            .build();
    // 收集异常信息
    Monitor.collectException(exceptionInfo, true);
    // 标识了状态码的时候就不拦截
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
        throw e;
    }
    ModelAndView mv = new ModelAndView();
    mv.addObject("url", request.getRequestURL());
    mv.addObject("exception", e);
    mv.setViewName("error/error");
    return mv;
}
```