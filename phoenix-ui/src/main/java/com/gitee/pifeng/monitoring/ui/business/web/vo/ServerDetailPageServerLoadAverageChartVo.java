package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器平均负载图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/19 14:51
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器详情页面服务器平均负载图表信息表现层对象")
public class ServerDetailPageServerLoadAverageChartVo implements ISuperBean {

    @Schema(description = "1分钟负载平均值")
    private Double one;

    @Schema(description = "5分钟负载平均值")
    private Double five;

    @Schema(description = "15分钟负载平均值")
    private Double fifteen;

    @Schema(description = "CPU逻辑核数量")
    private Integer logicalProcessorCount;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

}
