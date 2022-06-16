package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@ApiModel(value = "MonitorNet对象", description = "网络信息表")
public class MonitorNet implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址（来源）")
    @TableField("IP_SOURCE")
    private String ipSource;

    @ApiModelProperty(value = "IP地址（目的地）")
    @TableField("IP_TARGET")
    private String ipTarget;

    @ApiModelProperty(value = "IP地址描述")
    @TableField("IP_DESC")
    private String ipDesc;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "状态（0：网络不通，1：网络正常）")
    @TableField("STATUS")
    private String status;

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    @TableField("AVG_TIME")
    private Double avgTime;

    @ApiModelProperty(value = "离线次数")
    @TableField("OFFLINE_COUNT")
    private Integer offlineCount;

    @ApiModelProperty(value = "监控环境")
    @TableField(value = "MONITOR_ENV", updateStrategy = FieldStrategy.IGNORED)
    private String monitorEnv;

    @ApiModelProperty(value = "监控分组")
    @TableField(value = "MONITOR_GROUP", updateStrategy = FieldStrategy.IGNORED)
    private String monitorGroup;

}
