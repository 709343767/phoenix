## 介绍

**“phoenix”** 是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、数据库、网络、tcp端口和http接口，通过实时收集、汇聚和分析监控信息，实现在发现异常时立刻推送告警信息，并且提供了可视化系统进行配置、管理、查看。

- 应用程序

  默认支持Java应用程序，监控内容包括：在线状态、JVM、业务埋点。其它应用程序需要自己开发客户端，来调用接口与服务端或者代理端通信（心跳接口、服务器信息接口、告警接口）；

- JVM

  监控内容包括：内存、线程、类、GC等；

- 服务器

  支持主流服务器，如Linux、Windows、macOS、Unix等；    
  监控内容包括：在线状态、操作系统、CPU、平均负载、进程、磁盘、内存、网卡、电池、传感器；

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
  Jdk >=1.8，若使用Sigar监控服务器，则Jdk版本要用1.8(1.8.0_131到1.8.0_241)   
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

  1.2.4.RELEASE-CR1 
  
## 演示地址

[https://49.234.23.142/phoenix-ui/index](https://49.234.23.142/phoenix-ui/index)   
注意：演示项目只提供非管理员账号，只有查看权限！ 

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
作者QQ：709343767  
QQ群：[773127639](https://qm.qq.com/cgi-bin/qm/qr?k=a0yY8EZMVTwvt8Tc1uWuk2hGpvhnyp3C&authKey=nvLNq0pw1yo32ZxbW8rxkYa6yyDn4Vc7f4R65CiifQ+RAgyWXuhszxIKSCB+eb5q&noverify=0)  

为了更好的促进平台发展，捐赠至少**50元**联系作者或者捐赠至少**50元**入群获取《phoenix云监控平台使用说明书》！  

## 捐赠

> [捐赠记录,感谢你们的支持！](https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=5547585&doc_id=935794)
 
![捐赠](https://gitee.com/monitoring-platform/phoenix/raw/master/doc/%E5%85%B6%E5%AE%83/donate.jpg "捐赠")

