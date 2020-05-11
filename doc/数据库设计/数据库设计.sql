/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/5/10 21:44:55                           */
/*==============================================================*/


drop table if exists MONITOR_ALARM_DEFINITION;

drop table if exists MONITOR_ALARM_RECORD;

drop table if exists MONITOR_INSTANCE;

drop table if exists MONITOR_NET;

drop table if exists MONITOR_SERVER;

drop table if exists MONITOR_SERVER_CPU;

drop table if exists MONITOR_SERVER_DISK;

drop table if exists MONITOR_SERVER_JVM;

drop table if exists MONITOR_SERVER_MEMORY;

drop table if exists MONITOR_SERVER_NETCARD;

/*==============================================================*/
/* Table: MONITOR_ALARM_DEFINITION                              */
/*==============================================================*/
create table MONITOR_ALARM_DEFINITION
(
   ID                   int(8) not null comment '主键ID',
   TYPE                 varchar(8) comment '告警类型（SERVER、NET、INSTANCE、CUSTOM）',
   FIRST_CLASS          varchar(255) comment '一级分类',
   SECOND_CLASS         varchar(255) comment '二级分类',
   THIRD_CLASS          varchar(255) comment '三级分类',
   GRADE                varchar(5) comment '告警级别（INFO、WARM、ERROR、FATAL）',
   CODE                 varchar(8) comment '告警编码',
   TITLE                varchar(125) comment '告警标题',
   CONTENT              varchar(255) comment '告警内容',
   primary key (ID)
);

alter table MONITOR_ALARM_DEFINITION comment '告警定义表';

/*==============================================================*/
/* Table: MONITOR_ALARM_RECORD                                  */
/*==============================================================*/
create table MONITOR_ALARM_RECORD
(
   ID                   int(8) not null comment '主键ID',
   TYPE                 varchar(8) comment '告警类型（SERVER、NET、INSTANCE、CUSTOM）',
   WAY                  varchar(4) comment '告警方式（SMS、MAIL）',
   LEVEL                varchar(5) comment '告警级别（INFO、WARM、ERROR、FATAL）',
   INSERT_TIME          datetime comment '告警时间',
   TITLE                varchar(125) comment '告警标题',
   CONTENT              varchar(255) comment '告警内容',
   STATUS               varchar(1) comment '告警发送状态（0：失败；1：成功）',
   PERSON               varchar(500) comment '被告警人',
   primary key (ID)
);

alter table MONITOR_ALARM_RECORD comment '告警记录表';

/*==============================================================*/
/* Table: MONITOR_INSTANCE                                      */
/*==============================================================*/
create table MONITOR_INSTANCE
(
   INSTANCE_ID          varchar(32) not null comment '应用实例ID',
   ENDPOINT             varchar(8) comment '端点（客户端<client>、代理端<<agent>、服务端<server>）',
   INSTANCE_NAME        varchar(5) comment '应用实例名',
   IP                   varchar(15) comment 'IP地址',
   INSET_TIME           datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   STATUS               varchar(1) comment '应用状态（0：离线，1：在线）',
   primary key (INSTANCE_ID)
);

alter table MONITOR_INSTANCE comment '应用实例表';

/*==============================================================*/
/* Table: MONITOR_NET                                           */
/*==============================================================*/
create table MONITOR_NET
(
   IP                   varchar(15) not null comment 'IP地址',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   STATUS               varchar(1) comment '状态（0：网络不通，1：网络正常）',
   primary key (IP)
);

alter table MONITOR_NET comment '网络信息表';

/*==============================================================*/
/* Table: MONITOR_SERVER                                        */
/*==============================================================*/
create table MONITOR_SERVER
(
   IP                   varchar(15) not null comment 'IP地址',
   SERVER_NAME          varchar(30) comment '服务器名',
   OS_NAME              varchar(50) comment '操作系统名称',
   OS_VERSION           varchar(50) comment '操作系统版本',
   USER_NAME            varchar(50) comment '用户名称',
   USER_HOME            varchar(150) comment '用户主目录',
   OS_TIME_ZONE         varchar(30) comment '操作系统时区',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   primary key (IP)
);

alter table MONITOR_SERVER comment '服务器表';

/*==============================================================*/
/* Table: MONITOR_SERVER_CPU                                    */
/*==============================================================*/
create table MONITOR_SERVER_CPU
(
   ID                   int(8) not null comment '主键id',
   IP                   varchar(15) comment 'IP地址',
   CPU_NO               int(8) comment 'CPU序号',
   CPU_MHZ              varchar(8) comment 'CPU频率',
   CPU_COMBINED         varchar(8) comment 'CPU使用率',
   CPU_IDLE             varchar(8) comment 'CPU剩余率',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   primary key (ID)
);

