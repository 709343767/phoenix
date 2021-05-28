package com.gitee.pifeng.monitoring.server.config;

import com.gitee.pifeng.monitoring.starter.annotation.EnableMonitoring;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * 开发环境监控配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午10:47:52
 */
@Configuration
@Profile("dev")
@EnableMonitoring(configFilePath = "classpath:/", configFileName = "monitoring-dev.properties")
public class MonitoringServerDevConfig {
}
