package com.gitee.pifeng.monitoring.ui.business.web.entity;

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
 * TCP/IP信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_TCPIP")
@ApiModel(value = "MonitorTcpIp对象", description = "TCP/IP信息表")
public class MonitorTcpIp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId("ID")
    private Long id;

    @ApiModelProperty(value = "IP地址（来源）")
    @TableField("IP_SOURCE")
    private String ipSource;

    @ApiModelProperty(value = "IP地址（目的地）")
    @TableField("IP_TARGET")
    private String ipTarget;

    @ApiModelProperty(value = "端口号")
    @TableField("PORT_TARGET")
    private Integer portTarget;

    @ApiModelProperty(value = "描述")
    @TableField("DESCR")
    private String descr;

    @ApiModelProperty(value = "协议")
    @TableField("PROTOCOL")
    private String protocol;

    @ApiModelProperty(value = "状态（0：不通，1：正常）")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
