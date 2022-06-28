package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 服务器平均负载历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_LOAD_AVERAGE_HISTORY")
@ApiModel(value = "MonitorServerLoadAverageHistory对象", description = "服务器平均负载历史记录表")
public class MonitorServerLoadAverageHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "1分钟负载平均值")
    @TableField("ONE")
    private Double one;

    @ApiModelProperty(value = "5分钟负载平均值")
    @TableField("FIVE")
    private Double five;

    @ApiModelProperty(value = "15分钟负载平均值")
    @TableField("FIFTEEN")
    private Double fifteen;

    @ApiModelProperty(value = "CPU逻辑核数量")
    @TableField("LOGICAL_PROCESSOR_COUNT")
    private Integer logicalProcessorCount;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
