package com.imby.server.business.web.entity;

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

    @ApiModelProperty(value = "是否已经断网告警（0：否，1：是）")
    @TableField("IS_ALARM")
    private String isAlarm;

}
