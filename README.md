## 介绍

**“kacper”** 是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、数据库和网络，通过实时收集、汇聚和分析监控信息，实现在发现异常时立刻推送告警信息，并且提供了可视化系统进行配置、管理、查看。持续更新中...

- 应用程序

  默认支持Java应用程序，监控内容包括：在线状态、业务埋点。其它应用程序需要自己开发客户端，来调用接口与服务端或者代理端通信（心跳接口、服务器信息接口、告警接口）；

- JVM

  监控内容包括：内存、线程、类、GC等；

- 服务器

  支持Windows、Linux，监控内容包括：在线状态、操作系统、CPU、磁盘、内存、网卡、电池、传感器；

- 数据库

  支持MySQL、Oracle，监控内容包括：连接状态、会话，Oracle数据库则还能监控表空间；

- 网络：支持监控网络状态；

- 告警：默认支持电子邮件。

## 设计

- 功能架构

  ![功能导图](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E5%8A%9F%E8%83%BD%E5%AF%BC%E5%9B%BE.png "功能导图")

- 总体架构

  ![总体架构图](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%80%BB%E4%BD%93%E6%9E%B6%E6%9E%84%E5%9B%BE.png "总体架构图")

- 运行环境

  Maven3+  
  Jdk1.8+  
  Lombok  
  Mysql5.7+

- 模块结构

  平台使用Java+layui+echarts开发，数据库采用MySQL。

  monitoring（监控平台父工程）  
  ├── monitoring-common（监控公共模块父工程）  
  │ ├── monitoring-common-core（监控核心公共模块）  
  │ └── monitoring-common-web（监控WEB公共模块）  
  ├── monitoring-client（监控客户端父工程）  
  │ ├── monitoring-client-core（监控客户端）  
  │ ├── monitoring-client-spring-boot-starter（监控客户端与springboot集成的starter）  
  │ └── monitoring-client-spring-mvc-integrator（监控客户端与springmvc集成的integrator）  
  ├── monitoring-agent（监控代理端）  
  ├── monitoring-server（监控服务端）  
  ├── monitoring-ui（监控UI端）  
  └── doc（文档）

  monitoring：监控平台父工程，管理平台的依赖、构建、插件等；  
  monitoring-common：监控公共模块，提供平台所有的公共代码，包含一个监控核心公共模块（monitoring-common-core）和一个监控WEB公共模块（monitoring-common-web）；  
  monitoring-client：监控客户端，用于集成到Java应用程序中实现业务埋点和Java应用程序监控信息收集，包含一个通用模块（monitoring-client-core）和与springboot集成的starter（monitoring-client-spring-boot-starter）、与springmvc集成的integrator（monitoring-client-spring-mvc-integrator）两个拓展模块；  
  monitoring-agent：监控代理端，用于收集服务器信息和汇聚、转发来自监控客户端的信息；  
  monitoring-server：监控服务端，是监控平台的核心模块，用于汇聚、分析监控信息，在发现异常时实时推送告警信息；  
  monitoring-ui：监控可视化系统，用于平台配置、用户管理、监控信息查看、图表展示等；  
  doc：包含平台的设计文档、服务启停脚本、数据库脚本等。

## 下载

