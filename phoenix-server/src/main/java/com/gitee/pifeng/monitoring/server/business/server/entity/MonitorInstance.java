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
 * 应用实例表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/10 22:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_INSTANCE")
public class MonitorInstance {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;

    /**
     * 端点（client、agent、server）
     */
    @TableField("ENDPOINT")
    private String endpoint;

    /**
     * 应用实例名
     */
    @TableField("INSTANCE_NAME")
    private String instanceName;

    /**
     * 应用实例描述
     */
    @TableField("INSTANCE_DESC")
    private String instanceDesc;

    /**
     * 应用实例摘要
     */
    @TableField("INSTANCE_SUMMARY")
    private String instanceSummary;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

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
     * 应用状态（0：离线，1：在线）
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
     * 是否收到离线通知（0：否，1：是）
     */
    @TableField("IS_OFFLINE_NOTICE")
    private String isOfflineNotice;

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
     * 编程语言
     */
    @TableField("LANGUAGE")
    private String language;

    /**
     * 应用服务器类型
     */
    @TableField("APP_SERVER_TYPE")
    private String appServerType;

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
