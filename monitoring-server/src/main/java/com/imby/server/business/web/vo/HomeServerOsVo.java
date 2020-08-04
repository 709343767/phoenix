package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
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
public class HomeServerOsVo implements ISuperBean {

    @ApiModelProperty(value = "服务器数量")
    private Integer serverSum;
}
