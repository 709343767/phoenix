package com.gitee.pifeng.agent.config;

import com.gitee.pifeng.starter.annotation.EnableMonitoring;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
@EnableMonitoring(configFilePath = "classpath:/", configFileName = "monitoring-prod.properties")
public class MonitoringAgentProdConfig {
}
