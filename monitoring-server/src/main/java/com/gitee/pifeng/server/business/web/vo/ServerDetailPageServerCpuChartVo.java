package com.gitee.pifeng.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器详情页面服务器CPU图表信息表现层对象
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
@ApiModel(value = "服务器详情页面服务器CPU图表信息表现层对象")
public class ServerDetailPageServerCpuChartVo implements ISuperBean {

    @ApiModelProperty(value = "CPU用户使用率")
    private Double cpuUser;

    @ApiModelProperty(value = "CPU系统使用率")
    private Double cpuSys;

    @ApiModelProperty(value = "CPU等待率")
    private Double cpuWait;

    @ApiModelProperty(value = "CPU错误率")
    private Double cpuNice;

    @ApiModelProperty(value = "CPU使用率")
    private Double cpuCombined;

    @ApiModelProperty(value = "CPU剩余率")
    private Double cpuIdle;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

}
