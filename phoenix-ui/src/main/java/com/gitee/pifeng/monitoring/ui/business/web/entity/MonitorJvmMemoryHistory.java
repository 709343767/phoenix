package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * java虚拟机内存历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_JVM_MEMORY_HISTORY")
@Schema(description = "MonitorJvmMemoryHistory对象")
public class MonitorJvmMemoryHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "内存类型")
    @TableField("MEMORY_TYPE")
    private String memoryType;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "初始内存量（单位：byte）")
    @TableField("INIT")
    private Long init;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "已用内存量（单位：byte）")
    @TableField("USED")
    private Long used;

    @JsonSerialize(using = ToStringSerializer.class)
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
