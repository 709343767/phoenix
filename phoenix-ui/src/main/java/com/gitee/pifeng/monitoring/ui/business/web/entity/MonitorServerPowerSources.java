package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_POWER_SOURCES")
@Schema(description = "MonitorServerPowerSources对象")
public class MonitorServerPowerSources implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "电池序号")
    @TableField("POWER_SOURCES_NO")
    private Integer powerSourcesNo;

    @Schema(description = "操作系统级别的电源名称")
    @TableField("NAME")
    private String name;

    @Schema(description = "设备级别的电源名称")
    @TableField("DEVICE_NAME")
    private String deviceName;

    @Schema(description = "剩余容量百分比")
    @TableField("REMAINING_CAPACITY_PERCENT")
    private Double remainingCapacityPercent;

    @Schema(description = "操作系统估计的电源上剩余的估计时间（以毫秒为单位）")
    @TableField("TIME_REMAINING_ESTIMATED")
    private String timeRemainingEstimated;

    @Schema(description = "估计的电源剩余时间（以毫秒为单位），由电池报告")
    @TableField("TIME_REMAINING_INSTANT")
    private String timeRemainingInstant;

    @Schema(description = "电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）")
    @TableField("POWER_USAGE_RATE")
    private String powerUsageRate;

    @Schema(description = "电池电压，以伏特为单位")
    @TableField("VOLTAGE")
    private String voltage;

    @Schema(description = "电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）")
    @TableField("AMPERAGE")
    private String amperage;

    @Schema(description = "是否已插入外部电源")
    @TableField("IS_POWER_ON_LINE")
    private String isPowerOnLine;

    @Schema(description = "电池是否正在充电")
    @TableField("IS_CHARGING")
    private String isCharging;

    @Schema(description = "电池是否正在放电")
    @TableField("IS_DISCHARGING")
    private String isDischarging;

    @Schema(description = "电池的当前（剩余）容量")
    @TableField("CURRENT_CAPACITY")
    private String currentCapacity;

    @Schema(description = "电池的最大容量")
    @TableField("MAX_CAPACITY")
    private String maxCapacity;

    @Schema(description = "电池的设计（原始）容量")
    @TableField("DESIGN_CAPACITY")
    private String designCapacity;

    @Schema(description = "电池的循环计数（如果知道）")
    @TableField("CYCLE_COUNT")
    private String cycleCount;

    @Schema(description = "电池化学成分（例如，锂离子电池）")
    @TableField("CHEMISTRY")
    private String chemistry;

    @Schema(description = "电池的生产日期")
    @TableField("MANUFACTURE_DATE")
    private String manufactureDate;

    @Schema(description = "电池制造商的名称")
    @TableField("MANUFACTURER")
    private String manufacturer;

    @Schema(description = "电池的序列号")
    @TableField("SERIAL_NUMBER")
    private String serialNumber;

    @Schema(description = "电池温度，以摄氏度为单位")
    @TableField("TEMPERATURE")
    private String temperature;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
