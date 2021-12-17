package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器电池表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_POWER_SOURCES")
public class MonitorServerPowerSources {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * 电池序号
     */
    @TableField("POWER_SOURCES_NO")
    private Integer powerSourcesNo;

    /**
     * 操作系统级别的电源名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 设备级别的电源名称
     */
    @TableField("DEVICE_NAME")
    private String deviceName;

    /**
     * 剩余容量百分比
     */
    @TableField("REMAINING_CAPACITY_PERCENT")
    private Double remainingCapacityPercent;

    /**
     * 操作系统估计的电源上剩余的估计时间（以毫秒为单位）
     */
    @TableField("TIME_REMAINING_ESTIMATED")
    private String timeRemainingEstimated;

    /**
     * 估计的电源剩余时间（以毫秒为单位），由电池报告
     */
    @TableField("TIME_REMAINING_INSTANT")
    private String timeRemainingInstant;

    /**
     * 电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）
     */
    @TableField("POWER_USAGE_RATE")
    private String powerUsageRate;

    /**
     * 电池电压，以伏特为单位
     */
    @TableField("VOLTAGE")
    private String voltage;

    /**
     * 电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）
     */
    @TableField("AMPERAGE")
    private String amperage;

    /**
     * 是否已插入外部电源
     */
    @TableField("IS_POWER_ON_LINE")
    private String isPowerOnLine;

    /**
     * 电池是否正在充电
     */
    @TableField("IS_CHARGING")
    private String isCharging;

    /**
     * 电池是否正在放电
     */
    @TableField("IS_DISCHARGING")
    private String isDischarging;

    /**
     * 电池的当前（剩余）容量
     */
    @TableField("CURRENT_CAPACITY")
    private String currentCapacity;

    /**
     * 电池的最大容量
     */
    @TableField("MAX_CAPACITY")
    private String maxCapacity;

    /**
     * 电池的设计（原始）容量
     */
    @TableField("DESIGN_CAPACITY")
    private String designCapacity;

    /**
     * 电池的循环计数（如果知道）
     */
    @TableField("CYCLE_COUNT")
    private String cycleCount;

    /**
     * 电池化学成分（例如，锂离子电池）
     */
    @TableField("CHEMISTRY")
    private String chemistry;

    /**
     * 电池的生产日期
     */
    @TableField("MANUFACTURE_DATE")
    private String manufactureDate;

    /**
     * 电池制造商的名称
     */
    @TableField("MANUFACTURER")
    private String manufacturer;

    /**
     * 电池的序列号
     */
    @TableField("SERIAL_NUMBER")
    private String serialNumber;

    /**
     * 电池温度，以摄氏度为单位
     */
    @TableField("TEMPERATURE")
    private String temperature;

    /**
     * 新增时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
