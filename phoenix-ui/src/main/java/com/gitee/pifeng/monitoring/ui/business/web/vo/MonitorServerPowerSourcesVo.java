package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerPowerSources;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器电池信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/15 20:00
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器电池信息表现层对象")
public class MonitorServerPowerSourcesVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "操作系统级别的电源名称")
    private String name;

    @ApiModelProperty(value = "电池序号")
    private Integer powerSourcesNo;

    @ApiModelProperty(value = "设备级别的电源名称")
    private String deviceName;

    @ApiModelProperty(value = "剩余容量百分比")
    private Double remainingCapacityPercent;

    @ApiModelProperty(value = "操作系统估计的电源上剩余的估计时间（以毫秒为单位）")
    private String timeRemainingEstimated;

    @ApiModelProperty(value = "估计的电源剩余时间（以毫秒为单位），由电池报告")
    private String timeRemainingInstant;

    @ApiModelProperty(value = "电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）")
    private String powerUsageRate;

    @ApiModelProperty(value = "电池电压，以伏特为单位")
    private String voltage;

    @ApiModelProperty(value = "电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）")
    private String amperage;

    @ApiModelProperty(value = "是否已插入外部电源")
    private String isPowerOnLine;

    @ApiModelProperty(value = "电池是否正在充电")
    private String isCharging;

    @ApiModelProperty(value = "电池是否正在放电")
    private String isDischarging;

    @ApiModelProperty(value = "电池的当前（剩余）容量")
    private String currentCapacity;

    @ApiModelProperty(value = "电池的最大容量")
    private String maxCapacity;

    @ApiModelProperty(value = "电池的设计（原始）容量")
    private String designCapacity;

    @ApiModelProperty(value = "电池的循环计数（如果知道）")
    private String cycleCount;

    @ApiModelProperty(value = "电池化学成分（例如，锂离子电池）")
    private String chemistry;

    @ApiModelProperty(value = "电池的生产日期")
    private String manufactureDate;

    @ApiModelProperty(value = "电池制造商的名称")
    private String manufacturer;

    @ApiModelProperty(value = "电池的序列号")
    private String serialNumber;

    @ApiModelProperty(value = "电池温度，以摄氏度为单位")
    private String temperature;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerPowerSourcesVo转MonitorServerPowerSources
     * </p>
     *
     * @return {@link MonitorServerPowerSources}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerPowerSources convertTo() {
        MonitorServerPowerSources monitorServerPowerSources = MonitorServerPowerSources.builder().build();
        BeanUtils.copyProperties(this, monitorServerPowerSources);
        return monitorServerPowerSources;
    }

    /**
     * <p>
     * MonitorServerPowerSources转MonitorServerPowerSourcesVo
     * </p>
     *
     * @param monitorServerPowerSources {@link MonitorServerPowerSources}
     * @return {@link MonitorServerPowerSourcesVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerPowerSourcesVo convertFor(MonitorServerPowerSources monitorServerPowerSources) {
        if (null != monitorServerPowerSources) {
            BeanUtils.copyProperties(monitorServerPowerSources, this);
        }
        return this;
    }

}
