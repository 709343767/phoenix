package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * TCP信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_TCP")
public class MonitorTcp {

    /**
     * 主键ID
     */
    @TableId("ID")
    private Long id;

    /**
     * 主机名（来源）
     */
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    /**
     * 主机名（目的地）
     */
    @TableField("HOSTNAME_TARGET")
    private String hostnameTarget;

    /**
     * 端口号
     */
    @TableField("PORT_TARGET")
    private Integer portTarget;

    /**
     * 描述
     */
    @TableField("DESCR")
    private String descr;

    /**
     * 状态（0：不通，1：正常）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 是否开启监控（0：不开启监控；1：开启监控）
     */
    @TableField("IS_ENABLE_MONITOR")
    private String isEnableMonitor;

    /**
     * 是否开启告警（0：不开启告警；1：开启告警）
     */
    @TableField("IS_ENABLE_ALARM")
    private String isEnableAlarm;

    /**
     * 平均响应时间（毫秒）
     */
    @TableField("AVG_TIME")
    private Long avgTime;

    /**
     * 离线次数
     */
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    /**
     * 新增时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 监控环境
     */
    @TableField("MONITOR_ENV")
    private String monitorEnv;

    /**
     * 监控分组
     */
    @TableField("MONITOR_GROUP")
    private String monitorGroup;

}
