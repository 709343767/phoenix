package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * Oracle数据库表空间表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/5 14:34
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Oracle数据库表空间表现层对象")
public class DbTableSpaceAll4OracleVo implements ISuperBean {

    @Schema(description = "表空间")
    private String tablespaceName;

    @Schema(description = "总空间")
    private String total;

    @Schema(description = "使用空间")
    private String used;

    @Schema(description = "剩余空间")
    private String free;

    @Schema(description = "表空间使用率")
    private Double usedRate;

    @Schema(description = "表空间剩余率")
    private Double freeRate;

}
