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
 * 网络信息历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_NET_HISTORY")
@ApiModel(value = "MonitorNetHistory对象", description = "网络信息表")
public class MonitorNetHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "网络主表ID")
    @TableField("NET_ID")
    private Long netId;

    @ApiModelProperty(value = "IP地址（来源）")
    @TableField("IP_SOURCE")
    private String ipSource;

    @ApiModelProperty(value = "IP地址（目的地）")
    @TableField("IP_TARGET")
    private String ipTarget;

    @ApiModelProperty(value = "IP地址描述")
    @TableField("IP_DESC")
    private String ipDesc;

    @ApiModelProperty(value = "状态（0：网络不通，1：网络正常）")
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
