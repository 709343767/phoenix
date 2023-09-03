package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "home页的服务器表现层对象")
public class HomeServerVo implements ISuperBean {

    @Schema(description = "服务器数量")
    private Integer serverSum;

    @Schema(description = "linux服务器数量")
    private Integer linuxSum;

    @Schema(description = "windows服务器数量")
    private Integer windowsSum;

    @Schema(description = "其它服务器数量")
    private Integer otherSum;

    @Schema(description = "服务器在线数量")
    private Integer serverOnLineSum;

    @Schema(description = "服务器离线数量")
    private Integer serverOffLineSum;

    @Schema(description = "服务器在线率")
    private String serverOnLineRate;

}
