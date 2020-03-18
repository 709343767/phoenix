package com.transfar.agent.config;

import com.transfar.common.property.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * <p>
 * 开发环境监控代理程序配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午10:47:52
 */
@Configuration
@Profile("dev")
@PropertySource("classpath:monitoring-dev.properties")
@Slf4j
public class MonitoringAgentDevConfig {

    /**
     * URL
     */
    @Value("${monitoring.server.url}")
    private String url;

    /**
     * 用户名
     */
    @Value("${monitoring.server.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${monitoring.server.password}")
    private String password;

    /**
     * 当前应用实例名
     */
    @Value("${monitoring.own.instance.name}")
    private String instanceName;

    /**
     * 与服务端发心跳包的频率（秒）
     */
    @Value("${monitoring.heartbeat.rate}")
    private String heartbeatRate;

    /**
     * 与服务端发服务器信息的频率（秒）
     */
    @Value("${monitoring.server-info.rate}")
    private String serverInfoRate;

    /**
     * <p>
     * 把配置信息实例化到spring容器
     * </p>
     *
     * @return MonitoringProperties
     * @author 皮锋
     * @custom.date 2020年3月6日 上午11:08:17
     */
    @Bean
    public MonitoringProperties monitoringProperties() {
        MonitoringProperties monitoringProperties = new MonitoringProperties();
        MonitoringServerProperties serverProperties = new MonitoringServerProperties();
        serverProperties.setUrl(this.url);
        serverProperties.setUsername(this.username);
        serverProperties.setPassword(this.password);
        MonitoringOwnProperties ownProperties = new MonitoringOwnProperties();
        ownProperties.setInstanceName(this.instanceName);
        MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
        heartbeatProperties.setRate(Long.parseLong(this.heartbeatRate));
        MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
        monitoringServerInfoProperties.setRate(Long.parseLong(this.serverInfoRate));
        monitoringProperties.setServerProperties(serverProperties);
        monitoringProperties.setOwnProperties(ownProperties);
        monitoringProperties.setHeartbeatProperties(heartbeatProperties);
        monitoringProperties.setMonitoringServerInfoProperties(monitoringServerInfoProperties);
        log.info("开发环境监控配置加载成功！");
        return monitoringProperties;
    }

}
