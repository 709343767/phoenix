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
 * java虚拟机内存历史记录表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_JVM_MEMORY_HISTORY")
public class MonitorJvmMemoryHistory {

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
     * 内存类型
     */
    @TableField("MEMORY_TYPE")
    private String memoryType;

    /**
     * 初始内存量（单位：byte）
     */
    @TableField("INIT")
    private Long init;

    /**
     * 已用内存量（单位：byte）
     */
    @TableField("USED")
    private Long used;

    /**
     * 提交内存量（单位：byte）
     */
    @TableField("COMMITTED")
    private Long committed;

    /**
     * 最大内存量（单位：byte，可能存在未定义）
     */
    @TableField("MAX")
    private String max;

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
