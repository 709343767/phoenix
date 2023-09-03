package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "home页的TCP信息表现层对象")
public class HomeTcpVo implements ISuperBean {

    @Schema(description = "TCP数量")
    private Integer tcpSum;

    @Schema(description = "TCP正常数量")
    private Integer tcpConnectSum;

    @Schema(description = "TCP断开数量")
    private Integer tcpDisconnectSum;

    @Schema(description = "TCP未知数量")
    private Integer tcpUnsentSum;

    @Schema(description = "TCP正常率")
    private String tcpConnectRate;

}