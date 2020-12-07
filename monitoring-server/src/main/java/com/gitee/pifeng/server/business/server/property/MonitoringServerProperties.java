package com.gitee.pifeng.server.business.server.property;

import lombok.*;

/**
 * <p>
 * 服务器配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/14 21:53
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringServerProperties {

    /**
     * 是否监控服务器
     */
    private boolean enable;

    /**
     * 服务器CPU配置属性
     */
    private MonitoringServerCpuProperties serverCpuProperties;

    /**
     * 服务器磁盘配置属性
     */
    private MonitoringServerDiskProperties serverDiskProperties;

    /**
     * 服务器内存配置属性
     */
    private MonitoringServerMemoryProperties serverMemoryProperties;

}
