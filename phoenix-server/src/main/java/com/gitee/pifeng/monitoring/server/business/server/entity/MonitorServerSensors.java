package com.gitee.pifeng.monitoring.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_SERVER_SENSORS")
public class MonitorServerSensors {

    /**
     * 主键ID
     */
    @TableId("ID")
    private Long id;

    /**
     * IP地址
     */
    @TableField("IP")
    private String ip;

    /**
     * CPU温度（以摄氏度为单位）（如果可用）
     */
    @TableField("CPU_TEMPERATURE")
    private String cpuTemperature;

    /**
     * CPU电压（以伏特为单位）（如果可用）
     */
    @TableField("CPU_VOLTAGE")
    private String cpuVoltage;

    /**
     * 风扇的转速（rpm）（如果可用）
     */
    @TableField("FAN_SPEED")
    private String fanSpeed;

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
