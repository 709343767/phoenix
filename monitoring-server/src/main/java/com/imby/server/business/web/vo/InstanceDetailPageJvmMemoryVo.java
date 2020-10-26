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
 * 应用实例详情页面java虚拟机内存信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/16 13:28
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "应用实例详情页面java虚拟机内存信息表现层对象")
public class InstanceDetailPageJvmMemoryVo implements ISuperBean {

    @ApiModelProperty(value = "初始内存量（单位：Mb）")
    private String init;

    @ApiModelProperty(value = "已用内存量（单位：Mb）")
    private String used;

    @ApiModelProperty(value = "提交内存量（单位：Mb）")
    private String committed;

    @ApiModelProperty(value = "最大内存量（单位：Mb，可能存在未定义）")
    private String max;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

}
