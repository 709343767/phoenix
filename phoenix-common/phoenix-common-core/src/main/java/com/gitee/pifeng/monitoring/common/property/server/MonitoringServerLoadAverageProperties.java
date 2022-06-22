package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import lombok.*;

/**
 * <p>
 * 服务器平均负载配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/21 21:36
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerLoadAverageProperties {

    /**
     * 15分钟过载阈值
     */
    private double overloadThreshold15minutes;

    /**
     * 15分钟过载监控级别，四级：INFO &#60; WARN &#60; ERROR &#60; FATAL
     */
    private AlarmLevelEnums levelEnum15minutes;

}
