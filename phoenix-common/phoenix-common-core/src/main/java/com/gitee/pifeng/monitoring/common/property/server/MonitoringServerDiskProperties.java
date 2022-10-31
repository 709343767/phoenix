package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 服务器磁盘配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/14 21:55
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerDiskProperties implements ISuperBean {

    /**
     * 过载阈值
     */
    private double overloadThreshold;

    /**
     * 监控级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    private AlarmLevelEnums levelEnum;

}
