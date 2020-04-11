package com.transfar.agent.config;

import lombok.extern.slf4j.Slf4j;
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
@PropertySource("classpath:monitoring-dev.properties")
@Slf4j
public class MonitoringAgentDevConfig extends AbstractMonitoringAgentConfig {
}
