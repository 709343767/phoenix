<div align="center">

# Phoenix 监控平台

**灵活可配置的开源监控平台，集监控、告警、通知于一体**

[![License](https://img.shields.io/github/license/709343767/phoenix?color=blue&logo=gnu&logoColor=white)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![Java](https://img.shields.io/badge/Java-1.8+-brightgreen?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.12.RELEASE-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.gitee.pifeng/phoenix-client-core?color=C71A36&logo=apachemaven&logoColor=white&label=Maven%20Central)](https://mvnrepository.com/artifact/com.gitee.pifeng)
[![GitHub Stars](https://img.shields.io/github/stars/709343767/phoenix?style=flat&logo=github)](https://github.com/709343767/phoenix)
[![Gitee Stars](https://gitee.com/monitoring-platform/phoenix/badge/star.svg?theme=dark)](https://gitee.com/monitoring-platform/phoenix/stargazers)
[![Version](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fgitee.com%2Fapi%2Fv5%2Frepos%2Fmonitoring-platform%2Fphoenix%2Freleases%2Flatest&query=%24.tag_name&label=Version&color=orange)](https://gitee.com/monitoring-platform/phoenix/releases)

[在线演示](http://124.222.235.43/phoenix-ui/index) |
[微信公众号](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&__biz=MzYzMzgzNzQ5OQ==&scene=1&album_id=4442806491825356807&count=3#wechat_redirect) |
[GitCode](https://gitcode.com/monitoring-platform/phoenix) |
[Gitee](https://gitee.com/monitoring-platform/phoenix) |
[GitHub](https://github.com/709343767/phoenix)

中文 | [English](README_EN.md)

</div>

---

## 📖 目录

- [项目简介](#-项目简介)
- [核心特性](#-核心特性)
- [功能对比](#-功能对比)
- [系统架构](#-系统架构)
- [技术栈](#-技术栈)
- [工程结构](#-工程结构)
- [快速开始](#-快速开始)
- [客户端接入](#-客户端接入)
- [功能截图](#-功能截图)
- [技术博客](#-技术博客)
- [在线演示](#-在线演示)
- [贡献指南](#-贡献指南)
- [版权许可](#-版权许可)
- [联系我们](#-联系我们)
- [捐赠支持](#-捐赠支持)
- [Star 历史](#-star-历史)

## 💡 项目简介

**Phoenix** 是一个灵活可配置的开源监控平台，主要用于监控应用程序、服务器、网络设备、Docker、数据库、网络、TCP端口和HTTP接口。通过实时收集、汇聚和分析监控信息，实现在发现异常时立刻推送告警信息，并且提供了可视化系统进行配置、管理和查看。

> 开源不等同于免费，允许基于 Phoenix 进行二次开发，但不得修改 logo、名称、版权等。

### 🏆 G-Star 认证

Phoenix 已通过 **GitCode G-Star 毕业项目认证**。

<img src="doc/其它/Phoenix GitCode G-Star 毕业项目认证.jpg" alt="G-Star认证" width="500"/>

## ✨ 核心特性

- **🖥️ 全栈监控** — 从应用到基础设施的一站式监控，详见[功能对比](#-功能对比)
- **⚡ 实时告警** — 多渠道通知 + 四级告警机制（INFO/WARN/ERROR/FATAL）
- **🔧 灵活配置** — 灵活的告警定义引擎，监控策略可动态调整
- **🌐 分布式架构** — 支持集群部署，代理端可部署于跳板机打通网络壁垒
- **🔒 数据安全** — 支持数据加密传输，保障监控数据安全
- **🐳 容器化部署** — Docker / Docker Compose 一键部署，10分钟完成安装
- **📱 多端支持** — Web界面同时支持PC端和移动端
- **🔄 动态线程池** — 运行时查看和修改线程池参数，支持7种队列类型、6种拒绝策略 + SPI扩展
- **📡 拓展性强** — 多协议通信架构，可拓展监控其它编程语言编写的程序

## 🔍 功能对比

Phoenix 提供两个版本：**开源版（1.x）** 和 **商用版（2.x）**。开源版包含核心监控能力，适合个人学习和小型项目；商用版在此基础上扩展了 Docker 监控、网络设备管理、在线诊断、动态线程池、慢SQL监控、企业通知渠道、业务异常日志采集等高级功能，适合企业级生产环境。

> 💰 标记表示商用版专属收费功能。

| 功能模块 | 功能 | 监控内容 | 开源版(1.x) | 商用版(2.x) |
|:---------|:-----|:---------|:----------:|:----------:|
| **服务器监控** | 服务器资源监控 | 在线状态、操作系统、CPU、平均负载、内存、磁盘、网卡、电池、传感器、进程 | ✅ | ✅ |
| **Java应用监控** | JVM监控 | JVM概要、内存、线程、类加载、GC | ✅ | ✅ |
| | 应用实例状态监控 | 在线状态 | ✅ | ✅ |
| | 业务埋点监控 | 业务埋点 | ✅ | ✅ |
| | 动态线程池监控与管理 | 线程池参数查看与动态修改 | ❌ | 💰 |
| | Arthas在线JVM诊断 | JVM在线诊断 | ❌ | 💰 |
| **网络监控** | HTTP接口监控 | 接口可用性 | ✅ | ✅ |
| | TCP端口监控 | Telnet端口状态 | ✅ | ✅ |
| | Ping网络连通性监控 | 网络连通性 | ✅ | ✅ |
| | 网络设备SNMP监控 | 交换机、路由器等：在线状态、系统信息、接口信息 | ❌ | 💰 |
| **数据库监控** | 数据库连接状态监控 | MySQL、Oracle、Redis、MongoDB在线状态与会话 | ✅ | ✅ |
| | 数据库慢SQL监控 | MySQL慢SQL、Oracle慢SQL与表空间 | ❌ | 💰 |
| **Docker监控** | Docker完整监控 | 服务状态、容器、镜像、事件、资源统计 | ❌ | 💰 |
| **告警系统** | 告警通知（邮件 / 短信） | 四级告警（INFO/WARN/ERROR/FATAL） | ✅ | ✅ |
| | 告警通知（钉钉 / 企业微信 / 飞书） | 多渠道企业通知 | ❌ | 💰 |
| | 告警次数配置（故障 / 恢复） | 故障与恢复告警次数阈值 | ❌ | 💰 |
| **日志管理** | 操作日志管理 | 操作日志记录、查看、管理 | ✅ | ✅ |
| | 异常日志管理 | 异常日志记录、查看、管理 | ✅ | ✅ |
| | 业务异常日志采集 | 客户端异常日志采集 | ❌ | 💰 |
| **通信架构** | HTTP通信 | 基础HTTP通信 | ✅ | ✅ |
| | WebSocket通信 | WebSocket实时通信 | ❌ | 💰 |
| **可视化界面** | 基础监控仪表盘 | PC端和移动端监控面板 | ✅ | ✅ |
| | Docker管理界面 | Docker管理与监控页面 | ❌ | 💰 |
| | 网络设备管理界面 | 网络设备管理与监控页面 | ❌ | 💰 |
| | 网络拓扑图 | 拓扑图展示 | ❌ | 💰 |
| | Web终端仿真 | 在线终端 | ❌ | 💰 |
| | Arthas诊断界面 | Arthas诊断操作页面 | ❌ | 💰 |

> 📮 **获取商业版**：如需获取商业版授权或了解更多详情，请查看[《商用版定价方案》](doc/商用版定价方案.pdf)或联系作者（微信：`pifengeclipse`），更多联系方式详见[联系我们](#-联系我们)。

## 🏗️ 系统架构

![系统架构图](doc/系统架构图.png)

## 🛠️ 技术栈

| 类别 | 技术 |
|:-----|:-----|
| **核心框架** | Spring Boot 、Netty |
| **安全框架** | Spring Security 、Spring Session |
| **持久层** | MyBatis-Plus 、Alibaba Druid |
| **任务调度** | JUC 、Spring Task 、Quartz |
| **服务器监控** | Sigar / OSHI |
| **网络设备监控** | SNMP4J |
| **Docker监控** | docker-java |
| **JVM诊断** | Alibaba Arthas |
| **加密** | Bouncy Castle |
| **前端框架** | Layui 、ECharts 、jtopo（拓扑图）、xterm（终端仿真） |
| **工具库** | Hutool 、FastJSON 、Jackson 、Apache Commons（IO/Collections/Net） |
| **其他** | EasyPOI（导出）、Knife4j（API文档）|

## 📦 工程结构

```
phoenix（监控平台）
├── phoenix-common（公共模块）
│   ├── phoenix-common-core                   -- 核心公共：线程池、数据模型、工具类、加解密
│   ├── phoenix-common-netty                  -- Netty通信：WebSocket、长连接、断线重连
│   └── phoenix-common-web                    -- Web公共：REST API 基础设施
├── phoenix-client（客户端SDK）
│   ├── phoenix-client-core                   -- 核心：数据收集、心跳、业务埋点
│   ├── phoenix-client-spring-boot-starter    -- Spring Boot 自动配置
│   └── phoenix-client-spring-mvc-integrator  -- Spring MVC 适配
├── phoenix-agent                             -- 代理端：服务器/Docker/SNMP 数据采集
├── phoenix-server                            -- 服务端：数据处理、告警引擎、配置下发
├── phoenix-ui                                -- 可视化UI：Web 管理界面
├── doc                                       -- 文档：架构图、截图、数据库脚本、部署脚本、技术博客
└── mvn                                       -- Maven打包脚本
```

## 🚀 快速开始

> 一键安装：**10 分钟**即可完成自动安装  
> 最低资源需求：**1核 CPU / 2 GB 内存 / 5 GB 磁盘**

### Docker 安装（推荐）

软件依赖：Docker 20.10.14+

```bash
bash -c "$(curl -fsSL https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/install.sh)"
```

### Docker Compose 安装

软件依赖：Docker Compose 2.0.0+

```bash
bash -c "$(curl -fsSL https://gitee.com/monitoring-platform/phoenix/raw/master/doc/DockerCompose/install.sh)"
```

### 安装 Agent（监控服务器时需要）

```bash
bash -c "$(curl -fsSL https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/phoenix-agent/install_agent.sh)"
```

### 配置说明

**phoenix-server 配置：**
1. 从源码目录 `phoenix-server/src/main/resources/` 复制 `application-prod.yml`
2. 放置于 `/data/phoenix/phoenix-server/config/`
3. 编辑该文件，配置邮件等告警渠道
4. 重启 phoenix-server 容器

**phoenix-agent 配置：**
1. 从源码目录 `phoenix-agent/src/main/resources/` 复制 `monitoring-prod.properties`
2. 放置于 `/data/phoenix/phoenix-agent/config/`
3. 将 `monitoring.comm.http.url` 配置为 `http://<server-ip>:16000/phoenix-server`
4. 重启 phoenix-agent 容器

安装完成后访问：`http://<ui-ip>/phoenix-ui/index`，初始账号 `admin / admin123`、`guest / guest123`

## 📥 客户端接入

Maven Central 最新稳定版本：**1.2.7.RELEASE**

**普通 Java 程序：**
```xml
<dependency>
    <groupId>com.gitee.pifeng</groupId>
    <artifactId>phoenix-client-core</artifactId>
    <version>1.2.7.RELEASE</version>
</dependency>
```

**Spring Boot 程序：**
```xml
<dependency>
    <groupId>com.gitee.pifeng</groupId>
    <artifactId>phoenix-client-spring-boot-starter</artifactId>
    <version>1.2.7.RELEASE</version>
</dependency>
```

**Spring MVC 程序：**
```xml
<dependency>
    <groupId>com.gitee.pifeng</groupId>
    <artifactId>phoenix-client-spring-mvc-integrator</artifactId>
    <version>1.2.7.RELEASE</version>
</dependency>
```

## 📸 功能截图

<details>
<summary><b>🏠 首页总览</b></summary>
<br/>

![首页1](doc/截图/首页1.png)
![首页2](doc/截图/首页2.png)
![首页3](doc/截图/首页3.png)
</details>

<details>
<summary><b>🖥️ 服务器监控</b></summary>
<br/>

![服务器1](doc/截图/服务器1.png)
![服务器2](doc/截图/服务器2.png)
![服务器3](doc/截图/服务器3.png)
</details>

<details>
<summary><b>☕ 应用程序监控</b></summary>
<br/>

![应用程序1](doc/截图/应用程序1.png)
![应用程序2](doc/截图/应用程序2.png)
![应用程序3](doc/截图/应用程序3.png)
</details>

<details>
<summary><b>🗄️ 数据库监控</b></summary>
<br/>

![数据库1](doc/截图/数据库1.png)
![数据库2](doc/截图/数据库2.png)
![数据库3](doc/截图/数据库3.png)
![数据库4](doc/截图/数据库4.png)
</details>

<details>
<summary><b>🌐 网络 / TCP / HTTP 监控</b></summary>
<br/>

![网络1](doc/截图/网络1.png)
![网络2](doc/截图/网络2.png)
![TCP1](doc/截图/TCP1.png)
![TCP2](doc/截图/TCP2.png)
![HTTP1](doc/截图/HTTP1.png)
![HTTP2](doc/截图/HTTP2.png)
![HTTP3](doc/截图/HTTP3.png)
</details>

<details>
<summary><b>🐳 Docker 监控</b></summary>
<br/>

![DOCKER1](doc/截图/DOCKER1.png)
![DOCKER2](doc/截图/DOCKER2.png)
![DOCKER3](doc/截图/DOCKER3.png)
![DOCKER4](doc/截图/DOCKER4.png)
![DOCKER5](doc/截图/DOCKER5.png)
</details>

<details>
<summary><b>🗺️ 拓扑图</b></summary>
<br/>

![拓扑图1](doc/截图/拓扑图1.png)
![拓扑图2](doc/截图/拓扑图2.png)
![拓扑图3](doc/截图/拓扑图3.png)
![拓扑图4](doc/截图/拓扑图4.png)
![拓扑图5](doc/截图/拓扑图5.png)
</details>

<details>
<summary><b>🔔 告警管理</b></summary>
<br/>

![告警1](doc/截图/告警1.png)
![告警2](doc/截图/告警2.png)
![告警定义](doc/截图/告警定义.png)
</details>

<details>
<summary><b>⚙️ 系统管理</b></summary>
<br/>

![用户管理](doc/截图/用户管理.png)
![操作日志1](doc/截图/操作日志1.png)
![操作日志2](doc/截图/操作日志2.png)
![异常日志1](doc/截图/异常日志1.png)
![异常日志2](doc/截图/异常日志2.png)
![监控配置](doc/截图/监控配置.png)
</details>

## 📚 技术博客

共 **60+** 篇深度技术文章（持续更新中），涵盖架构设计与核心实现：

| 系列 | 篇数 | 内容 |
|:-----|:----:|:-----|
| 通信与数据安全 | 7 | 全景概览、HTTP通信、WebSocket通信、Netty服务端、Netty客户端、加解密、数据压缩 |
| 客户端SDK | 8 | Monitor入口、心跳机制、配置加载、JVM采集、线程池采集、Spring Boot集成、Spring MVC集成、业务埋点 |
| 代理端 | 7 | 整体架构、服务器采集（上）、服务器采集（下）、Docker采集、Docker Stats、SNMP采集、数据转发 |
| 服务端核心 | 10 | 数据接收、并行处理、实例管理、服务器监控、Docker监控、数据库监控、慢SQL检测、网络监测、分布式锁、实时监控表 |
| 告警引擎 | 7 | 告警定义、触发流程、邮件通道、钉钉通道、企业微信通道、飞书通道、告警收敛 |
| UI端与安全 | 7 | UI架构、SpringSecurity认证、CAS单点登录、验证码、日志、前端交互、拓扑图 |
| 线程池监控 | 3 | 注册体系、动态调参、历史趋势 |
| 公共模块与工程化 | 4 | DTO体系、Domain模型、HTTP加解密切面、Maven多模块管理 |
| 部署与运维 | 4 | Docker部署、Docker Compose、服务化部署、集群高可用 |
| 数据库设计 | 3 | 表分域与索引策略、实时+历史双表模式、Quartz调度表 |

👉 阅读博客：[个人博客](https://kacper.fun/types/28) | [微信公众号](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&__biz=MzYzMzgzNzQ5OQ==&scene=1&album_id=4442806491825356807&count=3#wechat_redirect)

👉 扫码关注微信公众号，获取更多技术干货

<img src="doc/其它/wx_gzh.jpg" width="300" />

## 🖥️ 在线演示

| 演示地址 | 账号 | 密码 |
|:---------|:-----|:-----|
| [http://124.222.235.43/phoenix-ui/index](http://124.222.235.43/phoenix-ui/index) | guest | guest123 |

> **提示：** 演示环境仅提供查看权限，访问速度受限于服务器带宽。

## 🤝 贡献指南

欢迎提出更好的意见，帮助完善 Phoenix！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/your-feature`)
3. 提交更改 (`git commit -m 'Add your feature'`)
4. 推送到分支 (`git push origin feature/your-feature`)
5. 提交 Pull Request

💡 [升级日志](https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=4420016&doc_id=935794)

## 📄 版权许可

[![License](https://img.shields.io/badge/License-GNU%20General%20Public%20License%20v3.0-blue)](https://www.gnu.org/licenses/gpl-3.0.txt)

本项目基于 [GPLv3](https://www.gnu.org/licenses/gpl-3.0.txt) 协议开源，请遵守协议要求。

## 📬 联系我们

| 方式 | 信息 |
|:-----|:-----|
| 作者微信 | `pifengeclipse` |
| 作者QQ | `709343767` |
| QQ交流群 | [![加入QQ群](https://img.shields.io/badge/QQ群-773127639-blue)](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=qEfrXWBpzslfYc9QrTQJwx1Kty0sC3Ee&authKey=0GQGNvA5G0SLWZgRyn9EjX3Uta0WLAGuzSmrGqNWAnaUTU5LzRTZovcB2ivgE%2BFm&noverify=0&group_code=773127639) |

## 💰 捐赠支持

如果 Phoenix 对你有帮助，欢迎捐赠支持项目持续发展！

<img src="doc/其它/donate.jpg" alt="捐赠" width="400"/>

## ⭐ Star 历史

### GitHub

[![Star History Chart](https://api.star-history.com/svg?repos=709343767/phoenix&type=Date)](https://star-history.com/#709343767/phoenix&Date)

### GitCode

[![Stargazers over time](https://gitcode.com/monitoring-platform/phoenix/starcharts.svg?variant=adaptive)](https://gitcode.com/monitoring-platform/phoenix)