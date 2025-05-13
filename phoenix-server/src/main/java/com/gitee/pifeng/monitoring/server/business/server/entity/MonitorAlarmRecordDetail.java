package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_ALARM_RECORD_DETAIL")
public class MonitorAlarmRecordDetail {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 告警记录ID
     */
    @TableField("ALARM_RECORD_ID")
    private Long alarmRecordId;

    /**
     * 告警代码，使用UUID
     */
    @TableField("CODE")
    private String code;

    /**
     * 告警方式（SMS、MAIL、...）
     */
    @TableField("WAY")
    private String way;

    /**
     * 被告警人号码（手机号码、电子邮箱、...）
     */
    @TableField("NUMBER")
    private String number;

    /**
     * 告警发送状态（0：失败；1：成功）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 异常信息
     */
    @TableField("EXC_MESSAGE")
    private String excMessage;

    /**
     * 告警时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 告警结果获取时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
