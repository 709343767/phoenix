package com.imby.agent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

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
@PropertySource(value = "classpath:monitoring-dev.properties", encoding = "utf-8")
public class MonitoringAgentDevConfig extends AbstractMonitoringAgentConfig {
}
