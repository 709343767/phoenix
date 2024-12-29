package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorJvmRuntime;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "java虚拟机运行时信息表现层对象")
public class MonitorJvmRuntimeVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "应用实例ID")
    private String instanceId;

    @Schema(description = "正在运行的Java虚拟机名称")
    private String name;

    @Schema(description = "Java虚拟机实现名称")
    private String vmName;

    @Schema(description = "Java虚拟机实现供应商")
    private String vmVendor;

    @Schema(description = "Java虚拟机实现版本")
    private String vmVersion;

    @Schema(description = "Java虚拟机规范名称")
    private String specName;

    @Schema(description = "Java虚拟机规范供应商")
    private String specVendor;

    @Schema(description = "Java虚拟机规范版本")
    private String specVersion;

    @Schema(description = "管理接口规范版本")
    private String managementSpecVersion;

    @Schema(description = "Java类路径")
    private String classPath;

    @Schema(description = "Java库路径")
    private String libraryPath;

    @Schema(description = "Java虚拟机是否支持引导类路径")
    private String isBootClassPathSupported;

    @Schema(description = "引导类路径")
    private String bootClassPath;

    @Schema(description = "Java虚拟机入参")
    private String inputArguments;

    @Schema(description = "Java虚拟机的正常运行时间（毫秒）")
    private String uptime;

    @Schema(description = "Java虚拟机的开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @Schema(description = "新增时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Schema(description = "更新时间")
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
        if (null != monitorJvmRuntime) {
            BeanUtils.copyProperties(monitorJvmRuntime, this);
        }
        return this;
    }

}
