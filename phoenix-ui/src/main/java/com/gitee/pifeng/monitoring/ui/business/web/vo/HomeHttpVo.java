package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的HTTP信息表现层对象
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
@ApiModel(value = "home页的HTTP信息表现层对象")
public class HomeHttpVo implements ISuperBean {

    @ApiModelProperty(value = "HTTP数量")
    private Integer httpSum;

    @ApiModelProperty(value = "HTTP正常数量")
    private Integer httpConnectSum;

    @ApiModelProperty(value = "HTTP异常数量")
    private Integer httpDisconnectSum;

    @ApiModelProperty(value = "HTTP未知数量")
    private Integer httpUnsentSum;

    @ApiModelProperty(value = "HTTP正常率")
    private String httpConnectRate;

}