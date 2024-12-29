package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 数据库表空间配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/7 14:49
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringDbTableSpaceProperties implements ISuperBean {

    /**
     * 是否监控数据库表空间
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
