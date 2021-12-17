package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * java虚拟机运行时信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/27 18:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_JVM_RUNTIME")
public class MonitorJvmRuntime {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;

    /**
     * 正在运行的Java虚拟机名称
     */
    @TableField("NAME")
    private String name;

    /**
     * Java虚拟机实现名称
     */
    @TableField("VM_NAME")
    private String vmName;

    /**
     * Java虚拟机实现供应商
     */
    @TableField("VM_VENDOR")
    private String vmVendor;

    /**
     * Java虚拟机实现版本
     */
    @TableField("VM_VERSION")
    private String vmVersion;

    /**
     * Java虚拟机规范名称
     */
    @TableField("SPEC_NAME")
    private String specName;

    /**
     * Java虚拟机规范供应商
     */
    @TableField("SPEC_VENDOR")
    private String specVendor;

    /**
     * Java虚拟机规范版本
     */
    @TableField("SPEC_VERSION")
    private String specVersion;

    /**
     * 管理接口规范版本
     */
    @TableField("MANAGEMENT_SPEC_VERSION")
    private String managementSpecVersion;

    /**
     * Java类路径
     */
    @TableField("CLASS_PATH")
    private String classPath;

    /**
     * Java库路径
     */
    @TableField("LIBRARY_PATH")
    private String libraryPath;

    /**
     * Java虚拟机是否支持引导类路径
     */
    @TableField("IS_BOOT_CLASS_PATH_SUPPORTED")
    private String isBootClassPathSupported;

    /**
     * 引导类路径
     */
    @TableField("BOOT_CLASS_PATH")
    private String bootClassPath;

    /**
     * Java虚拟机入参
     */
    @TableField("INPUT_ARGUMENTS")
    private String inputArguments;

    /**
     * Java虚拟机的正常运行时间（毫秒）
     */
    @TableField("UPTIME")
    private String uptime;

    /**
     * Java虚拟机的开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 新增时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
