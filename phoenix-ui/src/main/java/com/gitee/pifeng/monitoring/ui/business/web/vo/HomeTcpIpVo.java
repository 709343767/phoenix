package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的TCP/IP信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/27 10:35
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "home页的TCP/IP信息表现层对象")
public class HomeTcpIpVo implements ISuperBean {

    @ApiModelProperty(value = "TCP/IP数量")
    private Integer tcpIpSum;

    @ApiModelProperty(value = "TCP/IP正常数量")
    private Integer tcpIpConnectSum;

    @ApiModelProperty(value = "TCP/IP断开数量")
    private Integer tcpIpDisconnectSum;

    @ApiModelProperty(value = "TCP/IP未知数量")
    private Integer tcpIpUnsentSum;

    @ApiModelProperty(value = "TCP/IP正常率")
    private String tcpIpConnectRate;

}