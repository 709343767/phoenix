--------------------------------------------------------
--  文件已创建 - 星期一-四月-13-2020   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table MONITOR_ALARM_DEFINITION
--------------------------------------------------------

  CREATE TABLE "MONITOR_ALARM_DEFINITION" 
   (	"ID" NUMBER(*,0), 
	"TYPE" VARCHAR2(8), 
	"FIRST_CLASS" VARCHAR2(255), 
	"SECOND_CLASS" VARCHAR2(255), 
	"THIRD_CLASS" VARCHAR2(255), 
	"GRADE" VARCHAR2(5), 
	"CODE" VARCHAR2(8), 
	"CONTENT" VARCHAR2(255), 
	"TITLE" VARCHAR2(125)
   ) ;

   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."ID" IS '主键';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."TYPE" IS '类型';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."FIRST_CLASS" IS '一级分类';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."SECOND_CLASS" IS '二级分类';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."THIRD_CLASS" IS '三级分类';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."GRADE" IS '告警级别';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."CODE" IS '告警编码';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."CONTENT" IS '告警内容';
   COMMENT ON COLUMN "MONITOR_ALARM_DEFINITION"."TITLE" IS '告警标题';
--------------------------------------------------------
--  DDL for Index MONITOR_ALARM_DEFINITION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MONITOR_ALARM_DEFINITION_PK" ON "MONITOR_ALARM_DEFINITION" ("ID") 
  ;
--------------------------------------------------------
--  Constraints for Table MONITOR_ALARM_DEFINITION
--------------------------------------------------------

  ALTER TABLE "MONITOR_ALARM_DEFINITION" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "MONITOR_ALARM_DEFINITION" ADD CONSTRAINT "MONITOR_ALARM_DEFINITION_PK" PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
