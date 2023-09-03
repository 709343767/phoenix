package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * java虚拟机运行时信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_JVM_RUNTIME")
@Schema(description = "MonitorJvmRuntime对象")
public class MonitorJvmRuntime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "正在运行的Java虚拟机名称")
    @TableField("NAME")
    private String name;

    @Schema(description = "Java虚拟机实现名称")
    @TableField("VM_NAME")
    private String vmName;

    @Schema(description = "Java虚拟机实现供应商")
    @TableField("VM_VENDOR")
    private String vmVendor;

    @Schema(description = "Java虚拟机实现版本")
    @TableField("VM_VERSION")
    private String vmVersion;

    @Schema(description = "Java虚拟机规范名称")
    @TableField("SPEC_NAME")
    private String specName;

    @Schema(description = "Java虚拟机规范供应商")
    @TableField("SPEC_VENDOR")
    private String specVendor;

    @Schema(description = "Java虚拟机规范版本")
    @TableField("SPEC_VERSION")
    private String specVersion;

    @Schema(description = "管理接口规范版本")
    @TableField("MANAGEMENT_SPEC_VERSION")
    private String managementSpecVersion;

    @Schema(description = "Java类路径")
    @TableField("CLASS_PATH")
    private String classPath;

    @Schema(description = "Java库路径")
    @TableField("LIBRARY_PATH")
    private String libraryPath;

    @Schema(description = "Java虚拟机是否支持引导类路径")
    @TableField("IS_BOOT_CLASS_PATH_SUPPORTED")
    private String isBootClassPathSupported;

    @Schema(description = "引导类路径")
    @TableField("BOOT_CLASS_PATH")
    private String bootClassPath;

    @Schema(description = "Java虚拟机入参")
    @TableField("INPUT_ARGUMENTS")
    private String inputArguments;

    @Schema(description = "Java虚拟机的正常运行时间（毫秒）")
    @TableField("UPTIME")
    private String uptime;

    @Schema(description = "Java虚拟机的开始时间")
    @TableField("START_TIME")
    private Date startTime;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
