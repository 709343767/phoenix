package com.imby.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "CPU使用率")
    private Double cpuCombined;

    @JsonFormat(pattern = "yy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

}
