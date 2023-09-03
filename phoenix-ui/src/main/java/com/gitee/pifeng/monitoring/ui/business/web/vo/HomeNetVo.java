package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "home页的网络信息表现层对象")
public class HomeNetVo implements ISuperBean {

    @Schema(description = "网络数量")
    private Integer netSum;

    @Schema(description = "网络正常数量")
    private Integer netConnectSum;

    @Schema(description = "网络断开数量")
    private Integer netDisconnectSum;

    @Schema(description = "网络未知数量")
    private Integer netUnsentSum;

    @Schema(description = "网络正常率")
    private String netConnectRate;

}
