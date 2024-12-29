package com.gitee.pifeng.monitoring.ui.business.web.entity;

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
 * 服务器传感器表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-15
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_SENSORS")
@Schema(description = "MonitorServerSensors对象")
public class MonitorServerSensors implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    @TableId("ID")
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "CPU温度（以摄氏度为单位）（如果可用）")
    @TableField("CPU_TEMPERATURE")
    private String cpuTemperature;

    @Schema(description = "CPU电压（以伏特为单位）（如果可用）")
    @TableField("CPU_VOLTAGE")
    private String cpuVoltage;

    @Schema(description = "风扇的转速（rpm）（如果可用）")
    @TableField("FAN_SPEED")
    private String fanSpeed;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
