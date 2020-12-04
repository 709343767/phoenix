package com.imby.plug.core;

import com.imby.common.constant.EndpointTypeEnums;
import com.imby.common.constant.LanguageTypeConstants;
import com.imby.common.exception.ErrorConfigParamException;
import com.imby.common.exception.NotFoundConfigFileException;
import com.imby.common.exception.NotFoundConfigParamException;
import com.imby.common.util.PropertiesUtils;
import com.imby.plug.property.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * 监控客户端加载监控配置文件信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:06:21
 */
@Slf4j
public class ConfigLoader {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private ConfigLoader() {
    }

    /**
     * 监控属性
     */
    public static final MonitoringProperties MONITORING_PROPERTIES = new MonitoringProperties();

    /**
     * <p>
     * 加载配置信息
     * </p>
     *
     * @param configPath 配置文件路径
     * @param configName 配置文件名称
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @throws NotFoundConfigFileException  找不到配置文件异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午3:36:32
     */
    public static void load(String configPath, String configName)
            throws NotFoundConfigParamException, NotFoundConfigFileException, ErrorConfigParamException {
        // 如果没有填写配置文件路径，默认在根路径
        if (StringUtils.isBlank(configPath)) {
            configPath = "";
        }
        // 如果没有写配置文件名字，默认为：monitoring.properties
        if (StringUtils.isBlank(configName)) {
            configName = "monitoring.properties";
        }
        Properties properties;
        try {
            properties = PropertiesUtils.loadProperties(configPath + configName);
            log.info("监控配置项：{}", properties.toString());
        } catch (IOException e) {
            throw new NotFoundConfigFileException("监控程序找不到配置文件！");
        }
        // 解析配置文件
        analysis(properties);
    }

    /**
     * <p>
     * 解析配置信息
     * </p>
     *
     * @param properties 配置信息
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午3:51:47
     */
    private static void analysis(Properties properties) throws NotFoundConfigParamException, ErrorConfigParamException {
        // 监控服务端url
        String serverUrl = StringUtils.trimToNull(properties.getProperty("monitoring.server.url"));
        // 缺省[实例次序(整数)，默认为1]
        String instanceOrderStr = StringUtils.trimToNull(properties.getProperty("monitoring.own.instance.order"));
        int instanceOrder = StringUtils.isBlank(instanceOrderStr) ? 1 : Integer.parseInt(instanceOrderStr);
        // 缺省[实例端点类型（服务端、代理端、客户端），默认客户端]
        String instanceEndpoint = StringUtils.trimToNull(properties.getProperty("monitoring.own.instance.endpoint"));
        if (StringUtils.isBlank(instanceEndpoint)) {
            instanceEndpoint = EndpointTypeEnums.CLIENT.getNameEn();
        }
        if (!(StringUtils.equals(instanceEndpoint, EndpointTypeEnums.CLIENT.getNameEn())
                || StringUtils.equals(instanceEndpoint, EndpointTypeEnums.AGENT.getNameEn())
                || StringUtils.equals(instanceEndpoint, EndpointTypeEnums.SERVER.getNameEn()))) {
            throw new ErrorConfigParamException("实例端点类型只能为（server、agent、client）其中之一！");
        }
        // 必填[实例名称，一般为项目名]
        String instanceName = StringUtils.trimToNull(properties.getProperty("monitoring.own.instance.name"));
        // 缺省[实例描述，默认没有描述信息]
        String instanceDesc = StringUtils.trimToEmpty(properties.getProperty("monitoring.own.instance.desc"));
        // 缺省[程序语言，默认：JAVA]
        String instanceLanguage = StringUtils.trimToEmpty(properties.getProperty("monitoring.own.instance.language"));
        if (StringUtils.isBlank(instanceLanguage)) {
            instanceLanguage = LanguageTypeConstants.JAVA;
        }
        // 缺省[与服务端或者代理端发心跳包的频率（秒），默认30秒，最小不能小于30秒]
        String heartbeatRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.heartbeat.rate"));
        long heartbeatRate = StringUtils.isBlank(heartbeatRateStr) ? 30L : Long.parseLong(heartbeatRateStr);
        // 缺省[是否采集服务器信息，默认false]
        String serverInfoEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.enable"));
        boolean serverInfoEnable = !StringUtils.isBlank(serverInfoEnableStr)
                && Boolean.parseBoolean(serverInfoEnableStr);
        // 缺省[与服务端或者代理端发服务器信息包的频率（秒），默认60秒，最小不能小于30秒]
        String serverInfoRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.rate"));
        long serverInfoRate = StringUtils.isBlank(serverInfoRateStr) ? 60L : Long.parseLong(serverInfoRateStr);
        // 缺省[是否采集Java虚拟机信息，默认false]
        String jvmInfoEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.jvm-info.enable"));
        boolean jvmInfoEnable = !StringUtils.isBlank(jvmInfoEnableStr)
                && Boolean.parseBoolean(jvmInfoEnableStr);
        // 缺省[与服务端或者代理端发送Java虚拟机信息的频率（秒），默认60秒，最小不能小于30秒]
        String jvmInfoRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.jvm-info.rate"));
        long jvmInfoRate = StringUtils.isBlank(jvmInfoRateStr) ? 60L : Long.parseLong(jvmInfoRateStr);
        // 没有配置连接
        if (StringUtils.isBlank(serverUrl)) {
            throw new NotFoundConfigParamException("监控程序找不到监控服务端URL配置！");
        }
        // 没有实例名称
        if (StringUtils.isBlank(instanceName)) {
            throw new NotFoundConfigParamException("监控程序找不到实例名称配置！");
        }
        // 频率配置不正确
        long minimum = 30L;
        if (heartbeatRate < minimum) {
            throw new ErrorConfigParamException("心跳频率最小不能小于30秒！");
        }
        if (serverInfoRate < minimum) {
            throw new ErrorConfigParamException("获取服务器信息频率最小不能小于30秒！");
        }
        if (jvmInfoRate < minimum) {
            throw new ErrorConfigParamException("获取Java虚拟机信息频率最小不能小于30秒！");
        }
        // 封装数据
        wrapMonitoringServerProperties(serverUrl);
        wrapMonitoringOwnProperties(instanceOrder, instanceEndpoint, instanceName, instanceDesc, instanceLanguage);
        wrapMonitoringHeartbeatProperties(heartbeatRate);
        wrapMonitoringServerInfoProperties(serverInfoEnable, serverInfoRate);
        wrapMonitoringJvmInfoProperties(jvmInfoEnable, jvmInfoRate);
    }

