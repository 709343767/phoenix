package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 告警记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_ALARM_RECORD")
@Schema(description = "MonitorAlarmRecord对象")
public class MonitorAlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "告警代码，使用UUID")
    @TableField("CODE")
    private String code;

    @Schema(description = "告警定义编码")
    @TableField("ALARM_DEF_CODE")
    private String alarmDefCode;

    @Schema(description = "告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、NETWORK_DEVICE、CUSTOM）")
    @TableField("TYPE")
    private String type;

    @Schema(description = "告警级别（INFO、WARM、ERROR、FATAL）")
    @TableField("LEVEL")
    private String level;

    @Schema(description = "告警方式，多种方式用逗号分隔")
    @TableField("WAY")
    private String way;

    @Schema(description = "告警标题")
    @TableField("TITLE")
    private String title;

    @Schema(description = "告警内容")
    @TableField("CONTENT")
    private String content;

    @Schema(description = "不发送告警原因")
    @TableField("NOT_SEND_REASON")
    private String notSendReason;

    @Schema(description = "插入时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