alter table MONITOR_SERVER_CPU comment '服务器CPU表';

/*==============================================================*/
/* Table: MONITOR_SERVER_DISK                                   */
/*==============================================================*/
create table MONITOR_SERVER_DISK
(
   ID                   int(8) not null comment '主键ID',
   IP                   varchar(15) comment 'IP地址',
   DISK_NO              int(8) comment '磁盘序号',
   DEV_NAME             varchar(15) comment '分区的盘符名称',
   DIR_NAME             varchar(30) comment '分区的盘符路径',
   TYPE_NAME            varchar(15) comment '磁盘类型名，比如本地硬盘、光驱、网络文件系统等',
   SYS_TYPE_NAME        varchar(15) comment '磁盘类型，比如 FAT32、NTFS',
   TOTAL                varchar(8) comment '磁盘总大小',
   FREE                 varchar(8) comment '磁盘剩余大小',
   USED                 varchar(8) comment '磁盘已用大小',
   AVAIL                varchar(8) comment '磁盘可用大小',
   USE_PERCENT          varchar(8) comment '磁盘资源的利用率',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   primary key (ID)
);

alter table MONITOR_SERVER_DISK comment '服务器磁盘表';

/*==============================================================*/
/* Table: MONITOR_SERVER_JVM                                    */
/*==============================================================*/
create table MONITOR_SERVER_JVM
(
   ID                   int(8) not null comment '主键ID',
   IP                   varchar(15) comment 'IP地址',
   JAVA_PATH            varchar(150) comment 'java安路径',
   JAVA_VENDOR          varchar(30) comment 'java运行时供应商',
   JAVA_VERSION         varchar(15) comment 'java版本',
   JAVA_NAME            varchar(15) comment 'java运行时名称',
   JVM_VERSION          varchar(15) comment 'java虚拟机版本',
   JVM_TOTAL_MEMORY     varchar(8) comment 'java虚拟机总内存',
   JVM_FREE_MEMORY      varchar(8) comment 'java虚拟机剩余内存',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   primary key (ID)
);

alter table MONITOR_SERVER_JVM comment '服务器JVM信息表';

/*==============================================================*/
/* Table: MONITOR_SERVER_MEMORY                                 */
/*==============================================================*/
create table MONITOR_SERVER_MEMORY
(
   ID                   int(8) not null comment '主键ID',
   IP                   varchar(15) comment 'IP地址',
   MEM_TOTAL            varchar(8) comment '物理内存总量',
   MEM_USED             varchar(8) comment '物理内存使用量',
   MEM_FREE             varchar(8) comment '物理内存剩余量',
   MEN_USED_PERCENT     varchar(8) comment '物理内存使用率',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   primary key (ID)
);

alter table MONITOR_SERVER_MEMORY comment '服务器内存表';

/*==============================================================*/
/* Table: MONITOR_SERVER_NETCARD                                */
/*==============================================================*/
create table MONITOR_SERVER_NETCARD
(
   ID                   int(8) not null comment '主键ID',
   IP                   varchar(15) comment 'IP地址',
   NET_NO               int(8) comment '网卡序号',
   NAME                 varchar(30) comment '网卡名字',
   TYPE                 varchar(30) comment '网卡类型',
   ADDRESS              varchar(30) comment '网卡地址',
   MASK                 varchar(15) comment '子网掩码',
   BROADCAST            varchar(15) comment '广播地址',
   INSERT_TIME          datetime comment '新增时间',
   UPDATE_TIME          datetime comment '更新时间',
   primary key (ID)
);

alter table MONITOR_SERVER_NETCARD comment '服务器网卡表';

alter table MONITOR_INSTANCE add constraint FK_Reference_5 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

alter table MONITOR_NET add constraint FK_Reference_7 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

alter table MONITOR_SERVER_CPU add constraint FK_Reference_1 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

alter table MONITOR_SERVER_DISK add constraint FK_Reference_2 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

alter table MONITOR_SERVER_JVM add constraint FK_Reference_3 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

alter table MONITOR_SERVER_MEMORY add constraint FK_Reference_4 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

alter table MONITOR_SERVER_NETCARD add constraint FK_Reference_6 foreign key (IP)
      references MONITOR_SERVER (IP) on delete restrict on update restrict;

