package com.imby.server.business.web.entity;

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
 * java虚拟机线程信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_JVM_THREAD")
@ApiModel(value = "MonitorJvmThread对象", description = "java虚拟机线程信息表")
public class MonitorJvmThread implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键IO")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @ApiModelProperty(value = "当前活动线程数")
    @TableField("THREAD_COUNT")
    private Integer threadCount;

    @ApiModelProperty(value = "线程峰值")
    @TableField("PEAK_THREAD_COUNT")
    private Integer peakThreadCount;

    @ApiModelProperty(value = "已创建并已启动的线程总数")
    @TableField("TOTAL_STARTED_THREAD_COUNT")
    private Integer totalStartedThreadCount;

    @ApiModelProperty(value = "当前活动守护线程数")
    @TableField("DAEMON_THREAD_COUNT")
    private Integer daemonThreadCount;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
