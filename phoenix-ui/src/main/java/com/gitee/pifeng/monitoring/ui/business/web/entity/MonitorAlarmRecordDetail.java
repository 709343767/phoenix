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
 * 告警记录详情表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-05-03
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_ALARM_RECORD_DETAIL")
@Schema(description = "MonitorAlarmRecordDetail对象")
public class MonitorAlarmRecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "告警记录ID")
    @TableField("ALARM_RECORD_ID")
    private Long alarmRecordId;

    @Schema(description = "告警方式（SMS、MAIL、...）")
    @TableField("WAY")
    private String way;

    @Schema(description = "被告警人号码（手机号码、电子邮箱、...）")
    @TableField("NUMBER")
    private String number;

    @Schema(description = "告警发送状态（0：失败；1：成功）")
    @TableField("STATUS")
    private String status;

    @Schema(description = "异常信息")
    @TableField("EXC_MESSAGE")
    private String excMessage;

    @Schema(description = "告警时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "告警结果获取时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
