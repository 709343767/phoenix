package com.imby.server.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.imby.common.inf.ISuperBean;
import com.imby.server.business.web.entity.MonitorJvmMemory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * java虚拟机内存信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/14 11:48
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "java虚拟机内存信息表现层对象")
public class MonitorJvmMemoryVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    private String instanceId;

    @ApiModelProperty(value = "内存类型")
    private String memoryType;

    @ApiModelProperty(value = "初始内存量（单位：byte）")
    private Long init;

    @ApiModelProperty(value = "已用内存量（单位：byte）")
    private Long used;

    @ApiModelProperty(value = "提交内存量（单位：byte）")
    private Long committed;

    @ApiModelProperty(value = "最大内存量（单位：byte，可能存在未定义）")
    private String max;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorJvmMemoryVo转MonitorJvmMemory
     * </p>
     *
     * @return {@link MonitorJvmMemory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorJvmMemory convertTo() {
        MonitorJvmMemory monitorJvmMemory = MonitorJvmMemory.builder().build();
        BeanUtils.copyProperties(this, monitorJvmMemory);
        return monitorJvmMemory;
    }

    /**
     * <p>
     * MonitorJvmMemory转MonitorJvmMemoryVo
     * </p>
     *
     * @param monitorJvmMemory {@link MonitorJvmMemory}
     * @return {@link MonitorJvmMemoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorJvmMemoryVo convertFor(MonitorJvmMemory monitorJvmMemory) {
        BeanUtils.copyProperties(monitorJvmMemory, this);
        return this;
    }

}
