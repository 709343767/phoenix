package com.gitee.pifeng.monitoring.server.config.phoenix;

import com.gitee.pifeng.monitoring.common.property.client.MonitoringProperties;
import com.gitee.pifeng.monitoring.plug.Monitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * 生产环境监控配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/18 14:13
 */
@Configuration
@Profile("prod")
public class MonitoringServerProdConfig {

    /**
     * <p>
     * 开启监控
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2021/5/13 20:22
     */
    @Bean
    @Primary
    public MonitoringProperties initMonitoring() {
        return Monitor.start(null, "monitoring-prod.properties");
    }

}
