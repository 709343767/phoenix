package com.transfar.server.property;

import lombok.*;

/**
 * <p>
 * 告警配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:18:45
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringAlarmProperties {

    /**
     * 告警是否打开
     */
    private boolean enable;

    /**
     * 告警级别，四级：INFO < WARN < ERROR < FATAL
     */
    private String level;

    /**
     * 告警方式
     */
    private String type;

    /**
     * 短信配置属性
     */
    private MonitoringSmsProperties smsProperties;

}
