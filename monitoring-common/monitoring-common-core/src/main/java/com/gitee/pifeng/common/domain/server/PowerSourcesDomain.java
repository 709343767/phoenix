package com.gitee.pifeng.common.domain.server;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 电池信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 20:15
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PowerSourcesDomain extends AbstractSuperBean {

    /**
     * 电池数量
     */
    private int powerSourceNum;

    /**
     * 电池信息
     */
    private List<PowerSourceDomain> powerSourceDomainList;

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class PowerSourceDomain extends AbstractSuperBean {

        /**
         * 操作系统级别的电源名称
         */
        private String name;

        /**
         * 设备级别的电源名称
         */
        private String deviceName;

        /**
         * 剩余容量百分比
         */
        private double remainingCapacityPercent;

        /**
         * 操作系统估计的电源上剩余的估计时间（以秒为单位）
         */
        private double timeRemainingEstimated;

        /**
         * 估计的电源剩余时间（以秒为单位），由电池报告
         */
        private double timeRemainingInstant;

        /**
         * 电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）
         */
        private double powerUsageRate;

        /**
         * 电池电压，以伏特为单位
         */
        private double voltage;

        /**
         * 电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）
         */
        private double amperage;

        /**
         * 是否已插入外部电源
         */
        private boolean isPowerOnLine;

        /**
         * 电池是否正在充电
         */
        private boolean isCharging;

        /**
         * 电池是否正在放电
         */
        private boolean isDischarging;


        /**
         * 电池的当前（剩余）容量
         */
        private int currentCapacity;

        /**
         * 电池的最大容量
         */
        private int maxCapacity;

        /**
         * 电池的设计（原始）容量
         */
        private int designCapacity;

        /**
         * 电池的循环计数（如果知道）
         */
        private int cycleCount;

        /**
         * 电池化学成分（例如，锂离子电池）
         */
        private String chemistry;

        /**
         * 电池的生产日期
         */
        private LocalDate manufactureDate;

        /**
         * 电池制造商的名称
         */
        private String manufacturer;

        /**
         * 电池的序列号
         */
        private String serialNumber;

        /**
         * 电池温度，以摄氏度为单位
         */
        private double temperature;
    }

}