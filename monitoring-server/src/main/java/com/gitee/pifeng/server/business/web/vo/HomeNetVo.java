package com.gitee.pifeng.server.business.web.vo;

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
public class HomeNetVo {

    @ApiModelProperty(value = "IP数量")
    private Integer netSum;

    @ApiModelProperty(value = "IP正常数量")
    private Integer netConnectSum;

    @ApiModelProperty(value = "IP断开数量")
    private Integer netDisconnectSum;

    @ApiModelProperty(value = "IP正常率")
    private String netConnectRate;

}
