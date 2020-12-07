package com.gitee.pifeng.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "服务器详情页面服务器网卡信息表现层对象")
public class ServerDetailPageServerNetcardVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "网卡序号")
    private Integer netNo;

    @ApiModelProperty(value = "网卡名字")
    private String name;

    @ApiModelProperty(value = "网卡类型")
    private String type;

    @ApiModelProperty(value = "网卡地址")
    private String address;

    @ApiModelProperty(value = "子网掩码")
    private String mask;

    @ApiModelProperty(value = "广播地址")
    private String broadcast;

    @ApiModelProperty(value = "MAC地址")
    private String hwAddr;

    @ApiModelProperty(value = "网卡信息描述")
    private String description;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "接收的总数据大小")
    private String rx;

    @ApiModelProperty(value = "接收的总包数")
    private Long rxPackets;

    @ApiModelProperty(value = "接收到的错误包数")
    private Long rxErrors;

    @ApiModelProperty(value = "接收时丢弃的包数")
    private Long rxDropped;

    @ApiModelProperty(value = "发送的总数据大小")
    private String tx;

    @ApiModelProperty(value = "发送的总包数")
    private Long txPackets;

    @ApiModelProperty(value = "发送时的错误包数")
    private Long txErrors;

    @ApiModelProperty(value = "发送时丢弃的包数")
    private Long txDropped;

}
