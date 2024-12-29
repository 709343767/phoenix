package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

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
public class MonitoringProperties implements ISuperBean {

    /**
     * 监控阈值
     */
    private int threshold;

    /**
     * 告警属性
     */
    private MonitoringAlarmProperties alarmProperties;

    /**
     * 网络属性
     */
    private MonitoringNetworkProperties networkProperties;

    /**
     * TCP属性
     */
    private MonitoringTcpProperties tcpProperties;

    /**
     * HTTP属性
     */
    private MonitoringHttpProperties httpProperties;

    /**
     * 应用实例配置属性
     */
    private MonitoringInstanceProperties instanceProperties;

    /**
     * 服务器属性
     */
    private MonitoringServerProperties serverProperties;

    /**
     * 数据库配置属性
     */
    private MonitoringDbProperties dbProperties;

}
