package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器网卡信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/11 22:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_NETCARD")
public class MonitorServerNetcard {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 网卡序号
     */
    @TableField("NET_NO")
    private Integer netNo;

    /**
     * 网卡名字
     */
    @TableField("NAME")
    private String name;

    /**
     * 网卡类型
     */
    @TableField("TYPE")
    private String type;

    /**
     * 网卡地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 子网掩码
     */
    @TableField("MASK")
    private String mask;

    /**
     * 广播地址
     */
    @TableField("BROADCAST")
    private String broadcast;

    /**
     * MAC地址
     */
    @TableField("HW_ADDR")
    private String hwAddr;

    /**
     * 网卡描述信息
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 新增时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 接收到的总字节数
     */
    @TableField("RX_BYTES")
    private Long rxBytes;

    /**
     * 接收的总包数
     */
    @TableField("RX_PACKETS")
    private Long rxPackets;

    /**
     * 接收到的错误包数
     */
    @TableField("RX_ERRORS")
    private Long rxErrors;

    /**
     * 接收时丢弃的包数
     */
    @TableField("RX_DROPPED")
    private Long rxDropped;

    /**
     * 发送的总字节数
     */
    @TableField("TX_BYTES")
    private Long txBytes;

    /**
     * 发送的总包数
     */
    @TableField("TX_PACKETS")
    private Long txPackets;

    /**
     * 发送时的错误包数
     */
    @TableField("TX_ERRORS")
    private Long txErrors;

    /**
     * 发送时丢弃的包数
     */
    @TableField("TX_DROPPED")
    private Long txDropped;

}
