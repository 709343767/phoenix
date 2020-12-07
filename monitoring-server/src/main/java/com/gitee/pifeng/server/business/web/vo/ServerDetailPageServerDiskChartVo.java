package com.gitee.pifeng.server.business.web.vo;

import com.gitee.pifeng.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务器详情页面服务器磁盘图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/22 18:30
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器详情页面服务器磁盘图表信息表现层对象")
public class ServerDetailPageServerDiskChartVo implements ISuperBean {

    @ApiModelProperty(value = "分区的盘符名称")
    private String devName;

    @ApiModelProperty(value = "磁盘总大小")
    private String totalStr;

    @ApiModelProperty(value = "磁盘剩余大小")
    private String freeStr;

    @ApiModelProperty(value = "磁盘已用大小")
    private String usedStr;

    @ApiModelProperty(value = "磁盘可用大小")
    private String availStr;

    @ApiModelProperty(value = "磁盘资源的利用率")
    private Double usePercent;

}
