package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * HTTP信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_HTTP")
public class MonitorHttp {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 主机名（来源）
     */
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    /**
     * URL地址（目的地）
     */
    @TableField("URL_TARGET")
    private String urlTarget;

    /**
     * 请求方法
     */
    @TableField("METHOD")
    private String method;

    /**
     * 媒体类型
     */
    @TableField("CONTENT_TYPE")
    private String contentType;

    /**
     * 请求头参数
     */
    @TableField("HEADER_PARAMETER")
    private String headerParameter;

    /**
     * 请求体参数
     */
    @TableField("BODY_PARAMETER")
    private String bodyParameter;

    /**
     * 描述
     */
    @TableField("DESCR")
    private String descr;

    /**
     * 平均响应时间（毫秒）
     */
    @TableField("AVG_TIME")
    private Long avgTime;

    /**
     * 状态
     */
    @TableField("STATUS")
    private Integer status;

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
     * 异常信息
     */
    @TableField(value = "EXC_MESSAGE", updateStrategy = FieldStrategy.IGNORED)
    private String excMessage;

    /**
     * 结果内容
     */
    @TableField(value = "RESULT_BODY", updateStrategy = FieldStrategy.IGNORED)
    private String resultBody;

    /**
     * 结果内容大小（byte）
     */
    @TableField(value = "RESULT_BODY_SIZE", updateStrategy = FieldStrategy.IGNORED)
    private Integer resultBodySize;

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
