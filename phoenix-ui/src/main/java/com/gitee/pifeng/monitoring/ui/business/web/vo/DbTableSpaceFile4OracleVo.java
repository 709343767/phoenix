package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * Oracle数据库表空间表现层对象(按文件)
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:20
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Oracle数据库表空间表现层对象(按文件)")
public class DbTableSpaceFile4OracleVo implements ISuperBean {

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "表空间")
    private String tablespaceName;

    @Schema(description = "表空间大小")
    private String total;

    @Schema(description = "表空间使用大小")
    private String used;

    @Schema(description = "表空间剩余大小")
    private String free;

    @Schema(description = "使用百分比")
    private Double usedPer;

    @Schema(description = "剩余百分比")
    private Double freePer;

}
