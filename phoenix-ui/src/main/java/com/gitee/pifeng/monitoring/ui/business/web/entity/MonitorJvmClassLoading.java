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
@Schema(description = "MonitorJvmClassLoading对象")
public class MonitorJvmClassLoading implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "加载的类的总数")
    @TableField("TOTAL_LOADED_CLASS_COUNT")
    private Integer totalLoadedClassCount;

    @Schema(description = "当前加载的类的总数")
    @TableField("LOADED_CLASS_COUNT")
    private Integer loadedClassCount;

    @Schema(description = "卸载的类总数")
    @TableField("UNLOADED_CLASS_COUNT")
    private Integer unloadedClassCount;

    @Schema(description = "是否启用了类加载系统的详细输出")
    @TableField("IS_VERBOSE")
    private String isVerbose;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
