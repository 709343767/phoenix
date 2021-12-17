package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的网络信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:13
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "home页的网络信息表现层对象")
public class HomeNetVo implements ISuperBean {

    @ApiModelProperty(value = "网络数量")
    private Integer netSum;

    @ApiModelProperty(value = "网络正常数量")
    private Integer netConnectSum;

    @ApiModelProperty(value = "网络断开数量")
    private Integer netDisconnectSum;

    @ApiModelProperty(value = "网络未知数量")
    private Integer netUnsentSum;

    @ApiModelProperty(value = "网络正常率")
    private String netConnectRate;

}
