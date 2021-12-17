package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "MonitorServerProcess对象", description = "服务器进程表")
public class MonitorServerProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "进程ID")
    @TableField("PROCESS_ID")
    private Integer processId;

    @ApiModelProperty(value = "进程名")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "执行进程的完整路径")
    @TableField("PATH")
    private String path;

    @ApiModelProperty(value = "进程命令行")
    @TableField("COMMAND_LINE")
    private String commandLine;

    @ApiModelProperty(value = "进程当前的工作目录")
    @TableField("CURRENT_WORKING_DIRECTORY")
    private String currentWorkingDirectory;

    @ApiModelProperty(value = "用户名")
    @TableField("USER")
    private String user;

    @ApiModelProperty(value = "进程执行状态")
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "进程已启动的毫秒数")
    @TableField("UP_TIME")
    private String upTime;

    @ApiModelProperty(value = "进程的开始时间")
    @TableField("START_TIME")
    private Date startTime;

    @ApiModelProperty(value = "进程的累积CPU使用率")
    @TableField("CPU_LOAD_CUMULATIVE")
    private Double cpuLoadCumulative;

    @ApiModelProperty(value = "进程的位数")
    @TableField("BITNESS")
    private String bitness;

    @ApiModelProperty(value = "占用内存大小（单位：byte）")
    @TableField("MEMORY_SIZE")
    private Long memorySize;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
