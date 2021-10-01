/*
 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 14/07/2021 17:24:18
*/
CREATE database if NOT EXISTS `monitoring` default character set utf8mb4 collate utf8mb4_general_ci;

use `monitoring`;

SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for MONITOR_ALARM_DEFINITION
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ALARM_DEFINITION`;
CREATE TABLE `MONITOR_ALARM_DEFINITION`
(
    `ID`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `TYPE`         varchar(8)   NOT NULL COMMENT '告警类型（SERVER、NET、DATABASE、INSTANCE、CUSTOM）',
    `FIRST_CLASS`  varchar(255) NOT NULL COMMENT '一级分类',
    `SECOND_CLASS` varchar(255) DEFAULT NULL COMMENT '二级分类',
    `THIRD_CLASS`  varchar(255) DEFAULT NULL COMMENT '三级分类',
    `GRADE`        varchar(5)   NOT NULL COMMENT '告警级别（INFO、WARN、ERROR、FATAL）',
    `CODE`         varchar(32)   NOT NULL COMMENT '告警编码',
    `TITLE`        varchar(125) NOT NULL COMMENT '告警标题',
    `CONTENT`      varchar(255) NOT NULL COMMENT '告警内容',
    `INSERT_TIME`  datetime     NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`  datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_CODE` (`CODE`) USING BTREE COMMENT '索引_告警编码'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='告警定义表';

-- ----------------------------
-- Table structure for MONITOR_ALARM_RECORD
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ALARM_RECORD`;
CREATE TABLE `MONITOR_ALARM_RECORD`
(
    `ID`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `CODE`           varchar(36)  NOT NULL COMMENT '告警代码，使用UUID',
    `ALARM_DEF_CODE` varchar(36)  DEFAULT NULL COMMENT '告警定义编码',
    `TYPE`           varchar(8)   NOT NULL COMMENT '告警类型（SERVER、NET、INSTANCE、DATABASE、CUSTOM）',
    `WAY`            varchar(10)  NOT NULL COMMENT '告警方式（SMS、MAIL、...）',
    `LEVEL`          varchar(5)   NOT NULL COMMENT '告警级别（INFO、WARM、ERROR、FATAL）',
    `TITLE`          varchar(125) DEFAULT NULL COMMENT '告警标题',
    `CONTENT`        longtext     DEFAULT NULL COMMENT '告警内容',
    `STATUS`         varchar(1)   DEFAULT NULL COMMENT '告警发送状态（0：失败；1：成功）',
    `NUMBER`         varchar(500) DEFAULT NULL COMMENT '被告警人号码（手机号码、电子邮箱、...）',
    `INSERT_TIME`    datetime     NOT NULL COMMENT '告警时间',
    `UPDATE_TIME`    datetime     DEFAULT NULL COMMENT '告警结果获取时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_CODE` (`CODE`) USING BTREE COMMENT '索引_告警记录编码',
    KEY `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='告警记录表';

-- ----------------------------
-- Table structure for MONITOR_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_CONFIG`;
CREATE TABLE `MONITOR_CONFIG`
(
    `ID`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `VALUE`       text       NOT NULL COMMENT '配置内容（json字符串）',
    `INSERT_TIME` datetime   NOT NULL COMMENT '插入时间',
    `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='监控配置表';

-- ----------------------------
-- Table structure for MONITOR_DB
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_DB`;
CREATE TABLE `MONITOR_DB`
(
    `ID`           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `CONN_NAME`    varchar(255)  NOT NULL COMMENT '连接名称',
    `URL`          varchar(1000) NOT NULL COMMENT '数据库URL',
    `USERNAME`     varchar(50)   NOT NULL COMMENT '用户名',
    `PASSWORD`     varchar(255)  NOT NULL COMMENT '密码',
    `DB_TYPE`      varchar(50)   NOT NULL COMMENT '数据库类型',
    `DRIVER_CLASS` varchar(255)  NOT NULL COMMENT '驱动类',
    `DB_DESC`      varchar(1000) DEFAULT NULL COMMENT '描述',
    `IS_ONLINE`    varchar(255)  DEFAULT NULL COMMENT '数据库状态（0：离线，1：在线）',
    `INSERT_TIME`  datetime      NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='数据库表';

-- ----------------------------
-- Table structure for MONITOR_INSTANCE
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_INSTANCE`;
CREATE TABLE `MONITOR_INSTANCE`
(
    `ID`               bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`      varchar(32) NOT NULL COMMENT '应用实例ID',
    `ENDPOINT`         varchar(8)  NOT NULL COMMENT '端点（客户端<client>、代理端<agent>、服务端<server>、UI端<ui>）',
    `INSTANCE_NAME`    varchar(50) NOT NULL COMMENT '应用实例名',
    `INSTANCE_DESC`    varchar(255) DEFAULT NULL COMMENT '应用实例描述',
    `INSTANCE_SUMMARY` varchar(500) DEFAULT NULL COMMENT '应用实例摘要',
    `LANGUAGE`         varchar(10) NOT NULL COMMENT '编程语言',
    `APP_SERVER_TYPE`  varchar(32)  DEFAULT NULL COMMENT '应用服务器类型',
    `IP`               varchar(15) NOT NULL COMMENT 'IP地址',
    `IS_ONLINE`        varchar(1)   DEFAULT NULL COMMENT '应用状态（0：离线，1：在线）',
    `CONN_FREQUENCY`   int(8)      NOT NULL COMMENT '连接频率',
    `INSERT_TIME`      datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`      datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='应用实例表';

-- ----------------------------
-- Table structure for MONITOR_JVM_CLASS_LOADING
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_CLASS_LOADING`;
CREATE TABLE `MONITOR_JVM_CLASS_LOADING`
(
    `ID`                       bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`              varchar(32) NOT NULL COMMENT '应用实例ID',
    `TOTAL_LOADED_CLASS_COUNT` int(8)     DEFAULT NULL COMMENT '加载的类的总数',
    `LOADED_CLASS_COUNT`       int(8)     DEFAULT NULL COMMENT '当前加载的类的总数',
    `UNLOADED_CLASS_COUNT`     int(8)     DEFAULT NULL COMMENT '卸载的类总数',
    `IS_VERBOSE`               varchar(1) DEFAULT NULL COMMENT '是否启用了类加载系统的详细输出',
    `INSERT_TIME`              datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`              datetime   DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    CONSTRAINT `MONITOR_JVM_CLASS_LOADING_INSTANCE_FK` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `MONITOR_INSTANCE` (`INSTANCE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='java虚拟机类加载信息表';

-- ----------------------------
-- Table structure for MONITOR_JVM_GARBAGE_COLLECTOR
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_GARBAGE_COLLECTOR`;
CREATE TABLE `MONITOR_JVM_GARBAGE_COLLECTOR`
(
    `ID`                     bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`            varchar(32) NOT NULL COMMENT '应用实例ID',
    `GARBAGE_COLLECTOR_NO`   int(8)       DEFAULT NULL COMMENT '内存管理器序号',
    `GARBAGE_COLLECTOR_NAME` varchar(125) DEFAULT NULL COMMENT '内存管理器名称',
    `COLLECTION_COUNT`       varchar(8)   DEFAULT NULL COMMENT 'GC总次数',
    `COLLECTION_TIME`        varchar(255) DEFAULT NULL COMMENT 'GC总时间（毫秒）',
    `INSERT_TIME`            datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`            datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    CONSTRAINT `MONITOR_JVM_GARBAGE_COLLECTOR_INSTANCE_FK` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `MONITOR_INSTANCE` (`INSTANCE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='java虚拟机GC信息表';

-- ----------------------------
-- Table structure for MONITOR_JVM_MEMORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_MEMORY`;
CREATE TABLE `MONITOR_JVM_MEMORY`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID` varchar(32) NOT NULL COMMENT '应用实例ID',
    `MEMORY_TYPE` varchar(32)  DEFAULT NULL COMMENT '内存类型',
    `INIT`        bigint(20)   DEFAULT NULL COMMENT '初始内存量（单位：byte）',
    `USED`        bigint(20)   DEFAULT NULL COMMENT '已用内存量（单位：byte）',
    `COMMITTED`   bigint(20)   DEFAULT NULL COMMENT '提交内存量（单位：byte）',
    `MAX`         varchar(100) DEFAULT NULL COMMENT '最大内存量（单位：byte，可能存在未定义）',
    `INSERT_TIME` datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    CONSTRAINT `MONITOR_JVM_MEMORY_INSTANCE_FK` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `MONITOR_INSTANCE` (`INSTANCE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='java虚拟机内存信息表';

-- ----------------------------
-- Table structure for MONITOR_JVM_MEMORY_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_MEMORY_HISTORY`;
CREATE TABLE `MONITOR_JVM_MEMORY_HISTORY`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID` varchar(32) NOT NULL COMMENT '应用实例ID',
    `MEMORY_TYPE` varchar(32)  DEFAULT NULL COMMENT '内存类型',
    `INIT`        bigint(20)   DEFAULT NULL COMMENT '初始内存量（单位：byte）',
    `USED`        bigint(20)   DEFAULT NULL COMMENT '已用内存量（单位：byte）',
    `COMMITTED`   bigint(20)   DEFAULT NULL COMMENT '提交内存量（单位：byte）',
    `MAX`         varchar(100) DEFAULT NULL COMMENT '最大内存量（单位：byte，可能存在未定义）',
    `INSERT_TIME` datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    CONSTRAINT `MONITOR_JVM_MEMORY_HISTORY_INSTANCE_FK` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `MONITOR_INSTANCE` (`INSTANCE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='java虚拟机内存历史记录表';

-- ----------------------------
-- Table structure for MONITOR_JVM_RUNTIME
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_RUNTIME`;
CREATE TABLE `MONITOR_JVM_RUNTIME`
(
    `ID`                           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`                  varchar(32) NOT NULL COMMENT '应用实例ID',
    `NAME`                         varchar(125) DEFAULT NULL COMMENT '正在运行的Java虚拟机名称',
    `VM_NAME`                      varchar(125) DEFAULT NULL COMMENT 'Java虚拟机实现名称',
    `VM_VENDOR`                    varchar(125) DEFAULT NULL COMMENT 'Java虚拟机实现供应商',
    `VM_VERSION`                   varchar(125) DEFAULT NULL COMMENT 'Java虚拟机实现版本',
    `SPEC_NAME`                    varchar(125) DEFAULT NULL COMMENT 'Java虚拟机规范名称',
    `SPEC_VENDOR`                  varchar(125) DEFAULT NULL COMMENT 'Java虚拟机规范供应商',
    `SPEC_VERSION`                 varchar(125) DEFAULT NULL COMMENT 'Java虚拟机规范版本',
    `MANAGEMENT_SPEC_VERSION`      varchar(10)  DEFAULT NULL COMMENT '管理接口规范版本',
    `CLASS_PATH`                   text COMMENT 'Java类路径',
    `LIBRARY_PATH`                 text COMMENT 'Java库路径',
    `IS_BOOT_CLASS_PATH_SUPPORTED` varchar(1)   DEFAULT NULL COMMENT 'Java虚拟机是否支持引导类路径',
    `BOOT_CLASS_PATH`              text COMMENT '引导类路径',
    `INPUT_ARGUMENTS`              text COMMENT 'Java虚拟机入参',
    `UPTIME`                       varchar(255) DEFAULT NULL COMMENT 'Java虚拟机的正常运行时间（毫秒）',
    `START_TIME`                   datetime     DEFAULT NULL COMMENT 'Java虚拟机的开始时间',
    `INSERT_TIME`                  datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`                  datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    CONSTRAINT `MONITOR_JVM_RUNTIME_INSTANCE_FK` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `MONITOR_INSTANCE` (`INSTANCE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='java虚拟机运行时信息表';

-- ----------------------------
-- Table structure for MONITOR_JVM_THREAD
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_THREAD`;
CREATE TABLE `MONITOR_JVM_THREAD`
(
    `ID`                         bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`                varchar(32) NOT NULL COMMENT '应用实例ID',
    `THREAD_COUNT`               int(8)   DEFAULT NULL COMMENT '当前活动线程数',
    `PEAK_THREAD_COUNT`          int(8)   DEFAULT NULL COMMENT '线程峰值',
    `TOTAL_STARTED_THREAD_COUNT` int(8)   DEFAULT NULL COMMENT '已创建并已启动的线程总数',
    `DAEMON_THREAD_COUNT`        int(8)   DEFAULT NULL COMMENT '当前活动守护线程数',
    `INSERT_TIME`                datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`                datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    CONSTRAINT `MONITOR_JVM_THREAD_INSTANCE_FK` FOREIGN KEY (`INSTANCE_ID`) REFERENCES `MONITOR_INSTANCE` (`INSTANCE_ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='java虚拟机线程信息表';

-- ----------------------------
-- Table structure for MONITOR_LOG_EXCEPTION
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_LOG_EXCEPTION`;
CREATE TABLE `MONITOR_LOG_EXCEPTION`
(
    `ID`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `REQ_PARAM`   text COMMENT '请求参数',
    `EXC_NAME`    varchar(255) DEFAULT NULL COMMENT '异常名称',
    `EXC_MESSAGE` longtext COMMENT '异常信息',
    `USER_ID`     bigint(20)   NOT NULL COMMENT '操作用户ID',
    `USERNAME`    varchar(255) NOT NULL COMMENT '操作用户名',
    `OPER_METHOD` varchar(255) NOT NULL COMMENT '操作方法',
    `URI`         varchar(255) DEFAULT NULL COMMENT '请求URI',
    `IP`          varchar(255) DEFAULT NULL COMMENT '请求IP',
    `INSERT_TIME` datetime     NOT NULL COMMENT '插入时间',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='异常日志表';

-- ----------------------------
-- Table structure for MONITOR_LOG_OPERATION
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_LOG_OPERATION`;
CREATE TABLE `MONITOR_LOG_OPERATION`
(
    `ID`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `OPER_MODULE` varchar(64)  NOT NULL COMMENT '功能模块',
    `OPER_TYPE`   varchar(255) NOT NULL COMMENT '操作类型',
    `OPER_DESC`   varchar(500) DEFAULT NULL COMMENT '操作描述',
    `REQ_PARAM`   text COMMENT '请求参数',
    `RESP_PARAM`  longtext COMMENT '返回参数',
    `USER_ID`     bigint(20)   NOT NULL COMMENT '操作用户ID',
    `USERNAME`    varchar(50)  NOT NULL COMMENT '操作用户名',
    `OPER_METHOD` varchar(255) NOT NULL COMMENT '操作方法',
    `URI`         varchar(255) NOT NULL COMMENT '请求URI',
    `IP`          varchar(255) NOT NULL COMMENT '请求IP',
    `INSERT_TIME` datetime     NOT NULL COMMENT '插入时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC COMMENT ='操作日志表';

-- ----------------------------
-- Table structure for MONITOR_NET
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_NET`;
CREATE TABLE `MONITOR_NET`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP_SOURCE`   varchar(15) NOT NULL COMMENT 'IP地址（来源）',
    `IP_TARGET`   varchar(15) NOT NULL COMMENT 'IP地址（目的地）',
    `IP_DESC`     varchar(500) DEFAULT NULL COMMENT 'IP地址描述',
    `STATUS`      varchar(1)   DEFAULT NULL COMMENT '状态（0：网络不通，1：网络正常）',
    `INSERT_TIME` datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP_SOURCE` (`IP_SOURCE`) USING BTREE COMMENT '索引_IP地址（来源）',
    KEY `NX_IP_TARGET` (`IP_TARGET`) USING BTREE COMMENT '索引_IP地址（目的地）'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='网络信息表';

-- ----------------------------
-- Table structure for MONITOR_REALTIME_MONITORING
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_REALTIME_MONITORING`;
CREATE TABLE `MONITOR_REALTIME_MONITORING`
(
    `ID`            bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `TYPE`          varchar(8)  NOT NULL COMMENT '监控类型（SERVER、NET、INSTANCE、CUSTOM）',
    `CODE`          varchar(32) NOT NULL COMMENT '监控编号',
    `IS_SENT_ALARM` varchar(1)  NOT NULL COMMENT '是否已经发送了告警（1：是，0：否）',
    `INSERT_TIME`   datetime    NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`   datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_TYPE_CODE` (`TYPE`, `CODE`) USING BTREE COMMENT '索引_类型_编码'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='实时监控表';

-- ----------------------------
-- Table structure for MONITOR_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ROLE`;
CREATE TABLE `MONITOR_ROLE`
(
    `ID`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `ROLE_NAME`   varchar(255) NOT NULL COMMENT '角色名称',
    `CREATE_TIME` datetime     NOT NULL COMMENT '创建时间',
    `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='监控用户角色表';

-- ----------------------------
-- Table structure for MONITOR_SERVER
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER`;
CREATE TABLE `MONITOR_SERVER`
(
    `ID`             bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`             varchar(15) NOT NULL COMMENT 'IP地址',
    `SERVER_NAME`    varchar(30)  DEFAULT NULL COMMENT '服务器名',
    `SERVER_SUMMARY` varchar(500) DEFAULT NULL COMMENT '服务器摘要',
    `IS_ONLINE`      varchar(1)   DEFAULT NULL COMMENT '服务器状态（0：离线，1：在线）',
    `CONN_FREQUENCY` int(8)      NOT NULL COMMENT '连接频率',
    `INSERT_TIME`    datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`    datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_CPU
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_CPU`;
CREATE TABLE `MONITOR_SERVER_CPU`
(
    `ID`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) NOT NULL COMMENT 'IP地址',
    `CPU_NO`       int(8)        DEFAULT NULL COMMENT 'CPU序号',
    `CPU_MHZ`      int(11)       DEFAULT NULL COMMENT 'CPU频率（MHz）',
    `CPU_VENDOR`   varchar(255)  DEFAULT NULL COMMENT 'CPU卖主',
    `CPU_MODEL`    varchar(255)  DEFAULT NULL COMMENT 'CPU的类别，如：Celeron',
    `CPU_USER`     double(16, 4) DEFAULT NULL COMMENT 'CPU用户使用率',
    `CPU_SYS`      double(16, 4) DEFAULT NULL COMMENT 'CPU系统使用率',
    `CPU_WAIT`     double(16, 4) DEFAULT NULL COMMENT 'CPU等待率',
    `CPU_NICE`     double(16, 4) DEFAULT NULL COMMENT 'CPU错误率',
    `CPU_COMBINED` double(16, 4) DEFAULT NULL COMMENT 'CPU使用率',
    `CPU_IDLE`     double(16, 4) DEFAULT NULL COMMENT 'CPU剩余率',
    `INSERT_TIME`  datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP地址',
    CONSTRAINT `MONITOR_SERVER_CPU_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器CPU表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_CPU_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_CPU_HISTORY`;
CREATE TABLE `MONITOR_SERVER_CPU_HISTORY`
(
    `ID`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) NOT NULL COMMENT 'IP地址',
    `CPU_NO`       int(8)        DEFAULT NULL COMMENT 'CPU序号',
    `CPU_MHZ`      int(11)       DEFAULT NULL COMMENT 'CPU频率（MHz）',
    `CPU_VENDOR`   varchar(255)  DEFAULT NULL COMMENT 'CPU卖主',
    `CPU_MODEL`    varchar(255)  DEFAULT NULL COMMENT 'CPU的类别，如：Celeron',
    `CPU_USER`     double(16, 4) DEFAULT NULL COMMENT 'CPU用户使用率',
    `CPU_SYS`      double(16, 4) DEFAULT NULL COMMENT 'CPU系统使用率',
    `CPU_WAIT`     double(16, 4) DEFAULT NULL COMMENT 'CPU等待率',
    `CPU_NICE`     double(16, 4) DEFAULT NULL COMMENT 'CPU错误率',
    `CPU_COMBINED` double(16, 4) DEFAULT NULL COMMENT 'CPU使用率',
    `CPU_IDLE`     double(16, 4) DEFAULT NULL COMMENT 'CPU剩余率',
    `INSERT_TIME`  datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP地址',
    CONSTRAINT `MONITOR_SERVER_CPU_HISTORY_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器CPU历史记录表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_DISK
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_DISK`;
CREATE TABLE `MONITOR_SERVER_DISK`
(
    `ID`            bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`            varchar(15) NOT NULL COMMENT 'IP地址',
    `DISK_NO`       int(8)        DEFAULT NULL COMMENT '磁盘序号',
    `DEV_NAME`      varchar(100)  DEFAULT NULL COMMENT '分区的盘符名称',
    `DIR_NAME`      varchar(100)  DEFAULT NULL COMMENT '分区的盘符路径',
    `TYPE_NAME`     varchar(50)   DEFAULT NULL COMMENT '磁盘类型名，比如本地硬盘、光驱、网络文件系统等',
    `SYS_TYPE_NAME` varchar(50)   DEFAULT NULL COMMENT '磁盘类型，比如 FAT32、NTFS',
    `TOTAL`         bigint(20)    DEFAULT NULL COMMENT '磁盘总大小（单位：byte）',
    `FREE`          bigint(20)    DEFAULT NULL COMMENT '磁盘剩余大小（单位：byte）',
    `USED`          bigint(20)    DEFAULT NULL COMMENT '磁盘已用大小（单位：byte）',
    `AVAIL`         bigint(20)    DEFAULT NULL COMMENT '磁盘可用大小（单位：byte）',
    `USE_PERCENT`   double(16, 4) DEFAULT NULL COMMENT '磁盘资源的利用率',
    `INSERT_TIME`   datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`   datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_DISK_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器磁盘表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_DISK_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_DISK_HISTORY`;
CREATE TABLE `MONITOR_SERVER_DISK_HISTORY`
(
    `ID`            bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`            varchar(15) NOT NULL COMMENT 'IP地址',
    `DISK_NO`       int(8)        DEFAULT NULL COMMENT '磁盘序号',
    `DEV_NAME`      varchar(100)  DEFAULT NULL COMMENT '分区的盘符名称',
    `DIR_NAME`      varchar(100)  DEFAULT NULL COMMENT '分区的盘符路径',
    `TYPE_NAME`     varchar(50)   DEFAULT NULL COMMENT '磁盘类型名，比如本地硬盘、光驱、网络文件系统等',
    `SYS_TYPE_NAME` varchar(50)   DEFAULT NULL COMMENT '磁盘类型，比如 FAT32、NTFS',
    `TOTAL`         bigint(20)    DEFAULT NULL COMMENT '磁盘总大小（单位：byte）',
    `FREE`          bigint(20)    DEFAULT NULL COMMENT '磁盘剩余大小（单位：byte）',
    `USED`          bigint(20)    DEFAULT NULL COMMENT '磁盘已用大小（单位：byte）',
    `AVAIL`         bigint(20)    DEFAULT NULL COMMENT '磁盘可用大小（单位：byte）',
    `USE_PERCENT`   double(16, 4) DEFAULT NULL COMMENT '磁盘资源的利用率',
    `INSERT_TIME`   datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`   datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_DISK_HISTORY_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器磁盘历史记录表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_MEMORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_MEMORY`;
CREATE TABLE `MONITOR_SERVER_MEMORY`
(
    `ID`                bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                varchar(15) NOT NULL COMMENT 'IP地址',
    `MEM_TOTAL`         bigint(20)    DEFAULT NULL COMMENT '物理内存总量（单位：byte）',
    `MEM_USED`          bigint(20)    DEFAULT NULL COMMENT '物理内存使用量（单位：byte）',
    `MEM_FREE`          bigint(20)    DEFAULT NULL COMMENT '物理内存剩余量（单位：byte）',
    `MEN_USED_PERCENT`  double(16, 4) DEFAULT NULL COMMENT '物理内存使用率',
    `SWAP_TOTAL`        bigint(20)    DEFAULT NULL COMMENT '交换区总量（单位：byte）',
    `SWAP_USED`         bigint(20)    DEFAULT NULL COMMENT '交换区使用量（单位：byte）',
    `SWAP_FREE`         bigint(20)    DEFAULT NULL COMMENT '交换区剩余量（单位：byte）',
    `SWAP_USED_PERCENT` double(16, 4) DEFAULT NULL COMMENT '交换区使用率',
    `INSERT_TIME`       datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`       datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_MEMORY_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器内存表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_MEMORY_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_MEMORY_HISTORY`;
CREATE TABLE `MONITOR_SERVER_MEMORY_HISTORY`
(
    `ID`                bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                varchar(15) NOT NULL COMMENT 'IP地址',
    `MEM_TOTAL`         bigint(20)    DEFAULT NULL COMMENT '物理内存总量（单位：byte）',
    `MEM_USED`          bigint(20)    DEFAULT NULL COMMENT '物理内存使用量（单位：byte）',
    `MEM_FREE`          bigint(20)    DEFAULT NULL COMMENT '物理内存剩余量（单位：byte）',
    `MEN_USED_PERCENT`  double(16, 4) DEFAULT NULL COMMENT '物理内存使用率',
    `SWAP_TOTAL`        bigint(20)    DEFAULT NULL COMMENT '交换区总量（单位：byte）',
    `SWAP_USED`         bigint(20)    DEFAULT NULL COMMENT '交换区使用量（单位：byte）',
    `SWAP_FREE`         bigint(20)    DEFAULT NULL COMMENT '交换区剩余量（单位：byte）',
    `SWAP_USED_PERCENT` double(16, 4) DEFAULT NULL COMMENT '交换区使用率',
    `INSERT_TIME`       datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`       datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_MEMORY_HISTORY_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器内存历史记录表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_NETCARD
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_NETCARD`;
CREATE TABLE `MONITOR_SERVER_NETCARD`
(
    `ID`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) NOT NULL COMMENT 'IP地址',
    `NET_NO`       int(8)        DEFAULT NULL COMMENT '网卡序号',
    `NAME`         varchar(50)   DEFAULT NULL COMMENT '网卡名字',
    `TYPE`         varchar(50)   DEFAULT NULL COMMENT '网卡类型',
    `ADDRESS`      varchar(15)   DEFAULT NULL COMMENT '网卡地址',
    `MASK`         varchar(15)   DEFAULT NULL COMMENT '子网掩码',
    `BROADCAST`    varchar(15)   DEFAULT NULL COMMENT '广播地址',
    `HW_ADDR`      varchar(17)   DEFAULT NULL COMMENT 'MAC地址',
    `DESCRIPTION`  varchar(255)  DEFAULT NULL COMMENT '网卡信息描述',
    `RX_BYTES`     bigint(20)    DEFAULT NULL COMMENT '接收到的总字节数',
    `RX_PACKETS`   bigint(20)    DEFAULT NULL COMMENT '接收的总包数',
    `RX_ERRORS`    bigint(20)    DEFAULT NULL COMMENT '接收到的错误包数',
    `RX_DROPPED`   bigint(20)    DEFAULT NULL COMMENT '接收时丢弃的包数',
    `TX_BYTES`     bigint(20)    DEFAULT NULL COMMENT '发送的总字节数',
    `TX_PACKETS`   bigint(20)    DEFAULT NULL COMMENT '发送的总包数',
    `TX_ERRORS`    bigint(20)    DEFAULT NULL COMMENT '发送时的错误包数',
    `TX_DROPPED`   bigint(20)    DEFAULT NULL COMMENT '发送时丢弃的包数',
    `DOWNLOAD_BPS` double(22, 4) DEFAULT NULL COMMENT '下载速度',
    `UPLOAD_BPS`   double(22, 4) DEFAULT NULL COMMENT '上传速度',
    `INSERT_TIME`  datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_NETCARD_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器网卡表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_NETCARD_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_NETCARD_HISTORY`;
CREATE TABLE `MONITOR_SERVER_NETCARD_HISTORY`
(
    `ID`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) NOT NULL COMMENT 'IP地址',
    `NET_NO`       int(8)        DEFAULT NULL COMMENT '网卡序号',
    `NAME`         varchar(50)   DEFAULT NULL COMMENT '网卡名字',
    `TYPE`         varchar(50)   DEFAULT NULL COMMENT '网卡类型',
    `ADDRESS`      varchar(15)   DEFAULT NULL COMMENT '网卡地址',
    `MASK`         varchar(15)   DEFAULT NULL COMMENT '子网掩码',
    `BROADCAST`    varchar(15)   DEFAULT NULL COMMENT '广播地址',
    `HW_ADDR`      varchar(17)   DEFAULT NULL COMMENT 'MAC地址',
    `DESCRIPTION`  varchar(255)  DEFAULT NULL COMMENT '网卡信息描述',
    `RX_BYTES`     bigint(20)    DEFAULT NULL COMMENT '接收到的总字节数',
    `RX_PACKETS`   bigint(20)    DEFAULT NULL COMMENT '接收的总包数',
    `RX_ERRORS`    bigint(20)    DEFAULT NULL COMMENT '接收到的错误包数',
    `RX_DROPPED`   bigint(20)    DEFAULT NULL COMMENT '接收时丢弃的包数',
    `TX_BYTES`     bigint(20)    DEFAULT NULL COMMENT '发送的总字节数',
    `TX_PACKETS`   bigint(20)    DEFAULT NULL COMMENT '发送的总包数',
    `TX_ERRORS`    bigint(20)    DEFAULT NULL COMMENT '发送时的错误包数',
    `TX_DROPPED`   bigint(20)    DEFAULT NULL COMMENT '发送时丢弃的包数',
    `DOWNLOAD_BPS` double(22, 4) DEFAULT NULL COMMENT '下载速度',
    `UPLOAD_BPS`   double(22, 4) DEFAULT NULL COMMENT '上传速度',
    `INSERT_TIME`  datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_NETCARD_HISTORY_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器网卡历史记录表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_OS
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_OS`;
CREATE TABLE `MONITOR_SERVER_OS`
(
    `ID`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) NOT NULL COMMENT 'IP地址',
    `SERVER_NAME`  varchar(30)  DEFAULT NULL COMMENT '服务器名',
    `OS_NAME`      varchar(50)  DEFAULT NULL COMMENT '操作系统名称',
    `OS_VERSION`   varchar(50)  DEFAULT NULL COMMENT '操作系统版本',
    `USER_NAME`    varchar(50)  DEFAULT NULL COMMENT '用户名称',
    `USER_HOME`    varchar(150) DEFAULT NULL COMMENT '用户主目录',
    `OS_TIME_ZONE` varchar(30)  DEFAULT NULL COMMENT '操作系统时区',
    `INSERT_TIME`  datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE,
    CONSTRAINT `MONITOR_SERVER_OS_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器操作系统表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_POWER_SOURCES
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_POWER_SOURCES`;
CREATE TABLE `MONITOR_SERVER_POWER_SOURCES`
(
    `ID`                         bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                         varchar(15) NOT NULL COMMENT 'IP地址',
    `POWER_SOURCES_NO`           int(8)        DEFAULT NULL COMMENT '电池序号',
    `NAME`                       varchar(255)  DEFAULT NULL COMMENT '操作系统级别的电源名称',
    `DEVICE_NAME`                varchar(255)  DEFAULT NULL COMMENT '设备级别的电源名称',
    `REMAINING_CAPACITY_PERCENT` double(22, 4) DEFAULT NULL COMMENT '剩余容量百分比',
    `TIME_REMAINING_ESTIMATED`   varchar(50)   DEFAULT NULL COMMENT '操作系统估计的电源上剩余的估计时间（以毫秒为单位）',
    `TIME_REMAINING_INSTANT`     varchar(50)   DEFAULT NULL COMMENT '估计的电源剩余时间（以毫秒为单位），由电池报告',
    `POWER_USAGE_RATE`           varchar(50)   DEFAULT NULL COMMENT '电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）',
    `VOLTAGE`                    varchar(50)   DEFAULT NULL COMMENT '电池电压，以伏特为单位',
    `AMPERAGE`                   varchar(50)   DEFAULT NULL COMMENT '电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）',
    `IS_POWER_ON_LINE`           varchar(1)    DEFAULT NULL COMMENT '是否已插入外部电源',
    `IS_CHARGING`                varchar(1)    DEFAULT NULL COMMENT '电池是否正在充电',
    `IS_DISCHARGING`             varchar(1)    DEFAULT NULL COMMENT '电池是否正在放电',
    `CURRENT_CAPACITY`           varchar(50)   DEFAULT NULL COMMENT '电池的当前（剩余）容量',
    `MAX_CAPACITY`               varchar(50)   DEFAULT NULL COMMENT '电池的最大容量',
    `DESIGN_CAPACITY`            varchar(50)   DEFAULT NULL COMMENT '电池的设计（原始）容量',
    `CYCLE_COUNT`                varchar(255)  DEFAULT NULL COMMENT '电池的循环计数（如果知道）',
    `CHEMISTRY`                  varchar(255)  DEFAULT NULL COMMENT '电池化学成分（例如，锂离子电池）',
    `MANUFACTURE_DATE`           varchar(10)   DEFAULT NULL COMMENT '电池的生产日期',
    `MANUFACTURER`               varchar(255)  DEFAULT NULL COMMENT '电池制造商的名称',
    `SERIAL_NUMBER`              varchar(50)   DEFAULT NULL COMMENT '电池的序列号',
    `TEMPERATURE`                varchar(50)   DEFAULT NULL COMMENT '电池温度，以摄氏度为单位',
    `INSERT_TIME`                datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`                datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_POWER_SOURCES_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器电池表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_PROCESS
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_PROCESS`;
CREATE TABLE `MONITOR_SERVER_PROCESS`
(
    `ID`                        bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                        varchar(15) NOT NULL COMMENT 'IP地址',
    `PROCESS_ID`                int(8)      NOT NULL COMMENT '进程ID',
    `NAME`                      varchar(255)  DEFAULT NULL COMMENT '进程名',
    `PATH`                      text COMMENT '执行进程的完整路径',
    `COMMAND_LINE`              text COMMENT '进程命令行',
    `CURRENT_WORKING_DIRECTORY` varchar(255)  DEFAULT NULL COMMENT '进程当前的工作目录',
    `USER`                      varchar(255)  DEFAULT NULL COMMENT '用户名',
    `STATE`                     varchar(10)   DEFAULT NULL COMMENT '进程执行状态',
    `UP_TIME`                   varchar(255)  DEFAULT NULL COMMENT '进程已启动的毫秒数',
    `START_TIME`                datetime      DEFAULT NULL COMMENT '进程的开始时间',
    `CPU_LOAD_CUMULATIVE`       double(16, 4) DEFAULT NULL COMMENT '进程的累积CPU使用率',
    `BITNESS`                   varchar(4)    DEFAULT NULL COMMENT '进程的位数',
    `MEMORY_SIZE`               bigint(20)    DEFAULT NULL COMMENT '占用内存大小（单位：byte）',
    `INSERT_TIME`               datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`               datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_PROCESS_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器进程表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_PROCESS_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_PROCESS_HISTORY`;
CREATE TABLE `MONITOR_SERVER_PROCESS_HISTORY`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`          varchar(15) NOT NULL COMMENT 'IP地址',
    `PROCESS_NUM` int(8)   DEFAULT NULL COMMENT '正在运行的进程数',
    `INSERT_TIME` datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`),
    KEY `NX_IP` (`IP`) USING BTREE,
    CONSTRAINT `MONITOR_SERVER_PROCESS_HISTORY_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器进程历史记录表';

-- ----------------------------
-- Table structure for MONITOR_SERVER_SENSORS
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_SENSORS`;
CREATE TABLE `MONITOR_SERVER_SENSORS`
(
    `ID`              bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`              varchar(15) NOT NULL COMMENT 'IP地址',
    `CPU_TEMPERATURE` varchar(50)  DEFAULT NULL COMMENT 'CPU温度（以摄氏度为单位）（如果可用）',
    `CPU_VOLTAGE`     varchar(50)  DEFAULT NULL COMMENT 'CPU电压（以伏特为单位）（如果可用）',
    `FAN_SPEED`       varchar(255) DEFAULT NULL COMMENT '风扇的转速（rpm）（如果可用）',
    `INSERT_TIME`     datetime    NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`     datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    CONSTRAINT `MONITOR_SERVER_SENSORS_SERVER_FK` FOREIGN KEY (`IP`) REFERENCES `MONITOR_SERVER` (`IP`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='服务器传感器表';

-- ----------------------------
-- Table structure for MONITOR_USER
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_USER`;
CREATE TABLE `MONITOR_USER`
(
    `ID`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `ACCOUNT`       varchar(255) NOT NULL COMMENT '账号',
    `USERNAME`      varchar(50)  NOT NULL COMMENT '用户名',
    `PASSWORD`      varchar(255) NOT NULL COMMENT '密码',
    `ROLE_ID`       bigint(20)   NOT NULL COMMENT '角色ID',
    `EMAIL`         varchar(255)  DEFAULT NULL COMMENT '电子邮箱',
    `REMARKS`       varchar(1000) DEFAULT NULL COMMENT '备注',
    `REGISTER_TIME` datetime     NOT NULL COMMENT '注册时间',
    `UPDATE_TIME`   datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    KEY `NX_USER_ROLE_ID` (`ROLE_ID`) USING BTREE COMMENT '索引_角色ID',
    CONSTRAINT `MONITOR_USER_ROLE_FK` FOREIGN KEY (`ROLE_ID`) REFERENCES `MONITOR_ROLE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='监控用户表';

-- ----------------------------
-- Table structure for SPRING_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION`;
CREATE TABLE `SPRING_SESSION`
(
    `PRIMARY_ID`            char(36)   NOT NULL,
    `SESSION_ID`            char(36)   NOT NULL,
    `CREATION_TIME`         bigint(20) NOT NULL,
    `LAST_ACCESS_TIME`      bigint(20) NOT NULL,
    `MAX_INACTIVE_INTERVAL` int(11)    NOT NULL,
    `EXPIRY_TIME`           bigint(20) NOT NULL,
    `PRINCIPAL_NAME`        varchar(100) DEFAULT NULL,
    PRIMARY KEY (`PRIMARY_ID`) USING BTREE,
    UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`) USING BTREE,
    KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`) USING BTREE,
    KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for SPRING_SESSION_ATTRIBUTES
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES`
(
    `SESSION_PRIMARY_ID` char(36)     NOT NULL,
    `ATTRIBUTE_NAME`     varchar(200) NOT NULL,
    `ATTRIBUTE_BYTES`    blob         NOT NULL,
    PRIMARY KEY (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`) USING BTREE,
    CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `MONITOR_ROLE`(`ID`, `ROLE_NAME`, `CREATE_TIME`, `UPDATE_TIME`)
VALUES (1, '超级管理员', '2020-07-05 14:30:32', '2020-07-05 14:30:35');
INSERT INTO `MONITOR_ROLE`(`ID`, `ROLE_NAME`, `CREATE_TIME`, `UPDATE_TIME`)
VALUES (2, '普通用户', '2020-07-14 10:47:15', '2020-07-14 10:47:19');

INSERT INTO `MONITOR_USER`
VALUES (1, 'admin', '超级管理员', '$2a$10$gcWQ8riyvmxy4eSGOpJkZe.n//JfqAUBQlPaEO5iOM.Glte7Rbfsy', 1, '', '超级管理员',
        '2021-5-28 22:18:01', '2021-05-28 22:42:23');
INSERT INTO `MONITOR_USER`
VALUES (2, 'guest', '访客', '$2a$10$OE0rJtueah3L7jYAh/rtHeUThXxvAvyQZlfP74OuhNGTAUfDiCd6y', 2, '', '访客',
        '2020-8-2 21:22:31', '2020-12-31 12:05:33');

COMMIT;
