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
@Schema(description = "MonitorJvmMemory对象")
public class MonitorJvmMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "内存类型")
    @TableField("MEMORY_TYPE")
    private String memoryType;

    @Schema(description = "初始内存量（单位：byte）")
    @TableField("INIT")
    private Long init;

    @Schema(description = "已用内存量（单位：byte）")
    @TableField("USED")
    private Long used;

    @Schema(description = "提交内存量（单位：byte）")
    @TableField("COMMITTED")
    private Long committed;

    @Schema(description = "最大内存量（单位：byte，可能存在未定义）")
    @TableField("MAX")
    private String max;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
