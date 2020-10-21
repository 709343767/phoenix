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
 * 服务器详情页面服务器内存信息表现层对象
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
@ApiModel(value = "服务器详情页面服务器内存信息表现层对象")
public class ServerDetailPageServerMemoryVo implements ISuperBean {

    @ApiModelProperty(value = "物理内存总量（单位：Gb）")
    private Double memTotal;

    @ApiModelProperty(value = "物理内存使用量（单位：Gb）")
    private Double memUsed;

    @ApiModelProperty(value = "物理内存剩余量（单位：Gb）")
    private Double memFree;

    @ApiModelProperty(value = "物理内存使用率")
    private Double menUsedPercent;

    @JsonFormat(pattern = "yy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

}
