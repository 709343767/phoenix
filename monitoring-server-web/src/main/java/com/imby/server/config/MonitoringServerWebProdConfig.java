package com.imby.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.imby.server.property.MonitoringServerWebProperties;

import java.io.IOException;

/**
 * <p>
 * 生产环境的监控服务端配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:13:47
 */
@Configuration
@Slf4j
@Profile("prod")
public class MonitoringServerWebProdConfig {

    /**
     * <p>
     * 加载配置信息
     * </p>
     *
     * @return {@link MonitoringServerWebProperties}
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020年3月10日 下午2:28:37
     */
    @Bean
    public MonitoringServerWebProperties loadConfig() throws IOException {
        MonitoringServerWebProperties monitoringServerWebProperties = new MonitoringPropertiesLoader().getMonitoringServerWebProperties("monitoring-prod.properties");
        log.info("生产环境监控配置加载成功！");
        return monitoringServerWebProperties;
    }

}
