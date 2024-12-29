package com.gitee.pifeng.monitoring.agent.config;

import com.gitee.pifeng.monitoring.common.property.client.MonitoringProperties;
import com.gitee.pifeng.monitoring.plug.Monitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * 开发环境监控代理配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午10:47:52
 */
@Configuration
@Profile("dev")
public class MonitoringAgentDevConfig {

    /**
     * <p>
     * 开启监控
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2021/5/13 20:17
     */
    @Bean
    @Primary
    public MonitoringProperties initMonitoring() {
        return Monitor.start(null, "monitoring-dev.properties");
    }

}
