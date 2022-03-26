package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * TCP信息历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_TCP_HISTORY")
@ApiModel(value = "MonitorTcpHistory对象", description = "TCP信息历史记录表")
public class MonitorTcpHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId("ID")
    private Long id;

    @ApiModelProperty(value = "TCP主表ID")
    @TableField("TCP_ID")
    private Long tcpId;

    @ApiModelProperty(value = "主机名（来源）")
    @TableField("HOSTNAME_SOURCE")
    private String hostnameSource;

    @ApiModelProperty(value = "主机名（目的地）")
    @TableField("HOSTNAME_TARGET")
    private String hostnameTarget;

    @ApiModelProperty(value = "端口号")
    @TableField("PORT_TARGET")
    private Integer portTarget;

    @ApiModelProperty(value = "描述")
    @TableField("DESCR")
    private String descr;

    @ApiModelProperty(value = "状态（0：不通，1：正常）")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Long avgTime;

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
