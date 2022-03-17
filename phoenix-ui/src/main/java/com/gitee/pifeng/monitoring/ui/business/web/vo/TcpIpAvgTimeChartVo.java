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
 * TCPIP连接耗时图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/17 21:42
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TCPIP连接耗时图表信息表现层对象")
public class TcpIpAvgTimeChartVo implements ISuperBean {

    @ApiModelProperty(value = "平均响应时间（毫秒）")
    private Long avgTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

}
