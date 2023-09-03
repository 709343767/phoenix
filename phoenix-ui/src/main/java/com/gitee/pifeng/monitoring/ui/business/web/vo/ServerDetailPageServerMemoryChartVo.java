package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器内存图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/21 12:37
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器内存图表信息表现层对象")
public class ServerDetailPageServerMemoryChartVo implements ISuperBean {

    @Schema(description = "物理内存总量（单位：Gb）")
    private Double memTotal;

    @Schema(description = "物理内存使用量（单位：Gb）")
    private Double memUsed;

    @Schema(description = "物理内存剩余量（单位：Gb）")
    private Double memFree;

    @Schema(description = "物理内存使用率")
    private Double menUsedPercent;

    @Schema(description = "交换区总量（单位：Gb）")
    private Double swapTotal;

    @Schema(description = "交换区使用量（单位：Gb）")
    private Double swapUsed;

    @Schema(description = "交换区剩余量（单位：Gb）")
    private Double swapFree;

    @Schema(description = "交换区使用率")
    private Double swapUsedPercent;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

}
