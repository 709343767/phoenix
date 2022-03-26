package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的TCP信息表现层对象
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
@ApiModel(value = "home页的TCP信息表现层对象")
public class HomeTcpVo implements ISuperBean {

    @ApiModelProperty(value = "TCP数量")
    private Integer tcpSum;

    @ApiModelProperty(value = "TCP正常数量")
    private Integer tcpConnectSum;

    @ApiModelProperty(value = "TCP断开数量")
    private Integer tcpDisconnectSum;

    @ApiModelProperty(value = "TCP未知数量")
    private Integer tcpUnsentSum;

    @ApiModelProperty(value = "TCP正常率")
    private String tcpConnectRate;

}