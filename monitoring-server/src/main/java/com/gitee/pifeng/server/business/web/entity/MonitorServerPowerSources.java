package com.gitee.pifeng.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "MonitorServerPowerSources对象", description = "服务器电池表")
public class MonitorServerPowerSources implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "电池序号")
    @TableField("POWER_SOURCES_NO")
    private Integer powerSourcesNo;

    @ApiModelProperty(value = "操作系统级别的电源名称")
    @TableField("NAME")
    private String name;

    @ApiModelProperty(value = "设备级别的电源名称")
    @TableField("DEVICE_NAME")
    private String deviceName;

    @ApiModelProperty(value = "剩余容量百分比")
    @TableField("REMAINING_CAPACITY_PERCENT")
    private Double remainingCapacityPercent;

    @ApiModelProperty(value = "操作系统估计的电源上剩余的估计时间（以秒为单位）")
    @TableField("TIME_REMAINING_ESTIMATED")
    private Double timeRemainingEstimated;

    @ApiModelProperty(value = "估计的电源剩余时间（以秒为单位），由电池报告")
    @TableField("TIME_REMAINING_INSTANT")
    private Double timeRemainingInstant;

    @ApiModelProperty(value = "电池的电源使用功率，以毫瓦（mW）为单位（如果为正，则为充电率；如果为负，则为放电速率。）")
    @TableField("POWER_USAGE_RATE")
    private Double powerUsageRate;

    @ApiModelProperty(value = "电池电压，以伏特为单位")
    @TableField("VOLTAGE")
    private Double voltage;

    @ApiModelProperty(value = "电池的电流，以毫安（mA）为单位（如果为正，则为充电电流；如果为负，则为放点电流。）")
    @TableField("AMPERAGE")
    private Double amperage;

    @ApiModelProperty(value = "是否已插入外部电源")
    @TableField("IS_POWER_ON_LINE")
    private String isPowerOnLine;

    @ApiModelProperty(value = "电池是否正在充电")
    @TableField("IS_CHARGING")
    private String isCharging;

    @ApiModelProperty(value = "电池是否正在放电")
    @TableField("IS_DISCHARGING")
    private String isDischarging;

    @ApiModelProperty(value = "电池的当前（剩余）容量")
    @TableField("CURRENT_CAPACITY")
    private Integer currentCapacity;

    @ApiModelProperty(value = "电池的最大容量")
    @TableField("MAX_CAPACITY")
    private Integer maxCapacity;

    @ApiModelProperty(value = "电池的设计（原始）容量")
    @TableField("DESIGN_CAPACITY")
    private Integer designCapacity;

    @ApiModelProperty(value = "电池的循环计数（如果知道）")
    @TableField("CYCLE_COUNT")
    private Integer cycleCount;

    @ApiModelProperty(value = "电池化学成分（例如，锂离子电池）")
    @TableField("CHEMISTRY")
    private String chemistry;

    @ApiModelProperty(value = "电池的生产日期")
    @TableField("MANUFACTURE_DATE")
    private String manufactureDate;

    @ApiModelProperty(value = "电池制造商的名称")
    @TableField("MANUFACTURER")
    private String manufacturer;

    @ApiModelProperty(value = "电池的序列号")
    @TableField("SERIAL_NUMBER")
    private String serialNumber;

    @ApiModelProperty(value = "电池温度，以摄氏度为单位")
    @TableField("TEMPERATURE")
    private String temperature;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
