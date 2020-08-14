package com.imby.agent.config;

import com.imby.common.exception.ErrorConfigParamException;
import com.imby.common.exception.NotFoundConfigParamException;
import com.imby.common.property.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 因为不同环境的监控配置文件配置项相同，只有配置项的值不同，所以可以用抽象类的方式，读取监控配置文件信息。<br>
 * 具体不同环境都有一个对应环境的配置类，来继承此抽象类，以此来指定不同的环境和对应环境的监控配置文件，完成监控配置信息的加载。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/11 21:00
 */
@Slf4j
public abstract class AbstractMonitoringAgentConfig {

    /**
     * URL
     */
    @Value("${monitoring.server.url}")
    private String url;

    /**
     * 当前应用实例ID
     */
    @Value("${monitoring.own.instance.id}")
    private String instanceId;

    /**
     * 当前应用实例名
     */
    @Value("${monitoring.own.instance.name}")
    private String instanceName;

    /**
     * 当前应用实例描述
     */
    @Value("${monitoring.own.instance.desc}")
    private String instanceDesc;

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
     * 是否发送Java虚拟机信息
     */
    @Value("${monitoring.jvm-info.enable}")
    private String jvmInfoEnable;

    /**
     * 与服务端发Java虚拟机信息包的频率（秒）
     */
    @Value("${monitoring.jvm-info.rate}")
    private String jvmInfoRate;

    /**
     * <p>
     * 把配置信息实例化到spring容器
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月6日 上午11:08:17
     */
    @Bean
    public MonitoringProperties monitoringProperties() throws ErrorConfigParamException, NotFoundConfigParamException {
        MonitoringProperties monitoringProperties = new MonitoringProperties();
        MonitoringServerProperties serverProperties = new MonitoringServerProperties();
        // 没有配置连接
        if (StringUtils.isBlank(this.url)) {
            throw new NotFoundConfigParamException("监控程序找不到监控服务端URL配置！");
        }
        serverProperties.setUrl(this.url);
        MonitoringOwnProperties ownProperties = new MonitoringOwnProperties();
        ownProperties.setInstanceId(this.instanceId);
        // 没有实例名称
        if (StringUtils.isBlank(instanceName)) {
            throw new NotFoundConfigParamException("监控程序找不到实例名称配置！");
        }
        ownProperties.setInstanceName(this.instanceName);
        ownProperties.setInstanceDesc(this.instanceDesc);
        MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
        // 心跳频率
        long heartbeatRate = Long.parseLong(this.heartbeatRate);
        // 频率配置不正确
        if (heartbeatRate < 30L) {
            throw new ErrorConfigParamException("心跳频率最小不能小于30秒！");
        }
        heartbeatProperties.setRate(heartbeatRate);
        MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
        monitoringServerInfoProperties.setEnable(Boolean.parseBoolean(this.serverInfoEnable));
        // 发送服务器信息频率
        long serverInfoRate = Long.parseLong(this.serverInfoRate);
        // 频率配置不正确
        if (serverInfoRate < 30L) {
            throw new ErrorConfigParamException("获取服务器信息频率最小不能小于30秒！");
        }
        monitoringServerInfoProperties.setRate(serverInfoRate);
        MonitoringJvmInfoProperties monitoringJvmInfoProperties = new MonitoringJvmInfoProperties();
        monitoringJvmInfoProperties.setEnable(Boolean.parseBoolean(this.jvmInfoEnable));
        // 发送Java虚拟机信息频率
        long jvmInfoRate = Long.parseLong(this.jvmInfoRate);
        // 频率配置不正确
        if (jvmInfoRate < 30L) {
            throw new ErrorConfigParamException("获取Java虚拟机信息频率最小不能小于30秒！");
        }
        monitoringJvmInfoProperties.setRate(jvmInfoRate);
        monitoringProperties.setServerProperties(serverProperties);
        monitoringProperties.setOwnProperties(ownProperties);
        monitoringProperties.setHeartbeatProperties(heartbeatProperties);
        monitoringProperties.setServerInfoProperties(monitoringServerInfoProperties);
        monitoringProperties.setMonitoringJvmInfoProperties(monitoringJvmInfoProperties);
        log.info("监控配置加载成功！");
        return monitoringProperties;
    }

}
