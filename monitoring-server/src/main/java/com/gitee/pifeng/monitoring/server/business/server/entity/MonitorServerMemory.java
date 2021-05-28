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
 * 服务器内存表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/11 16:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_MEMORY")
public class MonitorServerMemory {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 物理内存总量（单位：byte）
     */
    @TableField("MEM_TOTAL")
    private Long menTotal;

    /**
     * 物理内存使用量（单位：byte）
     */
    @TableField("MEM_USED")
    private Long menUsed;

    /**
     * 物理内存剩余量（单位：byte）
     */
    @TableField("MEM_FREE")
    private Long menFree;

    /**
     * 物理内存使用率
     */
    @TableField("MEN_USED_PERCENT")
    private Double menUsedPercent;

    /**
     * 交换区总量（单位：byte）
     */
    @TableField("SWAP_TOTAL")
    private Long swapTotal;

    /**
     * 交换区使用量（单位：byte）
     */
    @TableField("SWAP_USED")
    private Long swapUsed;

    /**
     * 交换区剩余量（单位：byte）
     */
    @TableField("SWAP_FREE")
    private Long swapFree;

    /**
     * 交换区使用率
     */
    @TableField("SWAP_USED_PERCENT")
    private Double swapUsedPercent;

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
