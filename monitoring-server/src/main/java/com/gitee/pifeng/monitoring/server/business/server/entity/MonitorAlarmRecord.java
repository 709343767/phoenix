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
 * 监控告警记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/13 15:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_ALARM_RECORD")
public class MonitorAlarmRecord {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 告警代码，使用UUID
     */
    @TableField("CODE")
    private String code;

    /**
     * 告警类型（SERVER、NET、INSTANCE、DATABASE、CUSTOM）
     */
    @TableField("TYPE")
    private String type;

    /**
     * 告警方式（SMS、MAIL、...）
     */
    @TableField("WAY")
    private String way;

    /**
     * 告警级别（INFO、WARM、ERROR、FATAL）
     */
    @TableField("LEVEL")
    private String level;

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

    /**
     * 告警标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 告警内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 告警发送状态（0：失败；1：成功）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 被告警人号码（手机号码、电子邮箱、...）
     */
    @TableField("NUMBER")
    private String number;

}
