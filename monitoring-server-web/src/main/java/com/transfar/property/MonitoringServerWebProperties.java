package com.transfar.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 监控配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:16:48
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerWebProperties {

    /**
     * 监控阈值
     */
    private int threshold;

    /**
     * 告警属性
     */
    private MonitoringAlarmProperties alarmProperties;

}
