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
 * 服务器内存表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_MEMORY")
@Schema(description = "MonitorServerMemory对象")
public class MonitorServerMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "物理内存总量（单位：byte）")
    @TableField("MEM_TOTAL")
    private Long memTotal;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "物理内存使用量（单位：byte）")
    @TableField("MEM_USED")
    private Long memUsed;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "物理内存剩余量（单位：byte）")
    @TableField("MEM_FREE")
    private Long memFree;

    @Schema(description = "物理内存使用率")
    @TableField("MEN_USED_PERCENT")
    private Double menUsedPercent;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "交换区总量（单位：byte）")
    @TableField("SWAP_TOTAL")
    private Long swapTotal;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "交换区使用量（单位：byte）")
    @TableField("SWAP_USED")
    private Long swapUsed;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "交换区剩余量（单位：byte）")
    @TableField("SWAP_FREE")
    private Long swapFree;

    @Schema(description = "交换区使用率")
    @TableField("SWAP_USED_PERCENT")
    private Double swapUsedPercent;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
