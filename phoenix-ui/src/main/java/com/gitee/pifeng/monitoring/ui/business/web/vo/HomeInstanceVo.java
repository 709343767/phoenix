package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "home页的应用实例表现层对象")
public class HomeInstanceVo implements ISuperBean {

    @Schema(description = "应用实例数量")
    private Integer instanceSum;

    @Schema(description = "应用实例在线数量")
    private Integer instanceOnLineSum;

    @Schema(description = "应用实例离线数量")
    private Integer instanceOffLineSum;

    @Schema(description = "应用实例未知状态数量")
    private Integer instanceUnknownLineSum;

    @Schema(description = "应用实例在线率")
    private String instanceOnLineRate;

}
