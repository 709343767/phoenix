package com.imby.server.property;

import lombok.*;

/**
 * <p>
 * 网络配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 10:46
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringNetworkProperties {

    /**
     * 是否监控网络
     */
    private boolean monitoringEnable;
}
