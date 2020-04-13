/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50639
Source Host           : localhost:3306
Source Database       : monitoring

Target Server Type    : MYSQL
Target Server Version : 50639
File Encoding         : 65001

Date: 2020-04-13 10:08:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for monitor_alarm_definition
-- ----------------------------
DROP TABLE IF EXISTS `monitor_alarm_definition`;
CREATE TABLE `monitor_alarm_definition` (
  `ID` int(8) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TYPE` varchar(8) DEFAULT NULL COMMENT '类型',
  `FIRST_CLASS` varchar(255) DEFAULT NULL COMMENT '一级分类',
  `SECOND_CLASS` varchar(255) DEFAULT NULL COMMENT '二级分类',
  `THIRD_CLASS` varchar(255) DEFAULT NULL COMMENT '三级分类',
  `GRADE` varchar(5) DEFAULT NULL COMMENT '告警级别',
  `CODE` varchar(8) DEFAULT NULL COMMENT '告警编码',
  `TITLE` varchar(125) DEFAULT NULL COMMENT '告警标题',
  `CONTENT` varchar(255) DEFAULT NULL COMMENT '告警内容',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
