package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
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
public class MonitoringAlarmProperties implements ISuperBean {

    /**
     * 告警是否打开
     */
    private boolean enable;

    /**
     * 告警级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    private AlarmLevelEnums levelEnum;

    /**
     * 告警方式
     */
    private AlarmWayEnums[] wayEnums;

    /**
     * 短信配置属性
     */
    private MonitoringAlarmSmsProperties smsProperties;

    /**
     * 邮箱配置属性
     */
    private MonitoringAlarmMailProperties mailProperties;

}
