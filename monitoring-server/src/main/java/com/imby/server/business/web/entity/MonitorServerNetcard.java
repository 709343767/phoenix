package com.imby.server.business.web.entity;

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
 * 服务器网卡表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_NETCARD")
@ApiModel(value = "MonitorServerNetcard对象", description = "服务器网卡表")
public class MonitorServerNetcard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "网卡序号")
    @TableField("NET_NO")
    private Integer netNo;

    @ApiModelProperty(value = "网卡名字")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "网卡类型")
    @TableField("TYPE")
    private String type;

    @ApiModelProperty(value = "网卡地址")
    @TableField("ADDRESS")
    private String address;

    @ApiModelProperty(value = "子网掩码")
    @TableField("MASK")
    private String mask;

    @ApiModelProperty(value = "广播地址")
    @TableField("BROADCAST")
    private String broadcast;

    @ApiModelProperty(value = "MAC地址")
    @TableField("HW_ADDR")
    private String hwAddr;

    @ApiModelProperty(value = "网卡信息描述")
    @TableField("DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
