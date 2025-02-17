package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerCpuHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器CPU历史记录表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器CPU历史记录表现层对象")
public class MonitorServerCpuHistoryVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "CPU序号")
    private Integer cpuNo;

    @Schema(description = "CPU频率（MHz）")
    private Integer cpuMhz;

    @Schema(description = "CPU卖主")
    private String cpuVendor;

    @Schema(description = "CPU的类别，如：Celeron")
    private String cpuModel;

    @Schema(description = "CPU用户使用率")
    private Double cpuUser;

    @Schema(description = "CPU系统使用率")
    private Double cpuSys;

    @Schema(description = "CPU等待率")
    private Double cpuWait;

    @Schema(description = "CPU错误率")
    private Double cpuNice;

    @Schema(description = "CPU使用率")
    private Double cpuCombined;

    @Schema(description = "CPU剩余率")
    private Double cpuIdle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerCpuHistoryVo转MonitorServerCpuHistory
     * </p>
     *
     * @return {@link MonitorServerCpuHistory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerCpuHistory convertTo() {
        MonitorServerCpuHistory monitorServerCpuHistory = MonitorServerCpuHistory.builder().build();
        BeanUtils.copyProperties(this, monitorServerCpuHistory);
        return monitorServerCpuHistory;
    }

    /**
     * <p>
     * MonitorServerCpuHistory转MonitorServerCpuHistoryVo
     * </p>
     *
     * @param monitorServerCpuHistory {@link MonitorServerCpuHistory}
     * @return {@link MonitorServerCpuHistoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerCpuHistoryVo convertFor(MonitorServerCpuHistory monitorServerCpuHistory) {
        if (null != monitorServerCpuHistory) {
            BeanUtils.copyProperties(monitorServerCpuHistory, this);
        }
        return this;
    }

}
