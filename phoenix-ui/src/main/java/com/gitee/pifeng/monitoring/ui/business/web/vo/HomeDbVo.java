package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * home页的数据库信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/19 21:40
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "home页的数据库信息表现层对象")
public class HomeDbVo implements ISuperBean {

    @Schema(description = "数据库数量")
    private Integer dbSum;

    @Schema(description = "数据库正常数量")
    private Integer dbConnectSum;

    @Schema(description = "数据库断开数量")
    private Integer dbDisconnectSum;

    @Schema(description = "数据库未知数量")
    private Integer dbUnsentSum;

    @Schema(description = "数据库正常率")
    private String dbConnectRate;

}
