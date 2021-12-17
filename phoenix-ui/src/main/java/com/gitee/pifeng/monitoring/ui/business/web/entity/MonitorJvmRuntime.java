package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "MonitorJvmRuntime对象", description = "java虚拟机运行时信息表")
public class MonitorJvmRuntime implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "正在运行的Java虚拟机名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "Java虚拟机实现名称")
    @TableField("VM_NAME")
    private String vmName;

    @ApiModelProperty(value = "Java虚拟机实现供应商")
    @TableField("VM_VENDOR")
    private String vmVendor;

    @ApiModelProperty(value = "Java虚拟机实现版本")
    @TableField("VM_VERSION")
    private String vmVersion;

    @ApiModelProperty(value = "Java虚拟机规范名称")
    @TableField("SPEC_NAME")
    private String specName;

    @ApiModelProperty(value = "Java虚拟机规范供应商")
    @TableField("SPEC_VENDOR")
    private String specVendor;

    @ApiModelProperty(value = "Java虚拟机规范版本")
    @TableField("SPEC_VERSION")
    private String specVersion;

    @ApiModelProperty(value = "管理接口规范版本")
    @TableField("MANAGEMENT_SPEC_VERSION")
    private String managementSpecVersion;

    @ApiModelProperty(value = "Java类路径")
    @TableField("CLASS_PATH")
    private String classPath;

    @ApiModelProperty(value = "Java库路径")
    @TableField("LIBRARY_PATH")
    private String libraryPath;

    @ApiModelProperty(value = "Java虚拟机是否支持引导类路径")
    @TableField("IS_BOOT_CLASS_PATH_SUPPORTED")
    private String isBootClassPathSupported;

    @ApiModelProperty(value = "引导类路径")
    @TableField("BOOT_CLASS_PATH")
    private String bootClassPath;

    @ApiModelProperty(value = "Java虚拟机入参")
    @TableField("INPUT_ARGUMENTS")
    private String inputArguments;

    @ApiModelProperty(value = "Java虚拟机的正常运行时间（毫秒）")
    @TableField("UPTIME")
    private String uptime;

    @ApiModelProperty(value = "Java虚拟机的开始时间")
    @TableField("START_TIME")
    private Date startTime;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
