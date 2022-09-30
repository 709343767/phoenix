package com.gitee.pifeng.monitoring.common.util.server.oshi;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.gitee.pifeng.monitoring.common.domain.server.PowerSourcesDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PowerSource;

import java.util.List;

/**
 * <p>
 * 电池工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 20:12
 */
@Slf4j
public class PowerSourceUtils extends InitOshi {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 20:56
     */
    private PowerSourceUtils() {
    }

    /**
     * <p>
     * 获取电池信息
     * </p>
     *
     * @return {@link PowerSourcesDomain}
     * @author 皮锋
     * @custom.date 2021/1/14 20:26
     */
    public static PowerSourcesDomain getPowerSourcesInfo() {
        try {
            HardwareAbstractionLayer hardwareAbstractionLayer = SYSTEM_INFO.getHardware();
            List<PowerSource> powerSources = hardwareAbstractionLayer.getPowerSources();
            PowerSourcesDomain powerSourcesDomain = PowerSourcesDomain.builder().powerSourceNum(powerSources.size()).build();
            List<PowerSourcesDomain.PowerSourceDomain> powerSourceDomains = Lists.newArrayList();
            for (PowerSource powerSource : powerSources) {
                // 操作系统估计的电源上剩余的估计时间（以秒为单位），如果为正，则剩余秒数。如果为负，则-1.0（计算中）或-2.0（无限制）
                double timeRemainingEstimated = powerSource.getTimeRemainingEstimated();
                String timeRemainingEstimatedStr;
                if (timeRemainingEstimated == -1) {
                    timeRemainingEstimatedStr = "计算中";
                } else if (timeRemainingEstimated == -2) {
                    timeRemainingEstimatedStr = "无限制";
                } else {
                    timeRemainingEstimatedStr = DateUtil.formatBetween((long) timeRemainingEstimated * 1000L);
                }
                PowerSourcesDomain.PowerSourceDomain powerSourceDomain = PowerSourcesDomain.PowerSourceDomain.builder()
                        .amperage(NumberUtil.round(powerSource.getAmperage(), 2).doubleValue() + "mA")
                        .chemistry(powerSource.getChemistry())
                        .currentCapacity(powerSource.getCurrentCapacity() + powerSource.getCapacityUnits().name().replace("M", "m").replace("H", "h"))
                        .cycleCount(powerSource.getCycleCount() == -1 ? "未知" : String.valueOf(powerSource.getCycleCount()))
                        .designCapacity(powerSource.getDesignCapacity() + powerSource.getCapacityUnits().name().replace("M", "m").replace("H", "h"))
                        .deviceName(powerSource.getDeviceName())
                        .isCharging(powerSource.isCharging())
                        .isDischarging(powerSource.isDischarging())
                        .isPowerOnLine(powerSource.isPowerOnLine())
                        .manufactureDate(powerSource.getManufactureDate() == null ? "未知" : DateTimeUtils.localDateToString(powerSource.getManufactureDate()))
                        .manufacturer(powerSource.getManufacturer())
                        .maxCapacity(powerSource.getMaxCapacity() + powerSource.getCapacityUnits().name().replace("M", "m").replace("H", "h"))
                        .name(powerSource.getName())
                        .powerUsageRate(powerSource.getPowerUsageRate() + "mW")
                        .remainingCapacityPercent(NumberUtil.round(powerSource.getRemainingCapacityPercent(), 4).doubleValue())
                        .serialNumber(powerSource.getSerialNumber())
                        .temperature(powerSource.getTemperature() == 0 ? "未知" : powerSource.getTemperature() + "℃")
                        .timeRemainingEstimated(timeRemainingEstimatedStr)
                        .timeRemainingInstant(DateUtil.formatBetween((long) powerSource.getTimeRemainingInstant() * 1000L))
                        .voltage(powerSource.getVoltage() == -1 ? "未知" : NumberUtil.round(powerSource.getVoltage(), 2).doubleValue() + "V")
                        .build();
                powerSourceDomains.add(powerSourceDomain);
            }
            powerSourcesDomain.setPowerSourceDomainList(powerSourceDomains);
            return powerSourcesDomain;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}