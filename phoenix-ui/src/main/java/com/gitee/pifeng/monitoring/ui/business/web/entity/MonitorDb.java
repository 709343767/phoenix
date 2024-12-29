package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 数据库表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_DB")
@Schema(description = "MonitorDb对象")
public class MonitorDb implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "数据库连接名")
    @TableField("CONN_NAME")
    private String connName;

    @Schema(description = "数据库URL")
    @TableField("URL")
    private String url;

    @Schema(description = "用户名")
    @TableField("USERNAME")
    private String username;

    @Schema(description = "密码")
    @TableField("PASSWORD")
    private String password;

    @Schema(description = "数据库类型")
    @TableField("DB_TYPE")
    private String dbType;

    @Schema(description = "驱动类")
    @TableField("DRIVER_CLASS")
    private String driverClass;

    @Schema(description = "描述")
    @TableField("DB_DESC")
    private String dbDesc;

    @Schema(description = "数据库状态（0：离线，1：在线）")
    @TableField("IS_ONLINE")
    private String isOnline;

    @Schema(description = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    @TableField("IS_ENABLE_MONITOR")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    @TableField("IS_ENABLE_ALARM")
    private String isEnableAlarm;

    @Schema(description = "插入时间")
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
