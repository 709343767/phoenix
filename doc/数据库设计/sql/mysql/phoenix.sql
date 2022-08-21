/*
 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 04/12/2021 23:14:18
*/
DROP DATABASE IF EXISTS `phoenix`;
CREATE DATABASE IF NOT EXISTS `phoenix` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `phoenix`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for MONITOR_ALARM_DEFINITION
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ALARM_DEFINITION`;
CREATE TABLE `MONITOR_ALARM_DEFINITION`
(
    `ID`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `TYPE`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）',
    `FIRST_CLASS`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '一级分类',
    `SECOND_CLASS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级分类',
    `THIRD_CLASS`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三级分类',
    `GRADE`        varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '告警级别（INFO、WARN、ERROR、FATAL）',
    `CODE`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '告警编码',
    `TITLE`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '告警标题',
    `CONTENT`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '告警内容',
    `INSERT_TIME`  datetime                                                      NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_CODE` (`CODE`) USING BTREE COMMENT '索引_告警编码'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '告警定义表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_ALARM_RECORD
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ALARM_RECORD`;
CREATE TABLE `MONITOR_ALARM_RECORD`
(
    `ID`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `CODE`           varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '告警代码，使用UUID',
    `ALARM_DEF_CODE` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '告警定义编码',
    `TYPE`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）',
    `WAY`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '告警方式（SMS、MAIL、...）',
    `LEVEL`          varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '告警级别（INFO、WARM、ERROR、FATAL）',
    `TITLE`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '告警标题',
    `CONTENT`        longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '告警内容',
    `STATUS`         varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '告警发送状态（0：失败；1：成功）',
    `NUMBER`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被告警人号码（手机号码、电子邮箱、...）',
    `INSERT_TIME`    datetime                                                      NOT NULL COMMENT '告警时间',
    `UPDATE_TIME`    datetime                                                      NULL DEFAULT NULL COMMENT '告警结果获取时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_CODE` (`CODE`) USING BTREE COMMENT '索引_告警记录编码',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '告警记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_CONFIG`;
CREATE TABLE `MONITOR_CONFIG`
(
    `ID`          bigint(20)                                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `VALUE`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置内容（json字符串）',
    `INSERT_TIME` datetime                                              NOT NULL COMMENT '插入时间',
    `UPDATE_TIME` datetime                                              NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '监控配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_DB
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_DB`;
CREATE TABLE `MONITOR_DB`
(
    `ID`            bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `CONN_NAME`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '连接名称',
    `URL`           varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据库URL',
    `USERNAME`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '用户名',
    `PASSWORD`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '密码',
    `DB_TYPE`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '数据库类型',
    `DRIVER_CLASS`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '驱动类',
    `DB_DESC`       varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `IS_ONLINE`     varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL DEFAULT NULL COMMENT '数据库状态（0：离线，1：在线）',
    `OFFLINE_COUNT` int(8)                                                         NULL DEFAULT NULL COMMENT '离线次数',
    `MONITOR_ENV`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '监控环境',
    `MONITOR_GROUP` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '监控分组',
    `INSERT_TIME`   datetime                                                       NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`   datetime                                                       NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `MONITOR_DB_ENV_FK` (`MONITOR_ENV`) USING BTREE COMMENT '索引_监控环境',
    INDEX `MONITOR_DB_GROUP_FK` (`MONITOR_GROUP`) USING BTREE COMMENT '索引_监控分组',
    CONSTRAINT `MONITOR_DB_ENV_FK` FOREIGN KEY (`MONITOR_ENV`) REFERENCES `MONITOR_ENV` (`ENV_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `MONITOR_DB_GROUP_FK` FOREIGN KEY (`MONITOR_GROUP`) REFERENCES `MONITOR_GROUP` (`GROUP_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据库表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_ENV
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ENV`;
CREATE TABLE `MONITOR_ENV`
(
    `ID`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `ENV_NAME`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '环境名',
    `ENV_DESC`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '环境描述',
    `CREATE_ACCOUNT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人账号',
    `UPDATE_ACCOUNT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人账号',
    `INSERT_TIME`    datetime                                                      NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_ENV_NAME` (`ENV_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '监控环境表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_GROUP`;
CREATE TABLE `MONITOR_GROUP`
(
    `ID`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `GROUP_NAME`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '分组名',
    `GROUP_DESC`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组描述',
    `CREATE_ACCOUNT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人账号',
    `UPDATE_ACCOUNT` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人账号',
    `INSERT_TIME`    datetime                                                      NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_GROUP_NAME` (`GROUP_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '监控分组表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_HTTP
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_HTTP`;
CREATE TABLE `MONITOR_HTTP`
(
    `ID`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `HOSTNAME_SOURCE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主机名（来源）',
    `URL_TARGET`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址（目的地）',
    `METHOD`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '请求方法',
    `PARAMETER`       varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
    `DESCR`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `AVG_TIME`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '平均响应时间（毫秒）',
    `STATUS`          int(8)                                                        NULL DEFAULT NULL COMMENT '状态',
    `EXC_MESSAGE`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '异常信息',
    `OFFLINE_COUNT`   int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `MONITOR_ENV`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控环境',
    `MONITOR_GROUP`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控分组',
    `INSERT_TIME`     datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_HOSTNAME_SOURCE` (`HOSTNAME_SOURCE`) USING BTREE COMMENT '索引_主机名（来源）',
    INDEX `NX_URL_TARGET` (`URL_TARGET`) USING BTREE COMMENT '索引_URL（目的地）',
    INDEX `MONITOR_HTTP_ENV_FK` (`MONITOR_ENV`) USING BTREE COMMENT '索引_监控环境',
    INDEX `MONITOR_HTTP_GROUP_FK` (`MONITOR_GROUP`) USING BTREE COMMENT '索引_监控分组',
    CONSTRAINT `MONITOR_HTTP_ENV_FK` FOREIGN KEY (`MONITOR_ENV`) REFERENCES `MONITOR_ENV` (`ENV_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `MONITOR_HTTP_GROUP_FK` FOREIGN KEY (`MONITOR_GROUP`) REFERENCES `MONITOR_GROUP` (`GROUP_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'HTTP信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_HTTP_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_HTTP_HISTORY`;
CREATE TABLE `MONITOR_HTTP_HISTORY`
(
    `ID`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `HTTP_ID`         bigint(20)                                                    NOT NULL COMMENT 'HTTP主表ID',
    `HOSTNAME_SOURCE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主机名（来源）',
    `URL_TARGET`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址（目的地）',
    `METHOD`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '请求方法',
    `PARAMETER`       varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
    `DESCR`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `AVG_TIME`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '平均响应时间（毫秒）',
    `STATUS`          int(8)                                                        NULL DEFAULT NULL COMMENT '状态',
    `EXC_MESSAGE`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '异常信息',
    `OFFLINE_COUNT`   int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `INSERT_TIME`     datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_HOSTNAME_SOURCE` (`HOSTNAME_SOURCE`) USING BTREE COMMENT '索引_主机名（来源）',
    INDEX `NX_URL_TARGET` (`URL_TARGET`) USING BTREE COMMENT '索引_URL（目的地）',
    INDEX `NX_HTTP_ID` (`HTTP_ID`) USING BTREE COMMENT '索引_HTTP主表ID',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'HTTP信息历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_INSTANCE
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_INSTANCE`;
CREATE TABLE `MONITOR_INSTANCE`
(
    `ID`               bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '应用实例ID',
    `ENDPOINT`         varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '端点（客户端<client>、代理端<agent>、服务端<server>、UI端<ui>）',
    `INSTANCE_NAME`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '应用实例名',
    `INSTANCE_DESC`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用实例描述',
    `INSTANCE_SUMMARY` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用实例摘要',
    `LANGUAGE`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '编程语言',
    `APP_SERVER_TYPE`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '应用服务器类型',
    `IP`               varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `IS_ONLINE`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '应用状态（0：离线，1：在线）',
    `OFFLINE_COUNT`    int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `CONN_FREQUENCY`   int(8)                                                        NOT NULL COMMENT '连接频率',
    `MONITOR_ENV`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控环境',
    `MONITOR_GROUP`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控分组',
    `INSERT_TIME`      datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`      datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    INDEX `MONITOR_INSTANCE_ENV_FK` (`MONITOR_ENV`) USING BTREE COMMENT '索引_监控环境',
    INDEX `MONITOR_INSTANCE_GROUP_FK` (`MONITOR_GROUP`) USING BTREE COMMENT '索引_监控分组',
    CONSTRAINT `MONITOR_INSTANCE_ENV_FK` FOREIGN KEY (`MONITOR_ENV`) REFERENCES `MONITOR_ENV` (`ENV_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `MONITOR_INSTANCE_GROUP_FK` FOREIGN KEY (`MONITOR_GROUP`) REFERENCES `MONITOR_GROUP` (`GROUP_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '应用实例表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for MONITOR_JVM_CLASS_LOADING
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_CLASS_LOADING`;
CREATE TABLE `MONITOR_JVM_CLASS_LOADING`
(
    `ID`                       bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用实例ID',
    `TOTAL_LOADED_CLASS_COUNT` int(8)                                                       NULL DEFAULT NULL COMMENT '加载的类的总数',
    `LOADED_CLASS_COUNT`       int(8)                                                       NULL DEFAULT NULL COMMENT '当前加载的类的总数',
    `UNLOADED_CLASS_COUNT`     int(8)                                                       NULL DEFAULT NULL COMMENT '卸载的类总数',
    `IS_VERBOSE`               varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '是否启用了类加载系统的详细输出',
    `INSERT_TIME`              datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`              datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'java虚拟机类加载信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_JVM_GARBAGE_COLLECTOR
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_GARBAGE_COLLECTOR`;
CREATE TABLE `MONITOR_JVM_GARBAGE_COLLECTOR`
(
    `ID`                     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '应用实例ID',
    `GARBAGE_COLLECTOR_NO`   int(8)                                                        NULL DEFAULT NULL COMMENT '内存管理器序号',
    `GARBAGE_COLLECTOR_NAME` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内存管理器名称',
    `COLLECTION_COUNT`       varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT 'GC总次数',
    `COLLECTION_TIME`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'GC总时间（毫秒）',
    `INSERT_TIME`            datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`            datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'java虚拟机GC信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_JVM_MEMORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_MEMORY`;
CREATE TABLE `MONITOR_JVM_MEMORY`
(
    `ID`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '应用实例ID',
    `MEMORY_TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '内存类型',
    `INIT`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '初始内存量（单位：byte）',
    `USED`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '已用内存量（单位：byte）',
    `COMMITTED`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '提交内存量（单位：byte）',
    `MAX`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最大内存量（单位：byte，可能存在未定义）',
    `INSERT_TIME` datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'java虚拟机内存信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_JVM_MEMORY_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_MEMORY_HISTORY`;
CREATE TABLE `MONITOR_JVM_MEMORY_HISTORY`
(
    `ID`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '应用实例ID',
    `MEMORY_TYPE` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '内存类型',
    `INIT`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '初始内存量（单位：byte）',
    `USED`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '已用内存量（单位：byte）',
    `COMMITTED`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '提交内存量（单位：byte）',
    `MAX`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最大内存量（单位：byte，可能存在未定义）',
    `INSERT_TIME` datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID',
    INDEX `NX_MEMORY_TYPE` (`MEMORY_TYPE`) USING BTREE COMMENT '索引_JVM内存类型',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_JVM内存新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_JVM内存更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'java虚拟机内存历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_JVM_RUNTIME
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_RUNTIME`;
CREATE TABLE `MONITOR_JVM_RUNTIME`
(
    `ID`                           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '应用实例ID',
    `NAME`                         varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正在运行的Java虚拟机名称',
    `VM_NAME`                      varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机实现名称',
    `VM_VENDOR`                    varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机实现供应商',
    `VM_VERSION`                   varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机实现版本',
    `SPEC_NAME`                    varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机规范名称',
    `SPEC_VENDOR`                  varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机规范供应商',
    `SPEC_VERSION`                 varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机规范版本',
    `MANAGEMENT_SPEC_VERSION`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '管理接口规范版本',
    `CLASS_PATH`                   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT 'Java类路径',
    `LIBRARY_PATH`                 text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT 'Java库路径',
    `IS_BOOT_CLASS_PATH_SUPPORTED` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT 'Java虚拟机是否支持引导类路径',
    `BOOT_CLASS_PATH`              text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '引导类路径',
    `INPUT_ARGUMENTS`              text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT 'Java虚拟机入参',
    `UPTIME`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Java虚拟机的正常运行时间（毫秒）',
    `START_TIME`                   datetime                                                      NULL DEFAULT NULL COMMENT 'Java虚拟机的开始时间',
    `INSERT_TIME`                  datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`                  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'java虚拟机运行时信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_JVM_THREAD
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_JVM_THREAD`;
CREATE TABLE `MONITOR_JVM_THREAD`
(
    `ID`                         bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `INSTANCE_ID`                varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用实例ID',
    `THREAD_COUNT`               int(8)                                                       NULL DEFAULT NULL COMMENT '当前活动线程数',
    `PEAK_THREAD_COUNT`          int(8)                                                       NULL DEFAULT NULL COMMENT '线程峰值',
    `TOTAL_STARTED_THREAD_COUNT` int(8)                                                       NULL DEFAULT NULL COMMENT '已创建并已启动的线程总数',
    `DAEMON_THREAD_COUNT`        int(8)                                                       NULL DEFAULT NULL COMMENT '当前活动守护线程数',
    `INSERT_TIME`                datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`                datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSTANCE_ID` (`INSTANCE_ID`) USING BTREE COMMENT '索引_应用实例ID'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'java虚拟机线程信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_LOG_EXCEPTION
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_LOG_EXCEPTION`;
CREATE TABLE `MONITOR_LOG_EXCEPTION`
(
    `ID`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `REQ_PARAM`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '请求参数',
    `EXC_NAME`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常名称',
    `EXC_MESSAGE` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '异常信息',
    `USER_ID`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '操作用户ID',
    `USERNAME`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户名',
    `OPER_METHOD` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作方法',
    `URI`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
    `IP`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求IP',
    `INSERT_TIME` datetime                                                      NOT NULL COMMENT '插入时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '异常日志表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_LOG_OPERATION
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_LOG_OPERATION`;
CREATE TABLE `MONITOR_LOG_OPERATION`
(
    `ID`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `OPER_MODULE` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '功能模块',
    `OPER_TYPE`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型',
    `OPER_DESC`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作描述',
    `REQ_PARAM`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '请求参数',
    `RESP_PARAM`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '返回参数',
    `USER_ID`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '操作用户ID',
    `USERNAME`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作用户名',
    `OPER_METHOD` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作方法',
    `URI`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URI',
    `IP`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求IP',
    `INSERT_TIME` datetime                                                      NOT NULL COMMENT '插入时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_NET
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_NET`;
CREATE TABLE `MONITOR_NET`
(
    `ID`            bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP_SOURCE`     varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址（来源）',
    `IP_TARGET`     varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址（目的地）',
    `IP_DESC`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址描述',
    `STATUS`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '状态（0：网络不通，1：网络正常）',
    `AVG_TIME`      double                                                        NULL DEFAULT NULL COMMENT '平均响应时间（毫秒）',
    `OFFLINE_COUNT` int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `MONITOR_ENV`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控环境',
    `MONITOR_GROUP` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控分组',
    `INSERT_TIME`   datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`   datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP_SOURCE` (`IP_SOURCE`) USING BTREE COMMENT '索引_IP地址（来源）',
    INDEX `NX_IP_TARGET` (`IP_TARGET`) USING BTREE COMMENT '索引_IP地址（目的地）',
    INDEX `MONITOR_NET_ENV_FK` (`MONITOR_ENV`) USING BTREE COMMENT '索引_监控环境',
    INDEX `MONITOR_NET_GROUP_FK` (`MONITOR_GROUP`) USING BTREE COMMENT '索引_监控分组',
    CONSTRAINT `MONITOR_NET_ENV_FK` FOREIGN KEY (`MONITOR_ENV`) REFERENCES `MONITOR_ENV` (`ENV_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `MONITOR_NET_GROUP_FK` FOREIGN KEY (`MONITOR_GROUP`) REFERENCES `MONITOR_GROUP` (`GROUP_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '网络信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_NET_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_NET_HISTORY`;
CREATE TABLE `MONITOR_NET_HISTORY`
(
    `ID`            bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `NET_ID`        bigint(20)                                                    NOT NULL COMMENT '网络主表ID',
    `IP_SOURCE`     varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址（来源）',
    `IP_TARGET`     varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址（目的地）',
    `IP_DESC`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址描述',
    `STATUS`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '状态（0：网络不通，1：网络正常）',
    `AVG_TIME`      double                                                        NULL DEFAULT NULL COMMENT '平均响应时间（毫秒）',
    `OFFLINE_COUNT` int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `INSERT_TIME`   datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`   datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP_SOURCE` (`IP_SOURCE`) USING BTREE COMMENT '索引_IP地址（来源）',
    INDEX `NX_IP_TARGET` (`IP_TARGET`) USING BTREE COMMENT '索引_IP地址（目的地）',
    INDEX `NX_NET_ID` (`NET_ID`) USING BTREE COMMENT '索引_网络主表ID',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '网络信息历史记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for MONITOR_REALTIME_MONITORING
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_REALTIME_MONITORING`;
CREATE TABLE `MONITOR_REALTIME_MONITORING`
(
    `ID`            bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `TYPE`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '监控类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）',
    `CODE`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '监控编号',
    `IS_SENT_ALARM` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '是否已经发送了告警（1：是，0：否）',
    `INSERT_TIME`   datetime                                                     NOT NULL COMMENT '插入时间',
    `UPDATE_TIME`   datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_TYPE_CODE` (`TYPE`, `CODE`) USING BTREE COMMENT '索引_类型_编码'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '实时监控表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_ROLE`;
CREATE TABLE `MONITOR_ROLE`
(
    `ID`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `ROLE_NAME`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
    `CREATE_TIME` datetime                                                      NOT NULL COMMENT '创建时间',
    `UPDATE_TIME` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '监控用户角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER`;
CREATE TABLE `MONITOR_SERVER`
(
    `ID`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`             varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `SERVER_NAME`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '服务器名',
    `SERVER_SUMMARY` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器摘要',
    `IS_ONLINE`      varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '服务器状态（0：离线，1：在线）',
    `OFFLINE_COUNT`  int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `CONN_FREQUENCY` int(8)                                                        NOT NULL COMMENT '连接频率',
    `MONITOR_ENV`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控环境',
    `MONITOR_GROUP`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控分组',
    `INSERT_TIME`    datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    INDEX `MONITOR_SERVER_ENV_FK` (`MONITOR_ENV`) USING BTREE COMMENT '索引_监控环境',
    INDEX `MONITOR_SERVER_GROUP_FK` (`MONITOR_GROUP`) USING BTREE COMMENT '索引_监控分组',
    CONSTRAINT `MONITOR_SERVER_ENV_FK` FOREIGN KEY (`MONITOR_ENV`) REFERENCES `MONITOR_ENV` (`ENV_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `MONITOR_SERVER_GROUP_FK` FOREIGN KEY (`MONITOR_GROUP`) REFERENCES `MONITOR_GROUP` (`GROUP_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for MONITOR_SERVER_CPU
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_CPU`;
CREATE TABLE `MONITOR_SERVER_CPU`
(
    `ID`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `CPU_NO`       int(8)                                                        NULL DEFAULT NULL COMMENT 'CPU序号',
    `CPU_MHZ`      int(11)                                                       NULL DEFAULT NULL COMMENT 'CPU频率（MHz）',
    `CPU_VENDOR`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CPU卖主',
    `CPU_MODEL`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CPU的类别，如：Celeron',
    `CPU_USER`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU用户使用率',
    `CPU_SYS`      double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU系统使用率',
    `CPU_WAIT`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU等待率',
    `CPU_NICE`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU错误率',
    `CPU_COMBINED` double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU使用率',
    `CPU_IDLE`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU剩余率',
    `INSERT_TIME`  datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP地址'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器CPU表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_CPU_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_CPU_HISTORY`;
CREATE TABLE `MONITOR_SERVER_CPU_HISTORY`
(
    `ID`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `CPU_NO`       int(8)                                                        NULL DEFAULT NULL COMMENT 'CPU序号',
    `CPU_MHZ`      int(11)                                                       NULL DEFAULT NULL COMMENT 'CPU频率（MHz）',
    `CPU_VENDOR`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CPU卖主',
    `CPU_MODEL`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CPU的类别，如：Celeron',
    `CPU_USER`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU用户使用率',
    `CPU_SYS`      double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU系统使用率',
    `CPU_WAIT`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU等待率',
    `CPU_NICE`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU错误率',
    `CPU_COMBINED` double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU使用率',
    `CPU_IDLE`     double(16, 4)                                                 NULL DEFAULT NULL COMMENT 'CPU剩余率',
    `INSERT_TIME`  datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP地址',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_服务器CPU新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_服务器CPU更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器CPU历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_DISK
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_DISK`;
CREATE TABLE `MONITOR_SERVER_DISK`
(
    `ID`            bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`            varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `DISK_NO`       int(8)                                                        NULL DEFAULT NULL COMMENT '磁盘序号',
    `DEV_NAME`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分区的盘符名称',
    `DIR_NAME`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分区的盘符路径',
    `TYPE_NAME`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '磁盘类型名，比如本地硬盘、光驱、网络文件系统等',
    `SYS_TYPE_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '磁盘类型，比如 FAT32、NTFS',
    `TOTAL`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘总大小（单位：byte）',
    `FREE`          bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘剩余大小（单位：byte）',
    `USED`          bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘已用大小（单位：byte）',
    `AVAIL`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘可用大小（单位：byte）',
    `USE_PERCENT`   double(16, 4)                                                 NULL DEFAULT NULL COMMENT '磁盘资源的利用率',
    `INSERT_TIME`   datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`   datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器磁盘表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_DISK_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_DISK_HISTORY`;
CREATE TABLE `MONITOR_SERVER_DISK_HISTORY`
(
    `ID`            bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`            varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `DISK_NO`       int(8)                                                        NULL DEFAULT NULL COMMENT '磁盘序号',
    `DEV_NAME`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分区的盘符名称',
    `DIR_NAME`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分区的盘符路径',
    `TYPE_NAME`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '磁盘类型名，比如本地硬盘、光驱、网络文件系统等',
    `SYS_TYPE_NAME` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '磁盘类型，比如 FAT32、NTFS',
    `TOTAL`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘总大小（单位：byte）',
    `FREE`          bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘剩余大小（单位：byte）',
    `USED`          bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘已用大小（单位：byte）',
    `AVAIL`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '磁盘可用大小（单位：byte）',
    `USE_PERCENT`   double(16, 4)                                                 NULL DEFAULT NULL COMMENT '磁盘资源的利用率',
    `INSERT_TIME`   datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`   datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_服务器磁盘新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_服务器磁盘更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器磁盘历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_LOAD_AVERAGE
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_LOAD_AVERAGE`;
CREATE TABLE `MONITOR_SERVER_LOAD_AVERAGE`
(
    `ID`                      bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                      varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
    `LOGICAL_PROCESSOR_COUNT` int(4)                                                       NULL DEFAULT NULL COMMENT 'CPU逻辑核数量',
    `ONE`                     double(16, 4)                                                NULL DEFAULT NULL COMMENT '1分钟负载平均值',
    `FIVE`                    double(16, 4)                                                NULL DEFAULT NULL COMMENT '5分钟负载平均值',
    `FIFTEEN`                 double(16, 4)                                                NULL DEFAULT NULL COMMENT '15分钟负载平均值',
    `INSERT_TIME`             datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`             datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器平均负载表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_LOAD_AVERAGE_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_LOAD_AVERAGE_HISTORY`;
CREATE TABLE `MONITOR_SERVER_LOAD_AVERAGE_HISTORY`
(
    `ID`                      bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                      varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
    `LOGICAL_PROCESSOR_COUNT` int(4)                                                       NULL DEFAULT NULL COMMENT 'CPU逻辑核数量',
    `ONE`                     double(16, 4)                                                NULL DEFAULT NULL COMMENT '1分钟负载平均值',
    `FIVE`                    double(16, 4)                                                NULL DEFAULT NULL COMMENT '5分钟负载平均值',
    `FIFTEEN`                 double(16, 4)                                                NULL DEFAULT NULL COMMENT '15分钟负载平均值',
    `INSERT_TIME`             datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`             datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_服务器平均负载新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_服务器平均负载更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器平均负载历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_MEMORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_MEMORY`;
CREATE TABLE `MONITOR_SERVER_MEMORY`
(
    `ID`                bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
    `MEM_TOTAL`         bigint(20)                                                   NULL DEFAULT NULL COMMENT '物理内存总量（单位：byte）',
    `MEM_USED`          bigint(20)                                                   NULL DEFAULT NULL COMMENT '物理内存使用量（单位：byte）',
    `MEM_FREE`          bigint(20)                                                   NULL DEFAULT NULL COMMENT '物理内存剩余量（单位：byte）',
    `MEN_USED_PERCENT`  double(16, 4)                                                NULL DEFAULT NULL COMMENT '物理内存使用率',
    `SWAP_TOTAL`        bigint(20)                                                   NULL DEFAULT NULL COMMENT '交换区总量（单位：byte）',
    `SWAP_USED`         bigint(20)                                                   NULL DEFAULT NULL COMMENT '交换区使用量（单位：byte）',
    `SWAP_FREE`         bigint(20)                                                   NULL DEFAULT NULL COMMENT '交换区剩余量（单位：byte）',
    `SWAP_USED_PERCENT` double(16, 4)                                                NULL DEFAULT NULL COMMENT '交换区使用率',
    `INSERT_TIME`       datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`       datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器内存表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_MEMORY_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_MEMORY_HISTORY`;
CREATE TABLE `MONITOR_SERVER_MEMORY_HISTORY`
(
    `ID`                bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
    `MEM_TOTAL`         bigint(20)                                                   NULL DEFAULT NULL COMMENT '物理内存总量（单位：byte）',
    `MEM_USED`          bigint(20)                                                   NULL DEFAULT NULL COMMENT '物理内存使用量（单位：byte）',
    `MEM_FREE`          bigint(20)                                                   NULL DEFAULT NULL COMMENT '物理内存剩余量（单位：byte）',
    `MEN_USED_PERCENT`  double(16, 4)                                                NULL DEFAULT NULL COMMENT '物理内存使用率',
    `SWAP_TOTAL`        bigint(20)                                                   NULL DEFAULT NULL COMMENT '交换区总量（单位：byte）',
    `SWAP_USED`         bigint(20)                                                   NULL DEFAULT NULL COMMENT '交换区使用量（单位：byte）',
    `SWAP_FREE`         bigint(20)                                                   NULL DEFAULT NULL COMMENT '交换区剩余量（单位：byte）',
    `SWAP_USED_PERCENT` double(16, 4)                                                NULL DEFAULT NULL COMMENT '交换区使用率',
    `INSERT_TIME`       datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`       datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_服务器内存新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_服务器内存更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器内存历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_NETCARD
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_NETCARD`;
CREATE TABLE `MONITOR_SERVER_NETCARD`
(
    `ID`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `NET_NO`       int(8)                                                        NULL DEFAULT NULL COMMENT '网卡序号',
    `NAME`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '网卡名字',
    `TYPE`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '网卡类型',
    `ADDRESS`      varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '网卡地址',
    `MASK`         varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '子网掩码',
    `BROADCAST`    varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '广播地址',
    `HW_ADDR`      varchar(17) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'MAC地址',
    `DESCRIPTION`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网卡信息描述',
    `RX_BYTES`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收到的总字节数',
    `RX_PACKETS`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收的总包数',
    `RX_ERRORS`    bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收到的错误包数',
    `RX_DROPPED`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收时丢弃的包数',
    `TX_BYTES`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送的总字节数',
    `TX_PACKETS`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送的总包数',
    `TX_ERRORS`    bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送时的错误包数',
    `TX_DROPPED`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送时丢弃的包数',
    `DOWNLOAD_BPS` double(22, 4)                                                 NULL DEFAULT NULL COMMENT '下载速度',
    `UPLOAD_BPS`   double(22, 4)                                                 NULL DEFAULT NULL COMMENT '上传速度',
    `INSERT_TIME`  datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器网卡表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_NETCARD_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_NETCARD_HISTORY`;
CREATE TABLE `MONITOR_SERVER_NETCARD_HISTORY`
(
    `ID`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `NET_NO`       int(8)                                                        NULL DEFAULT NULL COMMENT '网卡序号',
    `NAME`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '网卡名字',
    `TYPE`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '网卡类型',
    `ADDRESS`      varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '网卡地址',
    `MASK`         varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '子网掩码',
    `BROADCAST`    varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '广播地址',
    `HW_ADDR`      varchar(17) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'MAC地址',
    `DESCRIPTION`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网卡信息描述',
    `RX_BYTES`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收到的总字节数',
    `RX_PACKETS`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收的总包数',
    `RX_ERRORS`    bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收到的错误包数',
    `RX_DROPPED`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '接收时丢弃的包数',
    `TX_BYTES`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送的总字节数',
    `TX_PACKETS`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送的总包数',
    `TX_ERRORS`    bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送时的错误包数',
    `TX_DROPPED`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '发送时丢弃的包数',
    `DOWNLOAD_BPS` double(22, 4)                                                 NULL DEFAULT NULL COMMENT '下载速度',
    `UPLOAD_BPS`   double(22, 4)                                                 NULL DEFAULT NULL COMMENT '上传速度',
    `INSERT_TIME`  datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    INDEX `NX_ADDRESS` (`ADDRESS`) USING BTREE COMMENT '索引_服务器网卡地址',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_服务器网卡新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_服务器网卡更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器网卡历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_OS
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_OS`;
CREATE TABLE `MONITOR_SERVER_OS`
(
    `ID`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`           varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `SERVER_NAME`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器名',
    `OS_NAME`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统名称',
    `OS_ARCH`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作系统架构',
    `OS_VERSION`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作系统版本',
    `USER_NAME`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
    `USER_HOME`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户主目录',
    `OS_TIME_ZONE` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作系统时区',
    `INSERT_TIME`  datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器操作系统表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_POWER_SOURCES
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_POWER_SOURCES`;
CREATE TABLE `MONITOR_SERVER_POWER_SOURCES`
(
    `ID`                         bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                         varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `POWER_SOURCES_NO`           int(8)                                                        NULL DEFAULT NULL COMMENT '电池序号',
    `NAME`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统级别的电源名称',
    `DEVICE_NAME`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备级别的电源名称',
    `REMAINING_CAPACITY_PERCENT` double(22, 4)                                                 NULL DEFAULT NULL COMMENT '剩余容量百分比',
    `TIME_REMAINING_ESTIMATED`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '操作系统估计的电源上剩余的估计时间（以毫秒为单位）',
    `TIME_REMAINING_INSTANT`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '估计的电源剩余时间（以毫秒为单位），由电池报告',
    `POWER_USAGE_RATE`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）',
    `VOLTAGE`                    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池电压，以伏特为单位',
    `AMPERAGE`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）',
    `IS_POWER_ON_LINE`           varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '是否已插入外部电源',
    `IS_CHARGING`                varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '电池是否正在充电',
    `IS_DISCHARGING`             varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '电池是否正在放电',
    `CURRENT_CAPACITY`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的当前（剩余）容量',
    `MAX_CAPACITY`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的最大容量',
    `DESIGN_CAPACITY`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的设计（原始）容量',
    `CYCLE_COUNT`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电池的循环计数（如果知道）',
    `CHEMISTRY`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电池化学成分（例如，锂离子电池）',
    `MANUFACTURE_DATE`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的生产日期',
    `MANUFACTURER`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电池制造商的名称',
    `SERIAL_NUMBER`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池的序列号',
    `TEMPERATURE`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电池温度，以摄氏度为单位',
    `INSERT_TIME`                datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`                datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器电池表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_PROCESS
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_PROCESS`;
CREATE TABLE `MONITOR_SERVER_PROCESS`
(
    `ID`                        bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`                        varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `PROCESS_ID`                int(8)                                                        NOT NULL COMMENT '进程ID',
    `NAME`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '进程名',
    `PATH`                      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '执行进程的完整路径',
    `COMMAND_LINE`              text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '进程命令行',
    `CURRENT_WORKING_DIRECTORY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '进程当前的工作目录',
    `USER`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `STATE`                     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '进程执行状态',
    `UP_TIME`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '进程已启动的毫秒数',
    `START_TIME`                datetime                                                      NULL DEFAULT NULL COMMENT '进程的开始时间',
    `CPU_LOAD_CUMULATIVE`       double(16, 4)                                                 NULL DEFAULT NULL COMMENT '进程的累积CPU使用率',
    `BITNESS`                   varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '进程的位数',
    `MEMORY_SIZE`               bigint(20)                                                    NULL DEFAULT NULL COMMENT '占用内存大小（单位：byte）',
    `INSERT_TIME`               datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`               datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器进程表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_PROCESS_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_PROCESS_HISTORY`;
CREATE TABLE `MONITOR_SERVER_PROCESS_HISTORY`
(
    `ID`          bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`          varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
    `PROCESS_NUM` int(8)                                                       NULL DEFAULT NULL COMMENT '正在运行的进程数',
    `INSERT_TIME` datetime                                                     NOT NULL COMMENT '新增时间',
    `UPDATE_TIME` datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_服务器进程新增时间',
    INDEX `NX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE COMMENT '索引_服务器进程更新时间'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器进程历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_SERVER_SENSORS
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_SERVER_SENSORS`;
CREATE TABLE `MONITOR_SERVER_SENSORS`
(
    `ID`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `IP`              varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'IP地址',
    `CPU_TEMPERATURE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'CPU温度（以摄氏度为单位）（如果可用）',
    `CPU_VOLTAGE`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'CPU电压（以伏特为单位）（如果可用）',
    `FAN_SPEED`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风扇的转速（rpm）（如果可用）',
    `INSERT_TIME`     datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_IP` (`IP`) USING BTREE COMMENT '索引_IP'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '服务器传感器表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_TCP
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_TCP`;
CREATE TABLE `MONITOR_TCP`
(
    `ID`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `HOSTNAME_SOURCE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主机名（来源）',
    `HOSTNAME_TARGET` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主机名（目的地）',
    `PORT_TARGET`     int(8)                                                        NOT NULL COMMENT '端口号',
    `DESCR`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `AVG_TIME`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '平均响应时间（毫秒）',
    `STATUS`          varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '状态（0：不通，1：正常）',
    `OFFLINE_COUNT`   int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `MONITOR_ENV`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控环境',
    `MONITOR_GROUP`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控分组',
    `INSERT_TIME`     datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_HOSTNAME_SOURCE` (`HOSTNAME_SOURCE`) USING BTREE COMMENT '索引_主机名（来源）',
    INDEX `NX_HOSTNAME_TARGET` (`HOSTNAME_TARGET`) USING BTREE COMMENT '索引_主机名（目的地）',
    INDEX `NX_PORT_TARGET` (`PORT_TARGET`) USING BTREE COMMENT '索引_端口号',
    INDEX `MONITOR_TCP_ENV_FK` (`MONITOR_ENV`) USING BTREE COMMENT '索引_监控环境',
    INDEX `MONITOR_TCP_GROUP_FK` (`MONITOR_GROUP`) USING BTREE COMMENT '索引_监控分组',
    CONSTRAINT `MONITOR_TCP_ENV_FK` FOREIGN KEY (`MONITOR_ENV`) REFERENCES `MONITOR_ENV` (`ENV_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `MONITOR_TCP_GROUP_FK` FOREIGN KEY (`MONITOR_GROUP`) REFERENCES `MONITOR_GROUP` (`GROUP_NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'TCP信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for MONITOR_TCP_HISTORY
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_TCP_HISTORY`;
CREATE TABLE `MONITOR_TCP_HISTORY`
(
    `ID`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `TCP_ID`          bigint(20)                                                    NOT NULL COMMENT 'TCP主表ID',
    `HOSTNAME_SOURCE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主机名（来源）',
    `HOSTNAME_TARGET` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '主机名（目的地）',
    `PORT_TARGET`     int(8)                                                        NOT NULL COMMENT '端口号',
    `DESCR`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `AVG_TIME`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '平均响应时间（毫秒）',
    `STATUS`          varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '状态（0：不通，1：正常）',
    `OFFLINE_COUNT`   int(8)                                                        NULL DEFAULT NULL COMMENT '离线次数',
    `INSERT_TIME`     datetime                                                      NOT NULL COMMENT '新增时间',
    `UPDATE_TIME`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_TCP_ID` (`TCP_ID`) USING BTREE COMMENT '索引_TCP主表ID',
    INDEX `NX_INSERT_TIME` (`INSERT_TIME`) USING BTREE COMMENT '索引_插入时间',
    INDEX `NX_HOSTNAME_SOURCE` (`HOSTNAME_SOURCE`) USING BTREE COMMENT '索引_主机名（来源）',
    INDEX `NX_HOSTNAME_TARGET` (`HOSTNAME_TARGET`) USING BTREE COMMENT '索引_主机名（目的地）',
    INDEX `NX_PORT_TARGET` (`PORT_TARGET`) USING BTREE COMMENT '索引_端口号'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'TCP信息历史记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for MONITOR_USER
-- ----------------------------
DROP TABLE IF EXISTS `MONITOR_USER`;
CREATE TABLE `MONITOR_USER`
(
    `ID`            bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `ACCOUNT`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '账号',
    `USERNAME`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '用户名',
    `PASSWORD`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '密码',
    `ROLE_ID`       bigint(20)                                                     NOT NULL COMMENT '角色ID',
    `EMAIL`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '电子邮箱',
    `REMARKS`       varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `REGISTER_TIME` datetime                                                       NOT NULL COMMENT '注册时间',
    `UPDATE_TIME`   datetime                                                       NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`ID`) USING BTREE,
    INDEX `NX_USER_ROLE_ID` (`ROLE_ID`) USING BTREE COMMENT '索引_角色ID',
    UNIQUE INDEX `NX_ACCOUNT` (`ACCOUNT`) USING BTREE COMMENT '索引_账号',
    CONSTRAINT `MONITOR_USER_ROLE_FK` FOREIGN KEY (`ROLE_ID`) REFERENCES `MONITOR_ROLE` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '监控用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `BLOB_DATA`     blob                                                          NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `SCHED_NAME` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `CALENDAR`      blob                                                          NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`
(
    `SCHED_NAME`      varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`    varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`   varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TIME_ZONE_ID`    varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `ENTRY_ID`          varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `TRIGGER_NAME`      varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`     varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `INSTANCE_NAME`     varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `FIRED_TIME`        bigint(13)                                                    NOT NULL,
    `SCHED_TIME`        bigint(13)                                                    NOT NULL,
    `PRIORITY`          int(11)                                                       NOT NULL,
    `STATE`             varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `JOB_NAME`          varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `JOB_GROUP`         varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `IS_NONCONCURRENT`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
    INDEX `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
    INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
    INDEX `IDX_QRTZ_FT_J_G` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_FT_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_FT_T_G` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_FT_TG` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_NAME`          varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_GROUP`         varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `DESCRIPTION`       varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `JOB_CLASS_NAME`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `IS_DURABLE`        varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `IS_NONCONCURRENT`  varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `IS_UPDATE_DATA`    varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `JOB_DATA`          blob                                                          NULL,
    PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
    INDEX `IDX_QRTZ_J_GRP` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`
(
    `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `LOCK_NAME`  varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`
(
    `SCHED_NAME`        varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `INSTANCE_NAME`     varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `LAST_CHECKIN_TIME` bigint(13)                                                    NOT NULL,
    `CHECKIN_INTERVAL`  bigint(13)                                                    NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`
(
    `SCHED_NAME`      varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`    varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`   varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `REPEAT_COUNT`    bigint(7)                                                     NOT NULL,
    `REPEAT_INTERVAL` bigint(12)                                                    NOT NULL,
    `TIMES_TRIGGERED` bigint(10)                                                    NOT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`
(
    `SCHED_NAME`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `STR_PROP_1`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `STR_PROP_2`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `STR_PROP_3`    varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `INT_PROP_1`    int(11)                                                       NULL DEFAULT NULL,
    `INT_PROP_2`    int(11)                                                       NULL DEFAULT NULL,
    `LONG_PROP_1`   bigint(20)                                                    NULL DEFAULT NULL,
    `LONG_PROP_2`   bigint(20)                                                    NULL DEFAULT NULL,
    `DEC_PROP_1`    decimal(13, 4)                                                NULL DEFAULT NULL,
    `DEC_PROP_2`    decimal(13, 4)                                                NULL DEFAULT NULL,
    `BOOL_PROP_1`   varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    `BOOL_PROP_2`   varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`
(
    `SCHED_NAME`     varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_NAME`   varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `TRIGGER_GROUP`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_NAME`       varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `JOB_GROUP`      varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `DESCRIPTION`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `NEXT_FIRE_TIME` bigint(13)                                                    NULL DEFAULT NULL,
    `PREV_FIRE_TIME` bigint(13)                                                    NULL DEFAULT NULL,
    `PRIORITY`       int(11)                                                       NULL DEFAULT NULL,
    `TRIGGER_STATE`  varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `TRIGGER_TYPE`   varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `START_TIME`     bigint(13)                                                    NOT NULL,
    `END_TIME`       bigint(13)                                                    NULL DEFAULT NULL,
    `CALENDAR_NAME`  varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `MISFIRE_INSTR`  smallint(2)                                                   NULL DEFAULT NULL,
    `JOB_DATA`       blob                                                          NULL,
    PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_J` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_JG` (`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_C` (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
    INDEX `IDX_QRTZ_T_G` (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
    INDEX `IDX_QRTZ_T_STATE` (`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
    INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`,
                                           `TRIGGER_STATE`) USING BTREE,
    CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SPRING_SESSION
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION`;
CREATE TABLE `SPRING_SESSION`
(
    `PRIMARY_ID`            char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NOT NULL,
    `SESSION_ID`            char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NOT NULL,
    `CREATION_TIME`         bigint(20)                                                    NOT NULL,
    `LAST_ACCESS_TIME`      bigint(20)                                                    NOT NULL,
    `MAX_INACTIVE_INTERVAL` int(11)                                                       NOT NULL,
    `EXPIRY_TIME`           bigint(20)                                                    NOT NULL,
    `PRINCIPAL_NAME`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`PRIMARY_ID`) USING BTREE,
    UNIQUE INDEX `SPRING_SESSION_IX1` (`SESSION_ID`) USING BTREE,
    INDEX `SPRING_SESSION_IX2` (`EXPIRY_TIME`) USING BTREE,
    INDEX `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for SPRING_SESSION_ATTRIBUTES
-- ----------------------------
DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES`
(
    `SESSION_PRIMARY_ID` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NOT NULL,
    `ATTRIBUTE_NAME`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `ATTRIBUTE_BYTES`    blob                                                          NOT NULL,
    PRIMARY KEY (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`) USING BTREE,
    CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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

INSERT INTO `MONITOR_ENV`
VALUES (1, 'dev', '开发环境', 'admin', NULL, '2022-06-14 11:18:03', NULL);
INSERT INTO `MONITOR_ENV`
VALUES (2, 'test', '测试环境', 'admin', NULL, '2022-06-14 11:18:12', NULL);
INSERT INTO `MONITOR_ENV`
VALUES (3, 'pre', '预生产环境', 'admin', NULL, '2022-06-14 11:18:23', NULL);
INSERT INTO `MONITOR_ENV`
VALUES (4, 'prod', '生产环境', 'admin', NULL, '2022-06-14 11:18:32', NULL);

COMMIT;
