package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "服务器详情页面服务器平均负载图表信息表现层对象")
public class ServerDetailPageServerLoadAverageChartVo implements ISuperBean {

    @ApiModelProperty(value = "1分钟负载平均值")
    private Double one;

    @ApiModelProperty(value = "5分钟负载平均值")
    private Double five;

    @ApiModelProperty(value = "15分钟负载平均值")
    private Double fifteen;

    @ApiModelProperty(value = "CPU逻辑核数量")
    private Integer logicalProcessorCount;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

}