    /**
     * <p>
     * 封装与服务端相关的监控属性
     * </p>
     *
     * @param serverUrl 监控服务端url
     * @author 皮锋
     * @custom.date 2020/10/27 20:29
     */
    private static void wrapMonitoringServerProperties(String serverUrl) {
        MonitoringServerProperties serverProperties = new MonitoringServerProperties();
        serverProperties.setUrl(serverUrl);
        MONITORING_PROPERTIES.setServerProperties(serverProperties);
    }

    /**
     * <p>
     * 封装与自己相关的监控属性
     * </p>
     *
     * @param instanceOrder    实例次序(整数)
     * @param instanceEndpoint 实例端点类型（服务端、代理端、客户端）
     * @param instanceName     实例名称
     * @param instanceDesc     实例描述
     * @param instanceLanguage 程序语言
     * @author 皮锋
     * @custom.date 2020/10/27 20:29
     */
    private static void wrapMonitoringOwnProperties(int instanceOrder, String instanceEndpoint, String instanceName, String instanceDesc, String instanceLanguage) {
        MonitoringOwnProperties ownProperties = new MonitoringOwnProperties();
        ownProperties.setInstanceOrder(instanceOrder);
        ownProperties.setInstanceEndpoint(instanceEndpoint);
        ownProperties.setInstanceName(instanceName);
        ownProperties.setInstanceDesc(instanceDesc);
        ownProperties.setInstanceLanguage(instanceLanguage);
        MONITORING_PROPERTIES.setOwnProperties(ownProperties);
    }

    /**
     * <p>
     * 封装服务器信息属性
     * </p>
     *
     * @param serverInfoEnable 是否采集服务器信息
     * @param serverInfoRate   与服务端或者代理端发服务器信息包的频率（秒）
     * @author 皮锋
     * @custom.date 2020/10/27 20:30
     */
    private static void wrapMonitoringServerInfoProperties(boolean serverInfoEnable, long serverInfoRate) {
        MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
        monitoringServerInfoProperties.setEnable(serverInfoEnable);
        monitoringServerInfoProperties.setRate(serverInfoRate);
        MONITORING_PROPERTIES.setServerInfoProperties(monitoringServerInfoProperties);
    }

    /**
     * <p>
     * 封装Java虚拟机信息属性
     * </p>
     *
     * @param jvmInfoEnable 是否采集Java虚拟机信息
     * @param jvmInfoRate   与服务端或者代理端发送Java虚拟机信息的频率（秒）
     * @author 皮锋
     * @custom.date 2020/10/27 20:31
     */
    private static void wrapMonitoringJvmInfoProperties(boolean jvmInfoEnable, long jvmInfoRate) {
        MonitoringJvmInfoProperties monitoringJvmInfoProperties = new MonitoringJvmInfoProperties();
        monitoringJvmInfoProperties.setEnable(jvmInfoEnable);
        monitoringJvmInfoProperties.setRate(jvmInfoRate);
        MONITORING_PROPERTIES.setJvmInfoProperties(monitoringJvmInfoProperties);
    }

    /**
     * <p>
     * 封装心跳属性
     * </p>
     *
     * @param heartbeatRate 与服务端或者代理端发心跳包的频率（秒）
     * @author 皮锋
     * @custom.date 2020/10/27 20:32
     */
    private static void wrapMonitoringHeartbeatProperties(long heartbeatRate) {
        MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
        heartbeatProperties.setRate(heartbeatRate);
        MONITORING_PROPERTIES.setHeartbeatProperties(heartbeatProperties);
    }

}
