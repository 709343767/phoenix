package com.imby.common.domain.jvm;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * JVM运行时信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 8:50
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class RuntimeDomain extends AbstractSuperBean {

    /**
     * 正在运行的Java虚拟机名称
     */
    private String name;

    /**
     * Java虚拟机实现名称
     */
    private String vmName;

    /**
     * Java虚拟机实现供应商
     */
    private String vmVendor;

    /**
     * Java虚拟机实现版本
     */
    private String vmVersion;

    /**
     * Java虚拟机规范名称
     */
    private String specName;

    /**
     * Java虚拟机规范供应商
     */
    private String specVendor;

    /**
     * Java虚拟机规范版本
     */
    private String specVersion;

    /**
     * 管理接口规范版本
     */
    private String managementSpecVersion;

    /**
     * Java类路径
     */
    private String classPath;

    /**
     * Java库路径
     */
    private String libraryPath;

    /**
     * Java虚拟机是否支持引导类路径
     */
    private boolean isBootClassPathSupported;

    /**
     * 引导类路径
     */
    private String bootClassPath;

    /**
     * Java虚拟机入参
     */
    private List<String> inputArguments;

    /**
     * Java虚拟机的正常运行时间（毫秒）
     */
    private long uptime;

    /**
     * Java虚拟机的开始时间
     */
    private Date startTime;

    /**
     * 系统属性
     */
    private Map<String, String> systemProperties;

}
