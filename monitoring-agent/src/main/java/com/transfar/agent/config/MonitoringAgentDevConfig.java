package com.transfar.agent.config;

import com.transfar.common.exception.ErrorConfigParamException;
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
     * 是否发送服务器信息
     */
    @Value("${monitoring.server-info.enable}")
    private String serverInfoEnable;

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
     * @return {@link MonitoringProperties}
     * @throws ErrorConfigParamException 错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月6日 上午11:08:17
     */
    @Bean
    public MonitoringProperties monitoringProperties() throws ErrorConfigParamException {
        MonitoringProperties monitoringProperties = new MonitoringProperties();
        MonitoringServerProperties serverProperties = new MonitoringServerProperties();
        serverProperties.setUrl(this.url);
        serverProperties.setUsername(this.username);
        serverProperties.setPassword(this.password);
        MonitoringOwnProperties ownProperties = new MonitoringOwnProperties();
        ownProperties.setInstanceName(this.instanceName);
        MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
        // 心跳频率
        long heartbeatRate = Long.parseLong(this.heartbeatRate);
        // 频率配置不正确
        if (heartbeatRate < 30) {
            throw new ErrorConfigParamException("心跳频率最小不能小于30秒！");
        }
        heartbeatProperties.setRate(heartbeatRate);
        MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
        monitoringServerInfoProperties.setEnable(Boolean.parseBoolean(this.serverInfoEnable));
        // 发送服务器信息频率
        long serverInfoRate = Long.parseLong(this.serverInfoRate);
        // 频率配置不正确
        if (serverInfoRate < 30) {
            throw new ErrorConfigParamException("获取服务器信息频率最小不能小于30秒！");
        }
        monitoringServerInfoProperties.setRate(serverInfoRate);
        monitoringProperties.setServerProperties(serverProperties);
        monitoringProperties.setOwnProperties(ownProperties);
        monitoringProperties.setHeartbeatProperties(heartbeatProperties);
        monitoringProperties.setServerInfoProperties(monitoringServerInfoProperties);
        log.info("开发环境监控配置加载成功！");
        return monitoringProperties;
    }

}
