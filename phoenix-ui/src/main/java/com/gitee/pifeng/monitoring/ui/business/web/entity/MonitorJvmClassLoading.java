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
 * java虚拟机类加载信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_JVM_CLASS_LOADING")
@ApiModel(value = "MonitorJvmClassLoading对象", description = "java虚拟机类加载信息表")
public class MonitorJvmClassLoading implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "加载的类的总数")
    @TableField("TOTAL_LOADED_CLASS_COUNT")
    private Integer totalLoadedClassCount;

    @ApiModelProperty(value = "当前加载的类的总数")
    @TableField("LOADED_CLASS_COUNT")
    private Integer loadedClassCount;

    @ApiModelProperty(value = "卸载的类总数")
    @TableField("UNLOADED_CLASS_COUNT")
    private Integer unloadedClassCount;

    @ApiModelProperty(value = "是否启用了类加载系统的详细输出")
    @TableField("IS_VERBOSE")
    private String isVerbose;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
