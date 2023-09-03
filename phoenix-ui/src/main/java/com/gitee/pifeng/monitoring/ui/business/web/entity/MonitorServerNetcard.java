package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "MonitorServerNetcard对象")
public class MonitorServerNetcard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "网卡序号")
    @TableField("NET_NO")
    private Integer netNo;

    @Schema(description = "网卡名字")
    @TableField("NAME")
    private String name;

    @Schema(description = "网卡类型")
    @TableField("TYPE")
    private String type;

    @Schema(description = "网卡地址")
    @TableField("ADDRESS")
    private String address;

    @Schema(description = "子网掩码")
    @TableField("MASK")
    private String mask;

    @Schema(description = "广播地址")
    @TableField("BROADCAST")
    private String broadcast;

    @Schema(description = "MAC地址")
    @TableField("HW_ADDR")
    private String hwAddr;

    @Schema(description = "网卡信息描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(description = "接收到的总字节数")
    @TableField("RX_BYTES")
    private Long rxBytes;

    @Schema(description = "接收的总包数")
    @TableField("RX_PACKETS")
    private Long rxPackets;

    @Schema(description = "接收到的错误包数")
    @TableField("RX_ERRORS")
    private Long rxErrors;

    @Schema(description = "接收时丢弃的包数")
    @TableField("RX_DROPPED")
    private Long rxDropped;

    @Schema(description = "发送的总字节数")
    @TableField("TX_BYTES")
    private Long txBytes;

    @Schema(description = "发送的总包数")
    @TableField("TX_PACKETS")
    private Long txPackets;

    @Schema(description = "发送时的错误包数")
    @TableField("TX_ERRORS")
    private Long txErrors;

    @Schema(description = "发送时丢弃的包数")
    @TableField("TX_DROPPED")
    private Long txDropped;

    @Schema(description = "下载速度")
    @TableField("DOWNLOAD_BPS")
    private Double downloadBps;

    @Schema(description = "上传速度")
    @TableField("UPLOAD_BPS")
    private Double uploadBps;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
