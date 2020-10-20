package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器CPU信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/19 14:21
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器详情页面服务器CPU信息表现层对象")
public class ServerDetailPageServerCpuVo implements ISuperBean {

    @ApiModelProperty(value = "CPU序号")
    private Integer cpuNo;

    @ApiModelProperty(value = "CPU频率（MHz）")
    private Integer cpuMhz;

    @ApiModelProperty(value = "CPU使用率")
    private Double cpuCombined;

    @ApiModelProperty(value = "CPU剩余率")
    private Double cpuIdle;

    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

}
