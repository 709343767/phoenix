package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/3 16:47
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器信息表现层对象")
public class MonitorServerVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "服务器名")
    private String serverName;

    @Schema(description = "服务器摘要")
    private String serverSummary;

    @Schema(description = "服务器状态（0：离线，1：在线）")
    private String isOnline;

    @Schema(description = "是否开启监控（0：不开启监控；1：开启监控）")
    private String isEnableMonitor;

    @Schema(description = "是否开启告警（0：不开启告警；1：开启告警）")
    private String isEnableAlarm;

    @Schema(description = "离线次数")
    private Integer offlineCount;

    @Schema(description = "连接频率")
    private Integer connFrequency;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "最后心跳时间")
    private String finalHeartbeat;

    @Schema(description = "操作系统名称")
    private String osName;

    @Schema(description = "CPU使用率")
    private Double cpuUserPercent;

    @Schema(description = "内存使用率")
    private Double menUsedPercent;

    @Schema(description = "服务器负载")
    private Double loadAverage;

    @Schema(description = "下行带宽")
    private String downloadBps;

    @Schema(description = "上行带宽")
    private String uploadBps;

    @Schema(description = "监控环境")
    private String monitorEnv;

    @Schema(description = "监控分组")
    private String monitorGroup;

    /**
     * <p>
     * MonitorServerVo转MonitorServer
     * </p>
     *
     * @return {@link MonitorServer}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServer convertTo() {
        MonitorServer monitorServer = MonitorServer.builder().build();
        BeanUtils.copyProperties(this, monitorServer);
        return monitorServer;
    }

    /**
     * <p>
     * MonitorServer转MonitorServerVo
     * </p>
     *
     * @param monitorServer {@link MonitorServer}
     * @return {@link MonitorServerVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerVo convertFor(MonitorServer monitorServer) {
        if (null != monitorServer) {
            BeanUtils.copyProperties(monitorServer, this);
        }
        return this;
    }

}
