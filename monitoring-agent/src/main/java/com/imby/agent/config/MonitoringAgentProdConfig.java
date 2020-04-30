package com.imby.agent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * <p>
 * 生产环境监控代理配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/18 14:13
 */
@Configuration
@Profile("prod")
@PropertySource("classpath:monitoring-prod.properties")
public class MonitoringAgentProdConfig extends AbstractMonitoringAgentConfig {
}
