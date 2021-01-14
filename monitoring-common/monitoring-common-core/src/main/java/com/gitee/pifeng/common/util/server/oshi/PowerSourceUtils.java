package com.gitee.pifeng.common.util.server.oshi;

import cn.hutool.core.util.NumberUtil;
import com.gitee.pifeng.common.domain.server.PowerSourcesDomain;
import com.gitee.pifeng.common.util.DateTimeUtils;
import com.google.common.collect.Lists;
import oshi.SystemInfo;
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
public class PowerSourceUtils {

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
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        List<PowerSource> powerSources = hardwareAbstractionLayer.getPowerSources();
        PowerSourcesDomain powerSourcesDomain = PowerSourcesDomain.builder().powerSourceNum(powerSources.size()).build();
        List<PowerSourcesDomain.PowerSourceDomain> powerSourceDomains = Lists.newArrayList();
        for (PowerSource powerSource : powerSources) {
            PowerSourcesDomain.PowerSourceDomain powerSourceDomain = PowerSourcesDomain.PowerSourceDomain.builder()
                    .amperage(NumberUtil.round(powerSource.getAmperage(), 2).doubleValue())
                    .chemistry(powerSource.getChemistry())
                    .currentCapacity(powerSource.getCurrentCapacity())
                    .cycleCount(powerSource.getCycleCount())
                    .designCapacity(powerSource.getDesignCapacity())
                    .deviceName(powerSource.getDeviceName())
                    .isCharging(powerSource.isCharging())
                    .isDischarging(powerSource.isDischarging())
                    .isPowerOnLine(powerSource.isPowerOnLine())
                    .manufactureDate(powerSource.getManufactureDate() != null ? DateTimeUtils.localDateToString(powerSource.getManufactureDate()) : "未知")
                    .manufacturer(powerSource.getManufacturer())
                    .maxCapacity(powerSource.getMaxCapacity())
                    .name(powerSource.getName())
                    .powerUsageRate(powerSource.getPowerUsageRate())
                    .remainingCapacityPercent(NumberUtil.round(powerSource.getRemainingCapacityPercent(), 4).doubleValue())
                    .serialNumber(powerSource.getSerialNumber())
                    .temperature(powerSource.getTemperature() != 0 ? String.valueOf(powerSource.getTemperature()) : "未知")
                    .timeRemainingEstimated(powerSource.getTimeRemainingEstimated())
                    .timeRemainingInstant(powerSource.getTimeRemainingInstant())
                    .voltage(NumberUtil.round(powerSource.getVoltage(), 2).doubleValue())
                    .build();
            powerSourceDomains.add(powerSourceDomain);
        }
        powerSourcesDomain.setPowerSourceDomainList(powerSourceDomains);
        return powerSourcesDomain;
    }

}
