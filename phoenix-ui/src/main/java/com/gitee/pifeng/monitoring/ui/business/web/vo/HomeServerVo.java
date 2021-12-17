package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的服务器表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/4 16:37
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "home页的服务器表现层对象")
public class HomeServerVo implements ISuperBean {

    @ApiModelProperty(value = "服务器数量")
    private Integer serverSum;

    @ApiModelProperty(value = "linux服务器数量")
    private Integer linuxSum;

    @ApiModelProperty(value = "windows服务器数量")
    private Integer windowsSum;

    @ApiModelProperty(value = "其它服务器数量")
    private Integer otherSum;

    @ApiModelProperty(value = "服务器在线数量")
    private Integer serverOnLineSum;

    @ApiModelProperty(value = "服务器离线数量")
    private Integer serverOffLineSum;

    @ApiModelProperty(value = "服务器在线率")
    private String serverOnLineRate;

}
