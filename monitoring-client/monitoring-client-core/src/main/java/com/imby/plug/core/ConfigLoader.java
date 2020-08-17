package com.imby.plug.core;

import com.imby.common.exception.ErrorConfigParamException;
import com.imby.common.exception.NotFoundConfigFileException;
import com.imby.common.exception.NotFoundConfigParamException;
import com.imby.common.property.*;
import com.imby.common.util.PropertiesUtils;
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
public class ConfigLoader {

    /**
     * 监控属性
     */
    public static MonitoringProperties monitoringProperties = new MonitoringProperties();

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
        // 缺省[实例ID，如果配置了实例ID，用配置的ID，如果没配置，系统会自动生成一个ID]
        String instanceId = StringUtils.trimToNull(properties.getProperty("monitoring.own.instance.id"));
        // 必填[实例名称，一般为项目名]
        String instanceName = StringUtils.trimToNull(properties.getProperty("monitoring.own.instance.name"));
        // 缺省[实例描述，默认没有描述信息]
        String instanceDesc = StringUtils.trimToEmpty(properties.getProperty("monitoring.own.instance.desc"));
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
        if (heartbeatRate < 30) {
            throw new ErrorConfigParamException("心跳频率最小不能小于30秒！");
        }
        if (serverInfoRate < 30) {
            throw new ErrorConfigParamException("获取服务器信息频率最小不能小于30秒！");
        }
        if (jvmInfoRate < 30) {
            throw new ErrorConfigParamException("获取Java虚拟机信息频率最小不能小于30秒！");
        }
        // 封装数据
        wrap(serverUrl, instanceId, instanceName, instanceDesc, heartbeatRate, serverInfoEnable,
                serverInfoRate, jvmInfoEnable, jvmInfoRate);
    }

    /**
     * <p>
     * 封装配置信息
     * </p>
     *
     * @param serverUrl        监控服务端url
     * @param instanceId       实例ID
     * @param instanceName     实例名称
     * @param instanceDesc     实例描述
     * @param heartbeatRate    缺省[与服务端或者代理端发心跳包的频率（秒），默认30秒]
     * @param serverInfoEnable 缺省[是否采集服务器信息，默认false]
     * @param serverInfoRate   缺省[与服务端或者代理端发服务器信息包的频率（秒），默认60秒]
     * @param jvmInfoEnable    缺省[是否采集Java虚拟机信息，默认false]
     * @param jvmInfoRate      缺省[与服务端或者代理端发送Java虚拟机信息的频率（秒），默认60秒，最小不能小于30秒]
     * @author 皮锋
     * @custom.date 2020年3月5日 下午4:36:33
     */
    private static void wrap(String serverUrl,
                             String instanceId, String instanceName,
                             String instanceDesc, long heartbeatRate,
                             boolean serverInfoEnable, long serverInfoRate,
                             boolean jvmInfoEnable, long jvmInfoRate) {
        MonitoringServerProperties serverProperties = new MonitoringServerProperties();
        serverProperties.setUrl(serverUrl);
        monitoringProperties.setServerProperties(serverProperties);
        MonitoringOwnProperties ownProperties = new MonitoringOwnProperties();
        ownProperties.setInstanceId(instanceId);
        ownProperties.setInstanceName(instanceName);
        ownProperties.setInstanceDesc(instanceDesc);
        monitoringProperties.setOwnProperties(ownProperties);
        MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
        heartbeatProperties.setRate(heartbeatRate);
        monitoringProperties.setHeartbeatProperties(heartbeatProperties);
        MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
        monitoringServerInfoProperties.setEnable(serverInfoEnable);
        monitoringServerInfoProperties.setRate(serverInfoRate);
        monitoringProperties.setServerInfoProperties(monitoringServerInfoProperties);
        MonitoringJvmInfoProperties monitoringJvmInfoProperties = new MonitoringJvmInfoProperties();
        monitoringJvmInfoProperties.setEnable(jvmInfoEnable);
        monitoringJvmInfoProperties.setRate(jvmInfoRate);
        monitoringProperties.setJvmInfoProperties(monitoringJvmInfoProperties);
    }

}
