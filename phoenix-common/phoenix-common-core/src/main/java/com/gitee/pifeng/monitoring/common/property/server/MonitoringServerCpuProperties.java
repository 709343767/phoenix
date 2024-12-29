package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 服务器CPU配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/14 21:54
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerCpuProperties implements ISuperBean {

    /**
     * 是否监控服务器CPU
     */
    private boolean enable;

    /**
     * 告警是否打开
     */
    private boolean alarmEnable;

    /**
     * 过载阈值
     */
    private double overloadThreshold;

    /**
     * 监控级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    private AlarmLevelEnums levelEnum;

}
