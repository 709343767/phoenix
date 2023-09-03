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
@Schema(description = "MonitorServerProcess对象")
public class MonitorServerProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "进程ID")
    @TableField("PROCESS_ID")
    private Integer processId;

    @Schema(description = "进程名")
    @TableField("NAME")
    private String name;

    @Schema(description = "执行进程的完整路径")
    @TableField("PATH")
    private String path;

    @Schema(description = "进程命令行")
    @TableField("COMMAND_LINE")
    private String commandLine;

    @Schema(description = "进程当前的工作目录")
    @TableField("CURRENT_WORKING_DIRECTORY")
    private String currentWorkingDirectory;

    @Schema(description = "用户名")
    @TableField("USER")
    private String user;

    @Schema(description = "进程执行状态")
    @TableField("STATE")
    private String state;

    @Schema(description = "进程已启动的毫秒数")
    @TableField("UP_TIME")
    private String upTime;

    @Schema(description = "进程的开始时间")
    @TableField("START_TIME")
    private Date startTime;

    @Schema(description = "进程的累积CPU使用率")
    @TableField("CPU_LOAD_CUMULATIVE")
    private Double cpuLoadCumulative;

    @Schema(description = "进程的位数")
    @TableField("BITNESS")
    private String bitness;

    @Schema(description = "占用内存大小（单位：byte）")
    @TableField("MEMORY_SIZE")
    private Long memorySize;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
