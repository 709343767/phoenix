package com.imby.server.business.server.entity;

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
    @TableId("ID")
    private int id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 物理内存总量
     */
    @TableField("MEM_TOTAL")
    private String menTotal;

    /**
     * 物理内存使用量
     */
    @TableField("MEM_USED")
    private String menUsed;

    /**
     * 物理内存剩余量
     */
    @TableField("MEM_FREE")
    private String menFree;

    /**
     * 物理内存使用率
     */
    @TableField("MEN_USED_PERCENT")
    private String menUsedPercent;

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
