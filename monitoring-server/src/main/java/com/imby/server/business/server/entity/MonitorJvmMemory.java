package com.imby.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * java虚拟机内存信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/28 9:22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_JVM_MEMORY")
public class MonitorJvmMemory {

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
     * 初始内存量
     */
    @TableField("INIT")
    private String init;

    /**
     * 已用内存量
     */
    @TableField("USED")
    private String used;

    /**
     * 提交内存量
     */
    @TableField("COMMITTED")
    private String committed;

    /**
     * 最大内存量
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
