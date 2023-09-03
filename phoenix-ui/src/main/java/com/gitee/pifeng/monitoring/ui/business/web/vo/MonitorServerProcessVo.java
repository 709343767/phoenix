package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcess;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "服务器进程表现层对象")
public class MonitorServerProcessVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "进程ID")
    private Integer processId;

    @Schema(description = "进程名")
    private String name;

    @Schema(description = "执行进程的完整路径")
    private String path;

    @Schema(description = "进程命令行")
    private String commandLine;

    @Schema(description = "进程当前的工作目录")
    private String currentWorkingDirectory;

    @Schema(description = "用户名")
    private String user;

    @Schema(description = "进程执行状态")
    private String state;

    @Schema(description = "进程已启动的毫秒数")
    private String upTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "进程的开始时间")
    private Date startTime;

    @Schema(description = "进程的累积CPU使用率")
    private Double cpuLoadCumulative;

    @Schema(description = "进程的位数")
    private String bitness;

    @Schema(description = "占用内存大小（单位：byte）")
    private Long memorySize;

    @Schema(description = "占用内存大小（智能转换单位后的大小）")
    private String memorySizeStr;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
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
