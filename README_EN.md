<div align="center">

# Phoenix Monitoring Platform

**A flexible and configurable open-source monitoring platform integrating monitoring, alerting, and notification**

[![License](https://img.shields.io/github/license/709343767/phoenix?color=blue&logo=gnu&logoColor=white)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![Java](https://img.shields.io/badge/Java-1.8+-brightgreen?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.12.RELEASE-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.gitee.pifeng/phoenix-client-core?color=C71A36&logo=apachemaven&logoColor=white&label=Maven%20Central)](https://mvnrepository.com/artifact/com.gitee.pifeng)
[![GitHub Stars](https://img.shields.io/github/stars/709343767/phoenix?style=flat&logo=github)](https://github.com/709343767/phoenix)
[![Gitee Stars](https://gitee.com/monitoring-platform/phoenix/badge/star.svg?theme=dark)](https://gitee.com/monitoring-platform/phoenix/stargazers)
[![Version](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fgitee.com%2Fapi%2Fv5%2Frepos%2Fmonitoring-platform%2Fphoenix%2Freleases%2Flatest&query=%24.tag_name&label=Version&color=orange)](https://gitee.com/monitoring-platform/phoenix/releases)

[Live Demo](http://124.222.235.43/phoenix-ui/index) |
[WeChat](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&__biz=MzYzMzgzNzQ5OQ==&scene=1&album_id=4442806491825356807&count=3#wechat_redirect) |
[GitCode](https://gitcode.com/monitoring-platform/phoenix) |
[Gitee](https://gitee.com/monitoring-platform/phoenix) |
[GitHub](https://github.com/709343767/phoenix)

[中文](README.md) | English

</div>

---

## 📖 Table of Contents

- [Introduction](#-introduction)
- [Key Features](#-key-features)
- [Feature Comparison](#-feature-comparison)
- [System Architecture](#-system-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Client Integration](#-client-integration)
- [Screenshots](#-screenshots)
- [Technical Blog](#-technical-blog)
- [Live Demo](#-live-demo)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact Us](#-contact-us)
- [Donate](#-donate)
- [Star History](#-star-history)

## 💡 Introduction

**Phoenix** is a flexible and configurable open-source monitoring platform designed for monitoring applications, servers, network devices, Docker, databases, networks, TCP ports, and HTTP interfaces. By collecting, aggregating, and analyzing monitoring data in real time, it pushes alert notifications immediately upon detecting anomalies, and provides a visual system for configuration, management, and viewing.

> Open source does not mean free of restrictions. Secondary development based on Phoenix is allowed, but the logo, name, and copyright must not be modified.

### 🏆 G-Star Certification

Phoenix has passed the **GitCode G-Star Graduation Project Certification**.

<img src="doc/其它/Phoenix GitCode G-Star 毕业项目认证.jpg" alt="G-Star Certification" width="500"/>

## ✨ Key Features

- **🖥️ Full-Stack Monitoring** — One-stop monitoring from applications to infrastructure, see [Feature Comparison](#-feature-comparison) for details
- **⚡ Real-Time Alerting** — Multi-channel notifications + four-level alerting mechanism (INFO/WARN/ERROR/FATAL)
- **🔧 Flexible Configuration** — Flexible alert definition engine with dynamically adjustable monitoring strategies
- **🌐 Distributed Architecture** — Supports cluster deployment; agents can be deployed on jump servers to bridge network barriers
- **🔒 Data Security** — Supports encrypted data transmission to ensure monitoring data security
- **🐳 Containerized Deployment** — One-click deployment with Docker / Docker Compose, installation completed in 10 minutes
- **📱 Multi-Platform Support** — Web interface supports both desktop and mobile devices
- **🔄 Dynamic Thread Pool** — View and modify thread pool parameters at runtime, supporting 7 queue types, 6 rejection policies + SPI extension
- **📡 High Extensibility** — Multi-protocol communication architecture, extensible to monitor programs written in other programming languages

## 🔍 Feature Comparison

Phoenix offers two editions: **Open Source (1.x)** and **Commercial (2.x)**. The open source edition includes core monitoring capabilities, suitable for personal learning and small projects. The commercial edition extends with Docker monitoring, network device management, online diagnostics, dynamic thread pool, slow SQL monitoring, enterprise notification channels, business exception log collection, and other advanced features, suitable for enterprise production environments.

> 💰 Marks indicate commercial edition exclusive paid features.

| Module | Feature | Monitoring Content | Open Source (1.x) | Commercial (2.x) |
|:-------|:--------|:-------------------|:-----------------:|:-----------------:|
| **Server Monitoring** | Server Resource Monitoring | Online status, OS, CPU, load average, memory, disk, NIC, battery, sensors, processes | ✅ | ✅ |
| **Java Application Monitoring** | JVM Monitoring | JVM overview, memory, threads, class loading, GC | ✅ | ✅ |
| | Application Instance Status Monitoring | Online status | ✅ | ✅ |
| | Business Instrumentation Monitoring | Business instrumentation | ✅ | ✅ |
| | Dynamic Thread Pool Monitoring & Management | Thread pool parameter viewing and dynamic modification | ❌ | 💰 |
| | Arthas Online JVM Diagnostics | Online JVM diagnostics | ❌ | 💰 |
| **Network Monitoring** | HTTP Interface Monitoring | Interface availability | ✅ | ✅ |
| | TCP Port Monitoring | Telnet port status | ✅ | ✅ |
| | Ping Network Connectivity Monitoring | Network connectivity | ✅ | ✅ |
| | Network Device SNMP Monitoring | Switches, routers, etc.: online status, system info, interface info | ❌ | 💰 |
| **Database Monitoring** | Database Connection Status Monitoring | MySQL, Oracle, Redis, MongoDB online status and sessions | ✅ | ✅ |
| | Database Slow SQL Monitoring | MySQL slow SQL, Oracle slow SQL and tablespace | ❌ | 💰 |
| **Docker Monitoring** | Full Docker Monitoring | Service status, containers, images, events, resource statistics | ❌ | 💰 |
| **Alert System** | Alert Notification (Email / SMS) | Four-level alerts (INFO/WARN/ERROR/FATAL) | ✅ | ✅ |
| | Alert Notification (DingTalk / WeCom / Feishu) | Multi-channel enterprise notification | ❌ | 💰 |
| | Alert Count Configuration (Failure / Recovery) | Failure and recovery alert count thresholds | ❌ | 💰 |
| **Log Management** | Operation Log Management | Operation log recording, viewing, management | ✅ | ✅ |
| | Exception Log Management | Exception log recording, viewing, management | ✅ | ✅ |
| | Business Exception Log Collection | Client-side exception log collection | ❌ | 💰 |
| **Communication Architecture** | HTTP Communication | Basic HTTP communication | ✅ | ✅ |
| | WebSocket Communication | WebSocket real-time communication | ❌ | 💰 |
| **Visual Interface** | Basic Monitoring Dashboard | Desktop and mobile monitoring panels | ✅ | ✅ |
| | Docker Management Interface | Docker management and monitoring pages | ❌ | 💰 |
| | Network Device Management Interface | Network device management and monitoring pages | ❌ | 💰 |
| | Network Topology Map | Topology map display | ❌ | 💰 |
| | Web Terminal Emulation | Online terminal | ❌ | 💰 |
| | Arthas Diagnostics Interface | Arthas diagnostics operation pages | ❌ | 💰 |

> 📮 **Get Commercial Edition**: To obtain a commercial license or learn more details, please refer to the [《Commercial Pricing Plan》](doc/商用版定价方案.pdf) or contact the author (WeChat: `pifengeclipse`). For more contact methods, see [Contact Us](#-contact-us).

## 🏗️ System Architecture

![System Architecture](doc/系统架构图.png)

## 🛠️ Tech Stack

| Category | Technology |
|:---------|:-----------|
| **Core Framework** | Spring Boot, Netty |
| **Security Framework** | Spring Security, Spring Session |
| **Persistence Layer** | MyBatis-Plus, Alibaba Druid |
| **Task Scheduling** | JUC, Spring Task, Quartz |
| **Server Monitoring** | Sigar / OSHI |
| **Network Device Monitoring** | SNMP4J |
| **Docker Monitoring** | docker-java |
| **JVM Diagnostics** | Alibaba Arthas |
| **Encryption** | Bouncy Castle |
| **Frontend Framework** | Layui, ECharts, jtopo (topology), xterm (terminal emulation) |
| **Utility Libraries** | Hutool, FastJSON, Jackson, Apache Commons (IO/Collections/Net) |
| **Others** | EasyPOI (export), Knife4j (API documentation) |

## 📦 Project Structure

```
phoenix (Monitoring Platform)
├── phoenix-common (Common Modules)
│   ├── phoenix-common-core                   -- Core common: thread pool, data models, utilities, encryption/decryption
│   ├── phoenix-common-netty                  -- Netty communication: WebSocket, persistent connections, reconnection
│   └── phoenix-common-web                    -- Web common: REST API infrastructure
├── phoenix-client (Client SDK)
│   ├── phoenix-client-core                   -- Core: data collection, heartbeat, business instrumentation
│   ├── phoenix-client-spring-boot-starter    -- Spring Boot auto-configuration
│   └── phoenix-client-spring-mvc-integrator  -- Spring MVC adapter
├── phoenix-agent                             -- Agent: server/Docker/SNMP data collection
├── phoenix-server                            -- Server: data processing, alert engine, configuration distribution
├── phoenix-ui                                -- Visual UI: web management interface
├── doc                                       -- Documentation: architecture diagrams, screenshots, DB scripts, deployment scripts, technical blog
└── mvn                                       -- Maven packaging scripts
```

## 🚀 Quick Start

> One-click installation: complete automated setup in **10 minutes**  
> Minimum resource requirements: **1 CPU core / 2 GB RAM / 5 GB disk**

### Docker Installation (Recommended)

Software dependency: Docker 20.10.14+

```bash
bash -c "$(curl -fsSL https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/install.sh)"
```

### Docker Compose Installation

Software dependency: Docker Compose 2.0.0+

```bash
bash -c "$(curl -fsSL https://gitee.com/monitoring-platform/phoenix/raw/master/doc/DockerCompose/install.sh)"
```

### Install Agent (Required for Server Monitoring)

```bash
bash -c "$(curl -fsSL https://gitee.com/monitoring-platform/phoenix/raw/master/doc/Docker/phoenix-agent/install_agent.sh)"
```

### Configuration Guide

**phoenix-server configuration:**
1. Copy `application-prod.yml` from the source directory `phoenix-server/src/main/resources/`
2. Place it in `/data/phoenix/phoenix-server/config/`
3. Edit the file to configure email and other alert channels
4. Restart the phoenix-server container

**phoenix-agent configuration:**
1. Copy `monitoring-prod.properties` from the source directory `phoenix-agent/src/main/resources/`
2. Place it in `/data/phoenix/phoenix-agent/config/`
3. Set `monitoring.comm.http.url` to `http://<server-ip>:16000/phoenix-server`
4. Restart the phoenix-agent container

After installation, visit: `http://<ui-ip>/phoenix-ui/index`, default accounts: `admin / admin123`, `guest / guest123`

## 📥 Client Integration

Latest stable version on Maven Central: **1.2.7.RELEASE**

**Plain Java application:**
```xml
<dependency>
    <groupId>com.gitee.pifeng</groupId>
    <artifactId>phoenix-client-core</artifactId>
    <version>1.2.7.RELEASE</version>
</dependency>
```

**Spring Boot application:**
```xml
<dependency>
    <groupId>com.gitee.pifeng</groupId>
    <artifactId>phoenix-client-spring-boot-starter</artifactId>
    <version>1.2.7.RELEASE</version>
</dependency>
```

**Spring MVC application:**
```xml
<dependency>
    <groupId>com.gitee.pifeng</groupId>
    <artifactId>phoenix-client-spring-mvc-integrator</artifactId>
    <version>1.2.7.RELEASE</version>
</dependency>
```

## 📸 Screenshots

<details>
<summary><b>🏠 Home Overview</b></summary>
<br/>

![Home 1](doc/截图/首页1.png)
![Home 2](doc/截图/首页2.png)
![Home 3](doc/截图/首页3.png)
</details>

<details>
<summary><b>🖥️ Server Monitoring</b></summary>
<br/>

![Server 1](doc/截图/服务器1.png)
![Server 2](doc/截图/服务器2.png)
![Server 3](doc/截图/服务器3.png)
</details>

<details>
<summary><b>☕ Application Monitoring</b></summary>
<br/>

![Application 1](doc/截图/应用程序1.png)
![Application 2](doc/截图/应用程序2.png)
![Application 3](doc/截图/应用程序3.png)
</details>

<details>
<summary><b>🗄️ Database Monitoring</b></summary>
<br/>

![Database 1](doc/截图/数据库1.png)
![Database 2](doc/截图/数据库2.png)
![Database 3](doc/截图/数据库3.png)
![Database 4](doc/截图/数据库4.png)
</details>

<details>
<summary><b>🌐 Network / TCP / HTTP Monitoring</b></summary>
<br/>

![Network 1](doc/截图/网络1.png)
![Network 2](doc/截图/网络2.png)
![TCP 1](doc/截图/TCP1.png)
![TCP 2](doc/截图/TCP2.png)
![HTTP 1](doc/截图/HTTP1.png)
![HTTP 2](doc/截图/HTTP2.png)
![HTTP 3](doc/截图/HTTP3.png)
</details>

<details>
<summary><b>🐳 Docker Monitoring</b></summary>
<br/>

![Docker 1](doc/截图/DOCKER1.png)
![Docker 2](doc/截图/DOCKER2.png)
![Docker 3](doc/截图/DOCKER3.png)
![Docker 4](doc/截图/DOCKER4.png)
![Docker 5](doc/截图/DOCKER5.png)
</details>

<details>
<summary><b>🗺️ Topology Map</b></summary>
<br/>

![Topology 1](doc/截图/拓扑图1.png)
![Topology 2](doc/截图/拓扑图2.png)
![Topology 3](doc/截图/拓扑图3.png)
![Topology 4](doc/截图/拓扑图4.png)
![Topology 5](doc/截图/拓扑图5.png)
</details>

<details>
<summary><b>🔔 Alert Management</b></summary>
<br/>

![Alert 1](doc/截图/告警1.png)
![Alert 2](doc/截图/告警2.png)
![Alert Definition](doc/截图/告警定义.png)
</details>

<details>
<summary><b>⚙️ System Management</b></summary>
<br/>

![User Management](doc/截图/用户管理.png)
![Operation Log 1](doc/截图/操作日志1.png)
![Operation Log 2](doc/截图/操作日志2.png)
![Exception Log 1](doc/截图/异常日志1.png)
![Exception Log 2](doc/截图/异常日志2.png)
![Monitoring Configuration](doc/截图/监控配置.png)
</details>

## 📚 Technical Blog

Over **60+** in-depth technical articles (continuously updated), covering architecture design and core implementation:

| Series | Count | Content |
|:-------|:-----:|:--------|
| Communication & Data Security | 7 | Overview, HTTP communication, WebSocket communication, Netty server, Netty client, encryption/decryption, data compression |
| Client SDK | 8 | Monitor entry, heartbeat mechanism, configuration loading, JVM collection, thread pool collection, Spring Boot integration, Spring MVC integration, business instrumentation |
| Agent | 7 | Overall architecture, server collection (Part 1), server collection (Part 2), Docker collection, Docker Stats, SNMP collection, data forwarding |
| Server Core | 10 | Data reception, parallel processing, instance management, server monitoring, Docker monitoring, database monitoring, slow SQL detection, network monitoring, distributed lock, real-time monitoring table |
| Alert Engine | 7 | Alert definition, trigger flow, email channel, DingTalk channel, WeCom channel, Feishu channel, alert convergence |
| UI & Security | 7 | UI architecture, Spring Security authentication, CAS SSO, CAPTCHA, logging, frontend interaction, topology map |
| Thread Pool Monitoring | 3 | Registration system, dynamic parameter tuning, historical trends |
| Common Modules & Engineering | 4 | DTO system, Domain model, HTTP encryption/decryption aspect, Maven multi-module management |
| Deployment & Operations | 4 | Docker deployment, Docker Compose, service deployment, cluster high availability |
| Database Design | 3 | Table domain partitioning & index strategy, real-time + historical dual-table pattern, Quartz scheduling tables |

👉 Read the blog: [Personal Blog](https://kacper.fun/types/28) | [WeChat Official Account](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&__biz=MzYzMzgzNzQ5OQ==&scene=1&album_id=4442806491825356807&count=3#wechat_redirect)

👉 Scan to follow the WeChat Official Account for more technical content

<img src="doc/其它/wx_gzh.jpg" width="300" />

## 🖥️ Live Demo

| Demo URL | Account | Password |
|:---------|:--------|:---------|
| [http://124.222.235.43/phoenix-ui/index](http://124.222.235.43/phoenix-ui/index) | guest | guest123 |

> **Note:** The demo environment provides view-only access, and access speed is limited by server bandwidth.

## 🤝 Contributing

We welcome suggestions to help improve Phoenix!

1. Fork this repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Submit a Pull Request

💡 [Changelog](https://gitee.com/monitoring-platform/phoenix/wikis/pages?sort_id=4420016&doc_id=935794)

## 📄 License

[![License](https://img.shields.io/badge/License-GNU%20General%20Public%20License%20v3.0-blue)](https://www.gnu.org/licenses/gpl-3.0.txt)

This project is open-sourced under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.txt) license. Please comply with the license requirements.

## 📬 Contact Us

| Method | Info |
|:-------|:-----|
| WeChat | `pifengeclipse` |
| QQ | `709343767` |
| QQ Group | [![Join QQ Group](https://img.shields.io/badge/QQ群-773127639-blue)](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=qEfrXWBpzslfYc9QrTQJwx1Kty0sC3Ee&authKey=0GQGNvA5G0SLWZgRyn9EjX3Uta0WLAGuzSmrGqNWAnaUTU5LzRTZovcB2ivgE%2BFm&noverify=0&group_code=773127639) |

## 💰 Donate

If Phoenix has been helpful to you, feel free to donate to support the project's continued development!

<img src="doc/其它/donate.jpg" alt="Donate" width="400"/>

## ⭐ Star History

[![Star History Chart](https://api.star-history.com/svg?repos=709343767/phoenix&type=Date)](https://star-history.com/#709343767/phoenix&Date)