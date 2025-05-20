package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器进程表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_PROCESS")
public class MonitorServerProcess {

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
     * 进程ID
     */
    @TableField("PROCESS_ID")
    private Integer processId;

    /**
     * 进程名
     */
    @TableField("NAME")
    private String name;

    /**
     * 执行进程的完整路径
     */
    @TableField("PATH")
    private String path;

    /**
     * 进程命令行
     */
    @TableField("COMMAND_LINE")
    private String commandLine;

    /**
     * 进程当前的工作目录
     */
    @TableField("CURRENT_WORKING_DIRECTORY")
    private String currentWorkingDirectory;

    /**
     * 用户名
     */
    @TableField("USER")
    private String user;

    /**
     * 进程执行状态
     */
    @TableField("STATE")
    private String state;

    /**
     * 进程已启动的毫秒数
     */
    @TableField("UP_TIME")
    private String upTime;

    /**
     * 进程的开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 进程的累积CPU使用率
     */
    @TableField("CPU_LOAD_CUMULATIVE")
    private Double cpuLoadCumulative;

    /**
     * 进程的位数
     */
    @TableField("BITNESS")
    private String bitness;

    /**
     * 占用内存大小（单位：byte）
     */
    @TableField("MEMORY_SIZE")
    private Long memorySize;

    /**
     * 端口号列表（逗号分隔）
     */
    @TableField("PORT")
    private String port;

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
