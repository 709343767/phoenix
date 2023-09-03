package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 应用实例表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_INSTANCE")
@Schema(description = "MonitorInstance对象")
public class MonitorInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "端点（客户端<client>、代理端<agent>、服务端<server>、UI端<ui>）")
    @TableField("ENDPOINT")
    private String endpoint;

    @Schema(description = "应用实例名")
    @TableField("INSTANCE_NAME")
    private String instanceName;

    @Schema(description = "应用实例描述")
    @TableField("INSTANCE_DESC")
    private String instanceDesc;

    @Schema(description = "应用实例摘要")
    @TableField("INSTANCE_SUMMARY")
    private String instanceSummary;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @Schema(description = "应用状态（0：离线，1：在线）")
    @TableField("IS_ONLINE")
    private String isOnline;

    @Schema(description = "是否收到离线通知（0：否，1：是）")
    @TableField("IS_OFFLINE_NOTICE")
    private String isOfflineNotice;

    @Schema(description = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @Schema(description = "连接频率")
    @TableField("CONN_FREQUENCY")
    private Integer connFrequency;

    @Schema(description = "程序语言")
    @TableField("LANGUAGE")
    private String language;

    @Schema(description = "应用服务器类型")
    @TableField("APP_SERVER_TYPE")
    private String appServerType;

    @Schema(description = "监控环境")
    @TableField(value = "MONITOR_ENV", updateStrategy = FieldStrategy.IGNORED)
    private String monitorEnv;

    @Schema(description = "监控分组")
    @TableField(value = "MONITOR_GROUP", updateStrategy = FieldStrategy.IGNORED)
    private String monitorGroup;

}
