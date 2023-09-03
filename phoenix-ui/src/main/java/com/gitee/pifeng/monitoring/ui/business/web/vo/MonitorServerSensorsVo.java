package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerSensors;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器传感器信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/15 20:10
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "服务器传感器信息表现层对象")
public class MonitorServerSensorsVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "CPU温度（以摄氏度为单位）（如果可用）")
    private String cpuTemperature;

    @Schema(description = "CPU电压（以伏特为单位）（如果可用）")
    private String cpuVoltage;

    @Schema(description = "风扇的转速（rpm）（如果可用）")
    private String fanSpeed;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerSensorsVo转MonitorServerSensors
     * </p>
     *
     * @return {@link MonitorServerSensors}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerSensors convertTo() {
        MonitorServerSensors monitorServerSensors = MonitorServerSensors.builder().build();
        BeanUtils.copyProperties(this, monitorServerSensors);
        return monitorServerSensors;
    }

    /**
     * <p>
     * MonitorServerSensors转MonitorServerSensorsVo
     * </p>
     *
     * @param monitorServerSensors {@link MonitorServerSensors}
     * @return {@link MonitorServerSensorsVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerSensorsVo convertFor(MonitorServerSensors monitorServerSensors) {
        if (null != monitorServerSensors) {
            BeanUtils.copyProperties(monitorServerSensors, this);
        }
        return this;
    }

}