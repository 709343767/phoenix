package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@Schema(description = "MonitorHttp对象")
public class MonitorHttp implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "主机名（来源）")
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    @Schema(description = "URL地址（目的地）")
    @TableField("URL_TARGET")
    private String urlTarget;

    @Schema(description = "请求方法")
    @TableField("METHOD")
    private String method;

    @Schema(description = "媒体类型")
    @TableField("CONTENT_TYPE")
    private String contentType;

    @Schema(description = "请求头参数")
    @TableField("HEADER_PARAMETER")
    private String headerParameter;

    @Schema(description = "请求体参数")
    @TableField("BODY_PARAMETER")
    private String bodyParameter;

    @Schema(description = "描述")
    @TableField("DESCR")
    private String descr;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Long avgTime;

    @Schema(description = "状态")
    @TableField("STATUS")
    private Integer status;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    @TableField("IS_ENABLE_MONITOR")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    @TableField("IS_ENABLE_ALARM")
    private String isEnableAlarm;

    @Schema(description = "异常信息")
    @TableField("EXC_MESSAGE")
    private String excMessage;

    @Schema(description = "结果内容")
    @TableField(value = "RESULT_BODY")
    private String resultBody;

    @Schema(description = "结果内容大小（byte）")
    @TableField(value = "RESULT_BODY_SIZE")
    private Integer resultBodySize;

    @Schema(description = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(description = "监控环境")
    @TableField(value = "MONITOR_ENV", updateStrategy = FieldStrategy.IGNORED)
    private String monitorEnv;

    @Schema(description = "监控分组")
    @TableField(value = "MONITOR_GROUP", updateStrategy = FieldStrategy.IGNORED)
    private String monitorGroup;

}
