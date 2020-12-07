package com.gitee.pifeng.server.business.web.entity;

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
 * 服务器网卡表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@Builder
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

    @ApiModelProperty(value = "接收到的总字节数")
    @TableField("RX_BYTES")
    private Long rxBytes;

    @ApiModelProperty(value = "接收的总包数")
    @TableField("RX_PACKETS")
    private Long rxPackets;

    @ApiModelProperty(value = "接收到的错误包数")
    @TableField("RX_ERRORS")
    private Long rxErrors;

    @ApiModelProperty(value = "接收时丢弃的包数")
    @TableField("RX_DROPPED")
    private Long rxDropped;

    @ApiModelProperty(value = "发送的总字节数")
    @TableField("TX_BYTES")
    private Long txBytes;

    @ApiModelProperty(value = "发送的总包数")
    @TableField("TX_PACKETS")
    private Long txPackets;

    @ApiModelProperty(value = "发送时的错误包数")
    @TableField("TX_ERRORS")
    private Long txErrors;

    @ApiModelProperty(value = "发送时丢弃的包数")
    @TableField("TX_DROPPED")
    private Long txDropped;

}
