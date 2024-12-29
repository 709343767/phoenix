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
 * 网络信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:10
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_NET")
@Schema(description = "MonitorNet对象")
public class MonitorNet implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址（来源）")
    @TableField("IP_SOURCE")
    private String ipSource;

    @Schema(description = "IP地址（目的地）")
    @TableField("IP_TARGET")
    private String ipTarget;

    @Schema(description = "IP地址描述")
    @TableField("IP_DESC")
    private String ipDesc;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(description = "状态（0：网络不通，1：网络正常）")
    @TableField("STATUS")
    private String status;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    @TableField("IS_ENABLE_MONITOR")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    @TableField("IS_ENABLE_ALARM")
    private String isEnableAlarm;

    @Schema(description = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Double avgTime;

    @Schema(description = "ping详情")
    @TableField("PING_DETAIL")
    private String pingDetail;

    @Schema(description = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @Schema(description = "监控环境")
    @TableField(value = "MONITOR_ENV", updateStrategy = FieldStrategy.IGNORED)
    private String monitorEnv;

    @Schema(description = "监控分组")
    @TableField(value = "MONITOR_GROUP", updateStrategy = FieldStrategy.IGNORED)
    private String monitorGroup;

}
