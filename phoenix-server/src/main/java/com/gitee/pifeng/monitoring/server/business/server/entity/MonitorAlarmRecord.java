package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.*;
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
     * 告警定义编码
     */
    @TableField("ALARM_DEF_CODE")
    private String alarmDefCode;

    /**
     * 告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）
     */
    @TableField("TYPE")
    private String type;

    /**
     * 告警级别（INFO、WARM、ERROR、FATAL）
     */
    @TableField("LEVEL")
    private String level;

    /**
     * 告警方式，多种方式用逗号分隔
     */
    @TableField(value = "WAY", updateStrategy = FieldStrategy.IGNORED)
    private String way;

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
     * 不发送告警原因
     */
    @TableField("NOT_SEND_REASON")
    private String notSendReason;

    /**
     * 插入时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
