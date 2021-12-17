package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmRuntime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * java虚拟机运行时信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/15 19:45
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "java虚拟机运行时信息表现层对象")
public class MonitorJvmRuntimeVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    private String instanceId;

    @ApiModelProperty(value = "正在运行的Java虚拟机名称")
    private String name;

    @ApiModelProperty(value = "Java虚拟机实现名称")
    private String vmName;

    @ApiModelProperty(value = "Java虚拟机实现供应商")
    private String vmVendor;

    @ApiModelProperty(value = "Java虚拟机实现版本")
    private String vmVersion;

    @ApiModelProperty(value = "Java虚拟机规范名称")
    private String specName;

    @ApiModelProperty(value = "Java虚拟机规范供应商")
    private String specVendor;

    @ApiModelProperty(value = "Java虚拟机规范版本")
    private String specVersion;

    @ApiModelProperty(value = "管理接口规范版本")
    private String managementSpecVersion;

    @ApiModelProperty(value = "Java类路径")
    private String classPath;

    @ApiModelProperty(value = "Java库路径")
    private String libraryPath;

    @ApiModelProperty(value = "Java虚拟机是否支持引导类路径")
    private String isBootClassPathSupported;

    @ApiModelProperty(value = "引导类路径")
    private String bootClassPath;

    @ApiModelProperty(value = "Java虚拟机入参")
    private String inputArguments;

    @ApiModelProperty(value = "Java虚拟机的正常运行时间（毫秒）")
    private String uptime;

    @ApiModelProperty(value = "Java虚拟机的开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorJvmRuntimeVo转MonitorJvmRuntime
     * </p>
     *
     * @return {@link MonitorJvmRuntime}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorJvmRuntime convertTo() {
        MonitorJvmRuntime monitorJvmRuntime = MonitorJvmRuntime.builder().build();
        BeanUtils.copyProperties(this, monitorJvmRuntime);
        return monitorJvmRuntime;
    }

    /**
     * <p>
     * MonitorJvmRuntime转MonitorJvmRuntimeVo
     * </p>
     *
     * @param monitorJvmRuntime {@link MonitorJvmRuntime}
     * @return {@link MonitorJvmRuntimeVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorJvmRuntimeVo convertFor(MonitorJvmRuntime monitorJvmRuntime) {
        BeanUtils.copyProperties(monitorJvmRuntime, this);
        return this;
    }

}
