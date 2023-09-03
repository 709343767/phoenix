package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "home页的HTTP信息表现层对象")
public class HomeHttpVo implements ISuperBean {

    @Schema(description = "HTTP数量")
    private Integer httpSum;

    @Schema(description = "HTTP正常数量")
    private Integer httpConnectSum;

    @Schema(description = "HTTP异常数量")
    private Integer httpDisconnectSum;

    @Schema(description = "HTTP未知数量")
    private Integer httpUnsentSum;

    @Schema(description = "HTTP正常率")
    private String httpConnectRate;

}