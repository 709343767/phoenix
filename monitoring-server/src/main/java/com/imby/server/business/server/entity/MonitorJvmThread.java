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
 * java虚拟机线程信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/28 9:59
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_JVM_THREAD")
public class MonitorJvmThread {

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
     * 当前活动线程数
     */
    @TableField("THREAD_COUNT")
    private Integer threadCount;

    /**
     * 线程峰值
     */
    @TableField("PEAK_THREAD_COUNT")
    private Integer peakThreadCount;

    /**
     * 已创建并已启动的线程总数
     */
    @TableField("TOTAL_STARTED_THREAD_COUNT")
    private Long totalStartedThreadCount;

    /**
     * 当前活动守护线程数
     */
    @TableField("DAEMON_THREAD_COUNT")
    private Integer daemonThreadCount;

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
