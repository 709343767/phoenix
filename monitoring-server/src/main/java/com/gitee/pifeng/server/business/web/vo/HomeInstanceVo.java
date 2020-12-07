package com.gitee.pifeng.server.business.web.vo;

import com.gitee.pifeng.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的应用实例表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/4 15:54
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "home页的应用实例表现层对象")
public class HomeInstanceVo implements ISuperBean {

    @ApiModelProperty(value = "应用实例数量")
    private Integer instanceSum;

    @ApiModelProperty(value = "应用实例在线数量")
    private Integer instanceOnLineSum;

    @ApiModelProperty(value = "应用实例离线数量")
    private Integer instanceOffLineSum;

    @ApiModelProperty(value = "应用实例在线率")
    private String instanceOnLineRate;

}
