package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "告警代码，使用UUID")
    @TableField("CODE")
    private String code;

    @Schema(description = "告警定义编码")
    @TableField("ALARM_DEF_CODE")
    private String alarmDefCode;

    @Schema(description = "告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）")
    @TableField("TYPE")
    private String type;

    @Schema(description = "告警方式（SMS、MAIL、...）")
    @TableField("WAY")
    private String way;

    @Schema(description = "告警级别（INFO、WARM、ERROR、FATAL）")
    @TableField("LEVEL")
    private String level;

    @Schema(description = "告警时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "告警结果获取时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(description = "告警标题")
    @TableField("TITLE")
    private String title;

    @Schema(description = "告警内容")
    @TableField("CONTENT")
    private String content;

    @Schema(description = "告警发送状态（0：失败；1：成功）")
    @TableField("STATUS")
    private String status;

    @Schema(description = "被告警人号码（手机号码、电子邮箱、...）")
    @TableField("NUMBER")
    private String number;

}
