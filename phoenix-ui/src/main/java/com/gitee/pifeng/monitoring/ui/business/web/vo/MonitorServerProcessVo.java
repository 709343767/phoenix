package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器进程表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/9/15 14:16
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器进程表现层对象")
public class MonitorServerProcessVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "进程ID")
    private Integer processId;

    @ApiModelProperty(value = "进程名")
    private String name;

    @ApiModelProperty(value = "执行进程的完整路径")
    private String path;

    @ApiModelProperty(value = "进程命令行")
    private String commandLine;

    @ApiModelProperty(value = "进程当前的工作目录")
    private String currentWorkingDirectory;

    @ApiModelProperty(value = "用户名")
    private String user;

    @ApiModelProperty(value = "进程执行状态")
    private String state;

    @ApiModelProperty(value = "进程已启动的毫秒数")
    private String upTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "进程的开始时间")
    private Date startTime;

    @ApiModelProperty(value = "进程的累积CPU使用率")
    private Double cpuLoadCumulative;

    @ApiModelProperty(value = "进程的位数")
    private String bitness;

    @ApiModelProperty(value = "占用内存大小（单位：byte）")
    private Long memorySize;

    @ApiModelProperty(value = "占用内存大小（智能转换单位后的大小）")
    private String memorySizeStr;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerProcessVo转MonitorServerProcess
     * </p>
     *
     * @return {@link MonitorServerProcess}
     * @author 皮锋
     * @custom.date 2021/9/15 14:16
     */
    public MonitorServerProcess convertTo() {
        MonitorServerProcess monitorServerProcess = MonitorServerProcess.builder().build();
        BeanUtils.copyProperties(this, monitorServerProcess);
        return monitorServerProcess;
    }

    /**
     * <p>
     * MonitorServerProcess转MonitorServerProcessVo
     * </p>
     *
     * @param monitorServerProcess {@link MonitorServerProcess}
     * @return {@link MonitorServerProcessVo}
     * @author 皮锋
     * @custom.date 2021/9/15 14:16
     */
    public MonitorServerProcessVo convertFor(MonitorServerProcess monitorServerProcess) {
        if (null != monitorServerProcess) {
            BeanUtils.copyProperties(monitorServerProcess, this);
        }
        return this;
    }

}
