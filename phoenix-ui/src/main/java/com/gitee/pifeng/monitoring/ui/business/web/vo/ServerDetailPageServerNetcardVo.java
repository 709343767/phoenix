package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器网卡信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/30 19:33
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器网卡信息表现层对象")
public class ServerDetailPageServerNetcardVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "网卡序号")
    private Integer netNo;

    @Schema(description = "网卡名字")
    private String name;

    @Schema(description = "网卡类型")
    private String type;

    @Schema(description = "网卡地址")
    private String address;

    @Schema(description = "子网掩码")
    private String mask;

    @Schema(description = "广播地址")
    private String broadcast;

    @Schema(description = "MAC地址")
    private String hwAddr;

    @Schema(description = "网卡信息描述")
    private String description;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "接收的总数据大小")
    private String rx;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "接收的总包数")
    private Long rxPackets;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "接收到的错误包数")
    private Long rxErrors;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "接收时丢弃的包数")
    private Long rxDropped;

    @Schema(description = "发送的总数据大小")
    private String tx;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "发送的总包数")
    private Long txPackets;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "发送时的错误包数")
    private Long txErrors;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "发送时丢弃的包数")
    private Long txDropped;

    @Schema(description = "下载速度")
    private String downloadSpeed;

    @Schema(description = "上传速度")
    private String uploadSpeed;

}
