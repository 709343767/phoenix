package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "MonitorInstance对象", description = "应用实例表")
public class MonitorInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "端点（客户端<client>、代理端<agent>、服务端<server>、UI端<ui>）")
    @TableField("ENDPOINT")
    private String endpoint;

    @ApiModelProperty(value = "应用实例名")
    @TableField("INSTANCE_NAME")
    private String instanceName;

    @ApiModelProperty(value = "应用实例描述")
    @TableField("INSTANCE_DESC")
    private String instanceDesc;

    @ApiModelProperty(value = "应用实例摘要")
    @TableField("INSTANCE_SUMMARY")
    private String instanceSummary;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "应用状态（0：离线，1：在线）")
    @TableField("IS_ONLINE")
    private String isOnline;

    @ApiModelProperty(value = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @ApiModelProperty(value = "连接频率")
    @TableField("CONN_FREQUENCY")
    private Integer connFrequency;

    @ApiModelProperty(value = "程序语言")
    @TableField("LANGUAGE")
    private String language;

    @ApiModelProperty(value = "应用服务器类型")
    @TableField("APP_SERVER_TYPE")
    private String appServerType;

    @ApiModelProperty(value = "监控环境")
    @TableField("MONITOR_ENV")
    private String monitorEnv;

    @ApiModelProperty(value = "监控分组")
    @TableField("MONITOR_GROUP")
    private String monitorGroup;

}
