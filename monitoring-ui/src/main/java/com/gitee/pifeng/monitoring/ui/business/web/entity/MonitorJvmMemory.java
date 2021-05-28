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
 * java虚拟机内存信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_JVM_MEMORY")
@ApiModel(value = "MonitorJvmMemory对象", description = "java虚拟机内存信息表")
public class MonitorJvmMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "内存类型")
    @TableField("MEMORY_TYPE")
    private String memoryType;

    @ApiModelProperty(value = "初始内存量（单位：byte）")
    @TableField("INIT")
    private Long init;

    @ApiModelProperty(value = "已用内存量（单位：byte）")
    @TableField("USED")
    private Long used;

    @ApiModelProperty(value = "提交内存量（单位：byte）")
    @TableField("COMMITTED")
    private Long committed;

    @ApiModelProperty(value = "最大内存量（单位：byte，可能存在未定义）")
    @TableField("MAX")
    private String max;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