- 源码仓库地址

  [https://gitee.com/monitoring-platform/monitoring](https://gitee.com/monitoring-platform/monitoring)

- 示例代码仓库地址

  [https://gitee.com/monitoring-platform/monitoring-example](https://gitee.com/monitoring-platform/monitoring-example)

- 中央仓库地址

1. 客户端为普通Java程序

  ```xml
  <!-- https://mvnrepository.com/artifact/com.gitee.pifeng/monitoring-client-core -->
  <dependency>
      <groupId>com.gitee.pifeng</groupId>
      <artifactId>monitoring-client-core</artifactId>
      <version>${最新稳定版本}</version>
  </dependency>
  ```

2. 客户端为springboot程序

  ```xml
  <!-- https://mvnrepository.com/artifact/com.gitee.pifeng/monitoring-client-spring-boot-starter -->
  <dependency>
      <groupId>com.gitee.pifeng</groupId>
      <artifactId>monitoring-client-spring-boot-starter</artifactId>
      <version>${最新稳定版本}</version>
  </dependency>
  ```

3. 客户端为springmvc程序

  ```xml
  <!-- https://mvnrepository.com/artifact/com.gitee.pifeng/monitoring-client-spring-mvc-integrator -->
  <dependency>
      <groupId>com.gitee.pifeng</groupId>
      <artifactId>monitoring-client-spring-mvc-integrator</artifactId>
      <version>${最新稳定版本}</version>
  </dependency>
  ```

## 使用

### 初始化“监控数据库”

请下载项目源码并解压，进入目录：**/monitoring/doc/数据库设计/sql/mysql** ，找到SQL脚本并执行即可。

```
monitoring.sql
```

### 编译源码

解压源码,按照maven格式将源码导入IDE, 使用maven进行编译即可。

### 配置

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

  |配置项                           |含义                                                               |必须项       |默认值| 
  |--------------------------------|------------------------------------------------------------------|-------------|-----|
  |monitoring.server.url           |监控服务端（代理端）url                                              |是           |      |
  |monitoring.own.instance.order   |实例次序（整数），用于在集群中区分应用实例，配置“1”就代表集群中的第一个应用实例 |否           |1     |
  |monitoring.own.instance.endpoint|实例端点类型（server、agent、client、ui）                             |否           |client|
  |monitoring.own.instance.name    |实例名称，一般为项目名                                                |是           |      |
  |monitoring.own.instance.desc    |实例描述                                                           |否           |      |
  |monitoring.own.instance.language|程序语言                                                           |否           |Java  |
  |monitoring.heartbeat.rate       |与服务端或者代理端发心跳包的频率（秒），最小不能小于30秒                    |否           |30    |
  |monitoring.server-info.enable   |是否采集服务器信息                                                   |否           |false |
  |monitoring.server-info.rate     |与服务端或者代理端发服务器信息包的频率（秒），最小不能小于30秒               |否           |60    |
  |monitoring.server-info.ip       |服务器本机ip地址                                                    |否（自动获取） |      |
  |monitoring.jvm-info.enable      |是否采集Java虚拟机信息                                               |否           |false |
  |monitoring.jvm-info.rate        |与服务端或者代理端发送Java虚拟机信息的频率（秒），最小不能小于30秒           |否           |60    |

1. 监控UI端

    除了监控配置外，还需要在 **application-{profile}.yml** 文件中进行数据库配置。

2. 监控服务端

    除了监控配置外，还需要在 **application-{profile}.yml** 文件中进行数据库配置和邮箱配置。

3. 监控代理端

    只需监控配置。

4. 监控客户端

    只需监控配置。

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

Java应用程序只要集成了监控客户端，就具有业务埋点监控的能力，通过 **Monitor.buryingPoint()** 方法定时监控业务运行情况，通过 **Monitor.sendAlarm()** 发送告警。具体使用实例如下：
  
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

### 打包部署运行

**监控UI端、监控服务端、监控代理端** 直接打成可运行jar，部署后通过脚本（命令）运行。启停脚本在源码中，具体位置在：**/monitoring/doc/bat脚本、/monitoring/doc/shell脚本** 。  
**监控UI端**初始账号/密码：**admin/admin123**，**guest/guest123**。

## 功能截图

![首页1](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E9%A6%96%E9%A1%B51.png "首页1")
  
![首页2](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E9%A6%96%E9%A1%B52.png "首页2")
  
![服务器1](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%9C%8D%E5%8A%A1%E5%99%A81.png "服务器1")

![服务器2](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%9C%8D%E5%8A%A1%E5%99%A82.png "服务器2")
  
![应用程序1](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BA%94%E7%94%A8%E7%A8%8B%E5%BA%8F1.png "应用程序1")
  
![应用程序2](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BA%94%E7%94%A8%E7%A8%8B%E5%BA%8F2.png "应用程序2")

![数据库1](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%95%B0%E6%8D%AE%E5%BA%931.png "数据库1")

![数据库2](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%95%B0%E6%8D%AE%E5%BA%932.png "数据库2")

![网络](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E7%BD%91%E7%BB%9C.png "网络")

![告警设置](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%91%8A%E8%AD%A6%E8%AE%BE%E7%BD%AE.png "告警设置")

![告警记录](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%91%8A%E8%AD%A6%E8%AE%B0%E5%BD%95.png "告警记录")

![操作日志1](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%971.png "操作日志1")

![操作日志2](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%972.png "操作日志2")

![异常日志1](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BC%82%E5%B8%B8%E6%97%A5%E5%BF%971.png "异常日志1")

![异常日志2](https://gitee.com/monitoring-platform/monitoring/raw/master/doc/%E6%88%AA%E5%9B%BE/%E5%BC%82%E5%B8%B8%E6%97%A5%E5%BF%972.png "异常日志2")

