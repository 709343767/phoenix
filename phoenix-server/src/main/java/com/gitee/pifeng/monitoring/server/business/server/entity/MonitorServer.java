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
 * 服务器表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/11 15:33
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER")
public class MonitorServer {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 服务器名
     */
    @TableField("SERVER_NAME")
    private String serverName;

    /**
     * 服务器摘要
     */
    @TableField("SERVER_SUMMARY")
    private String serverSummary;

    /**
     * 服务器状态（0：离线，1：在线）
     */
    @TableField("IS_ONLINE")
    private String isOnline;

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
     * 离线次数
     */
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    /**
     * 连接频率
     */
    @TableField("CONN_FREQUENCY")
    private Integer connFrequency;

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
