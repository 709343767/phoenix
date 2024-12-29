package com.gitee.pifeng.monitoring.plug.core;

import com.gitee.pifeng.monitoring.common.constant.CommFrameworkTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.LanguageTypeConstants;
import com.gitee.pifeng.monitoring.common.exception.ErrorConfigParamException;
import com.gitee.pifeng.monitoring.common.exception.NotFoundConfigFileException;
import com.gitee.pifeng.monitoring.common.exception.NotFoundConfigParamException;
import com.gitee.pifeng.monitoring.common.property.client.*;
import com.gitee.pifeng.monitoring.common.util.DirUtils;
import com.gitee.pifeng.monitoring.common.util.PropertiesUtils;
import com.gitee.pifeng.monitoring.common.util.server.IpAddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
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
    private static final MonitoringProperties MONITORING_PROPERTIES = new MonitoringProperties();

    /**
     * <p>
     * 获取监控属性
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2023年5月19日 下午8:23:32
     */
    public static MonitoringProperties getMonitoringProperties() {
        return MONITORING_PROPERTIES;
    }

    /**
     * <p>
     * 验证监控属性配置是否正确
     * </p>
     *
     * @param monitoringProperties {@link MonitoringProperties}
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2024/4/7 10:57
     */
    public static MonitoringProperties verify(MonitoringProperties monitoringProperties) {
        // 解析配置信息
        analysis(null, monitoringProperties, true);
        log.info("监控配置项：{}", MONITORING_PROPERTIES.toJsonString());
        log.info("验证监控配置成功！");
        // 返回监控属性
        return MONITORING_PROPERTIES;
    }

    /**
     * <p>
     * 加载监控配置信息
     * </p>
     * 指定的filepath &gt; 当前工作目录/config/ &gt; 当前工作目录/ &gt; 指定的classpath &gt; classpath:/config/ &gt; classpath:/
     *
     * @param configPath 配置文件路径
     * @param configName 配置文件名称
     * @return {@link MonitoringProperties}
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @throws NotFoundConfigFileException  找不到配置文件异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午3:36:32
     */
    public static MonitoringProperties load(String configPath, String configName)
            throws NotFoundConfigParamException, NotFoundConfigFileException, ErrorConfigParamException {
        // 如果没有填写配置文件路径，默认在根路径
        configPath = StringUtils.defaultIfBlank(configPath, "");
        // 如果没有写配置文件名字，默认为：monitoring.properties
        configName = StringUtils.defaultIfBlank(configName, "monitoring.properties");
        Properties properties;
        try {
            if (!StringUtils.startsWith(configPath, "filepath:")) {
                throw new IllegalArgumentException();
            }
            String path = StringUtils.removeStart(configPath, "filepath:");
            properties = PropertiesUtils.loadPropertiesInFilepath(StringUtils.replace(path + configName, "/", File.separator));
        } catch (Exception e1) {
            try {
                String filePath;
                try {
                    // 获取Jar同级目录
                    filePath = DirUtils.getJarDirectory() + File.separator + "config" + File.separator + configName;
                } catch (Exception e) {
                    filePath = "config" + File.separator + configName;
                }
                properties = PropertiesUtils.loadPropertiesInFilepath(filePath);
            } catch (Exception e2) {
                try {
                    String filePath;
                    try {
                        // 获取Jar同级目录
                        filePath = DirUtils.getJarDirectory() + File.separator + configName;
                    } catch (Exception e) {
                        filePath = configName;
                    }
                    properties = PropertiesUtils.loadPropertiesInFilepath(filePath);
                } catch (Exception e3) {
                    try {
                        if (!StringUtils.startsWith(configPath, "classpath:")) {
                            throw new IllegalArgumentException();
                        }
                        String path = StringUtils.removeStart(configPath, "classpath:");
                        properties = PropertiesUtils.loadPropertiesInClasspath(path + configName);
                    } catch (Exception e4) {
                        try {
                            properties = PropertiesUtils.loadPropertiesInClasspath("config/" + configName);
                        } catch (Exception e5) {
                            try {
                                properties = PropertiesUtils.loadPropertiesInClasspath(configName);
                            } catch (Exception e6) {
                                throw new NotFoundConfigFileException("监控程序找不到监控配置文件！");
                            }
                        }
                    }
                }
            }
        }
        log.info("监控配置项：{}", properties);
        // 解析配置文件
        analysis(properties, null, false);
        log.info("加载监控配置成功！");
        // 返回监控属性
        return MONITORING_PROPERTIES;
    }

    /**
     * <p>
     * 解析配置信息
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午3:51:47
     */
    private static void analysis(Properties properties, MonitoringProperties monitoringProperties, boolean hasMonitoringProperties)
            throws NotFoundConfigParamException, ErrorConfigParamException {
        // 封装数据
        wrapMonitoringCommProperties(properties, monitoringProperties, hasMonitoringProperties);
        wrapMonitoringInstanceProperties(properties, monitoringProperties, hasMonitoringProperties);
        wrapMonitoringHeartbeatProperties(properties, monitoringProperties, hasMonitoringProperties);
        wrapMonitoringServerInfoProperties(properties, monitoringProperties, hasMonitoringProperties);
        wrapMonitoringJvmInfoProperties(properties, monitoringProperties, hasMonitoringProperties);
    }

    /**
     * <p>
     * 封装与通信相关的监控属性
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020/10/27 20:29
     */
    private static void wrapMonitoringCommProperties(Properties properties,
                                                     MonitoringProperties monitoringProperties,
                                                     boolean hasMonitoringProperties)
            throws NotFoundConfigParamException, ErrorConfigParamException {
        // 与通信相关的监控属性
        MonitoringCommProperties monitoringCommProperties;
        // 与服务端或者代理端通信的通信框架类型
        String commFrameworkType;
        if (hasMonitoringProperties) {
            monitoringCommProperties = monitoringProperties.getComm() == null ? new MonitoringCommProperties() : monitoringProperties.getComm();
            commFrameworkType = monitoringCommProperties.getCommFrameworkType().getName();
            // 没有配置通信框架类型 或者 通信框架是apacheHttpComponents
            if (StringUtils.isBlank(commFrameworkType) || StringUtils.equalsIgnoreCase(commFrameworkType, CommFrameworkTypeEnums.APACHE_HTTP_COMPONENTS.getName())) {
                monitoringCommProperties.setCommFrameworkType(CommFrameworkTypeEnums.APACHE_HTTP_COMPONENTS);
                MonitoringCommHttpProperties monitoringCommHttpProperties = wrapMonitoringCommHttpProperties(properties, monitoringProperties, true);
                monitoringCommProperties.setHttp(monitoringCommHttpProperties);
            }
        } else {
            monitoringCommProperties = new MonitoringCommProperties();
            commFrameworkType = StringUtils.trimToNull(properties.getProperty("monitoring.comm.comm-framework-type"));
            // 没有配置通信框架类型 或者 通信框架是apacheHttpComponents
            if (StringUtils.isBlank(commFrameworkType) || StringUtils.equalsIgnoreCase(commFrameworkType, CommFrameworkTypeEnums.APACHE_HTTP_COMPONENTS.getName())) {
                monitoringCommProperties.setCommFrameworkType(CommFrameworkTypeEnums.APACHE_HTTP_COMPONENTS);
                MonitoringCommHttpProperties monitoringCommHttpProperties = wrapMonitoringCommHttpProperties(properties, monitoringProperties, false);
                monitoringCommProperties.setHttp(monitoringCommHttpProperties);
            }
        }
        MONITORING_PROPERTIES.setComm(monitoringCommProperties);
    }

    /**
     * <p>
     * 封装与HTTP通信相关的监控属性
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @return {@link MonitoringCommHttpProperties} 与HTTP通信相关的监控属性
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020/10/27 20:29
     */
    private static MonitoringCommHttpProperties wrapMonitoringCommHttpProperties(Properties properties,
                                                                                 MonitoringProperties monitoringProperties,
                                                                                 boolean hasMonitoringProperties)
            throws NotFoundConfigParamException, ErrorConfigParamException {
        // 监控服务端url
        String serverUrl;
        // 缺省[连接超时时间（毫秒），默认：15秒]
        int connectTimeout;
        // 缺省[等待数据超时时间（毫秒），默认：15秒]
        int socketTimeout;
        // 缺省[从连接池获取连接的等待超时时间（毫秒），默认：15秒]
        int connectionRequestTimeout;
        if (hasMonitoringProperties) {
            MonitoringCommProperties comm = monitoringProperties.getComm() == null ? new MonitoringCommProperties() : monitoringProperties.getComm();
            MonitoringCommHttpProperties http = comm.getHttp() == null ? new MonitoringCommHttpProperties() : comm.getHttp();
            serverUrl = http.getUrl();
            connectTimeout = http.getConnectTimeout() == null ? 15000 : http.getConnectTimeout();
            socketTimeout = http.getSocketTimeout() == null ? 15000 : http.getSocketTimeout();
            connectionRequestTimeout = http.getConnectionRequestTimeout() == null ? 15000 : http.getConnectionRequestTimeout();
        } else {
            serverUrl = StringUtils.trimToNull(properties.getProperty("monitoring.comm.http.url"));
            String connectTimeoutStr = StringUtils.trimToNull(properties.getProperty("monitoring.comm.http.connect-timeout"));
            connectTimeout = StringUtils.isBlank(connectTimeoutStr) ? 15000 : Integer.parseInt(connectTimeoutStr);
            String socketTimeoutStr = StringUtils.trimToNull(properties.getProperty("monitoring.comm.http.socket-timeout"));
            socketTimeout = StringUtils.isBlank(socketTimeoutStr) ? 15000 : Integer.parseInt(socketTimeoutStr);
            String connectionRequestTimeoutStr = StringUtils.trimToNull(properties.getProperty("monitoring.comm.http.connection-request-timeout"));
            connectionRequestTimeout = StringUtils.isBlank(connectionRequestTimeoutStr) ? 15000 : Integer.parseInt(connectionRequestTimeoutStr);
        }
        // 没有配置连接
        if (StringUtils.isBlank(serverUrl)) {
            throw new NotFoundConfigParamException("监控程序找不到监控服务端(代理端)HTTP(S) URL配置！");
        }
        int minTimeout = 0;
        if (connectTimeout <= minTimeout) {
            throw new ErrorConfigParamException("HTTP(S)连接超时时间必须大于0秒！");
        }
        if (socketTimeout <= minTimeout) {
            throw new ErrorConfigParamException("HTTP(S)等待数据超时时间必须大于0秒！");
        }
        if (connectionRequestTimeout <= minTimeout) {
            throw new ErrorConfigParamException("从连接池获取HTTP(S)连接的等待超时时间必须大于0秒！");
        }
        MonitoringCommHttpProperties monitoringCommHttpProperties = new MonitoringCommHttpProperties();
        monitoringCommHttpProperties.setUrl(serverUrl);
        monitoringCommHttpProperties.setConnectTimeout(connectTimeout);
        monitoringCommHttpProperties.setSocketTimeout(socketTimeout);
        monitoringCommHttpProperties.setConnectionRequestTimeout(connectionRequestTimeout);
        return monitoringCommHttpProperties;
    }

    /**
     * <p>
     * 封装应用程序监控属性
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @author 皮锋
     * @custom.date 2020/10/27 20:29
     */
    private static void wrapMonitoringInstanceProperties(Properties properties,
                                                         MonitoringProperties monitoringProperties,
                                                         boolean hasMonitoringProperties)
            throws ErrorConfigParamException, NotFoundConfigParamException {
        // 缺省[实例次序(整数)，默认为1]
        int instanceOrder;
        // 缺省[实例端点类型（服务端、代理端、客户端、UI端），默认客户端]
        String instanceEndpoint;
        // 必填[实例名称，一般为项目名]
        String instanceName;
        // 缺省[实例描述，默认没有描述信息]
        String instanceDesc;
        // 缺省[程序语言，默认：JAVA]
        String instanceLanguage;
        if (hasMonitoringProperties) {
            MonitoringInstanceProperties instance = monitoringProperties.getInstance() == null ? new MonitoringInstanceProperties() : monitoringProperties.getInstance();
            instanceOrder = instance.getOrder() == null ? 1 : instance.getOrder();
            instanceEndpoint = instance.getEndpoint();
            instanceName = instance.getName();
            instanceDesc = instance.getDesc();
            instanceLanguage = instance.getLanguage();
        } else {
            String instanceOrderStr = StringUtils.trimToNull(properties.getProperty("monitoring.instance.order"));
            instanceOrder = StringUtils.isBlank(instanceOrderStr) ? 1 : Integer.parseInt(instanceOrderStr);
            instanceEndpoint = StringUtils.trimToNull(properties.getProperty("monitoring.instance.endpoint"));
            instanceName = StringUtils.trimToNull(properties.getProperty("monitoring.instance.name"));
            instanceDesc = StringUtils.trimToNull(properties.getProperty("monitoring.instance.desc"));
            instanceLanguage = StringUtils.trimToNull(properties.getProperty("monitoring.instance.language"));
        }
        if (StringUtils.isBlank(instanceEndpoint)) {
            instanceEndpoint = EndpointTypeEnums.CLIENT.getNameEn();
        }
        if (!(StringUtils.equals(instanceEndpoint, EndpointTypeEnums.CLIENT.getNameEn())
                || StringUtils.equals(instanceEndpoint, EndpointTypeEnums.AGENT.getNameEn())
                || StringUtils.equals(instanceEndpoint, EndpointTypeEnums.SERVER.getNameEn())
                || StringUtils.equals(instanceEndpoint, EndpointTypeEnums.UI.getNameEn())
        )) {
            throw new ErrorConfigParamException("实例端点类型只能为（server、agent、client、ui）其中之一！");
        }
        // 没有实例名称
        if (StringUtils.isBlank(instanceName)) {
            throw new NotFoundConfigParamException("监控程序找不到实例名称配置！");
        }
        if (StringUtils.isBlank(instanceLanguage)) {
            instanceLanguage = LanguageTypeConstants.JAVA;
        }
        MonitoringInstanceProperties instanceProperties = new MonitoringInstanceProperties();
        instanceProperties.setOrder(instanceOrder);
        instanceProperties.setEndpoint(instanceEndpoint);
        instanceProperties.setName(instanceName);
        instanceProperties.setDesc(instanceDesc);
        instanceProperties.setLanguage(instanceLanguage);
        MONITORING_PROPERTIES.setInstance(instanceProperties);
    }

    /**
     * <p>
     * 封装心跳属性
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @throws ErrorConfigParamException 错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020/10/27 20:32
     */
    private static void wrapMonitoringHeartbeatProperties(Properties properties,
                                                          MonitoringProperties monitoringProperties,
                                                          boolean hasMonitoringProperties)
            throws ErrorConfigParamException {
        // 缺省[与服务端或者代理端发心跳包的频率（秒），默认30秒，最小不能小于30秒]
        long heartbeatRate;
        if (hasMonitoringProperties) {
            MonitoringHeartbeatProperties heartbeat = monitoringProperties.getHeartbeat() == null ? new MonitoringHeartbeatProperties() : monitoringProperties.getHeartbeat();
            heartbeatRate = heartbeat.getRate() == null ? 30L : heartbeat.getRate();
        } else {
            // 缺省[与服务端或者代理端发心跳包的频率（秒），默认30秒，最小不能小于30秒]
            String heartbeatRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.heartbeat.rate"));
            heartbeatRate = StringUtils.isBlank(heartbeatRateStr) ? 30L : Long.parseLong(heartbeatRateStr);
        }
        // 频率配置不正确
        long minimum = 30L;
        if (heartbeatRate < minimum) {
            throw new ErrorConfigParamException("心跳频率最小不能小于30秒！");
        }
        MonitoringHeartbeatProperties heartbeatProperties = new MonitoringHeartbeatProperties();
        heartbeatProperties.setRate(heartbeatRate);
        MONITORING_PROPERTIES.setHeartbeat(heartbeatProperties);
    }

    /**
     * <p>
     * 封装服务器信息属性
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @throws ErrorConfigParamException 错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020/10/27 20:30
     */
    private static void wrapMonitoringServerInfoProperties(Properties properties,
                                                           MonitoringProperties monitoringProperties,
                                                           boolean hasMonitoringProperties)
            throws ErrorConfigParamException {
        // 缺省[是否采集服务器信息，默认false]
        boolean serverInfoEnable;
        // 缺省[与服务端或者代理端发服务器信息包的频率（秒），默认60秒，最小不能小于30秒]
        long serverInfoRate;
        // 缺省[服务器本机ip地址，默认：自动获取]
        String serverInfoIp;
        // 缺省[是否使用sigar采集服务器信息，默认：false]
        boolean serverInfoUserSigarEnable;
        if (hasMonitoringProperties) {
            MonitoringServerInfoProperties serverInfo = monitoringProperties.getServerInfo() == null ? new MonitoringServerInfoProperties() : monitoringProperties.getServerInfo();
            serverInfoEnable = serverInfo.getEnable() != null && serverInfo.getEnable();
            serverInfoRate = serverInfo.getRate() == null ? 60L : serverInfo.getRate();
            serverInfoIp = serverInfo.getIp();
            serverInfoUserSigarEnable = serverInfo.getUserSigarEnable() != null && serverInfo.getUserSigarEnable();
        } else {
            // 缺省[是否采集服务器信息，默认false]
            String serverInfoEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.enable"));
            serverInfoEnable = !StringUtils.isBlank(serverInfoEnableStr) && Boolean.parseBoolean(serverInfoEnableStr);
            // 缺省[与服务端或者代理端发服务器信息包的频率（秒），默认60秒，最小不能小于30秒]
            String serverInfoRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.rate"));
            serverInfoRate = StringUtils.isBlank(serverInfoRateStr) ? 60L : Long.parseLong(serverInfoRateStr);
            // 缺省[服务器本机ip地址，默认：自动获取]
            serverInfoIp = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.ip"));
            // 缺省[是否使用sigar采集服务器信息，默认：false]
            String serverInfoUserSigarEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.server-info.user-sigar-enable"));
            serverInfoUserSigarEnable = StringUtils.isNotBlank(serverInfoUserSigarEnableStr) && Boolean.parseBoolean(serverInfoUserSigarEnableStr);
        }
        // 频率配置不正确
        long minimum = 30L;
        if (serverInfoRate < minimum) {
            throw new ErrorConfigParamException("获取服务器信息频率最小不能小于30秒！");
        }
        // 是否为合法IP地址（为空的情况不考虑）
        if ((null != serverInfoIp) && (!IpAddressUtils.isIpAddress(serverInfoIp))) {
            throw new ErrorConfigParamException("服务器本机IP不是合法的IPv4地址！");
        }
        MonitoringServerInfoProperties monitoringServerInfoProperties = new MonitoringServerInfoProperties();
        monitoringServerInfoProperties.setEnable(serverInfoEnable);
        monitoringServerInfoProperties.setUserSigarEnable(serverInfoUserSigarEnable);
        monitoringServerInfoProperties.setRate(serverInfoRate);
        monitoringServerInfoProperties.setIp(serverInfoIp);
        MONITORING_PROPERTIES.setServerInfo(monitoringServerInfoProperties);
    }

    /**
     * <p>
     * 封装Java虚拟机信息属性
     * </p>
     *
     * @param properties              配置属性
     * @param monitoringProperties    {@link MonitoringProperties}
     * @param hasMonitoringProperties 是否有监控属性类
     * @throws ErrorConfigParamException 错误的配置参数异常
     * @author 皮锋
     * @custom.date 2020/10/27 20:31
     */
    private static void wrapMonitoringJvmInfoProperties(Properties properties,
                                                        MonitoringProperties monitoringProperties,
                                                        boolean hasMonitoringProperties)
            throws ErrorConfigParamException {
        // 缺省[是否采集Java虚拟机信息，默认false]
        boolean jvmInfoEnable;
        // 缺省[与服务端或者代理端发送Java虚拟机信息的频率（秒），默认60秒，最小不能小于30秒]
        long jvmInfoRate;
        if (hasMonitoringProperties) {
            MonitoringJvmInfoProperties jvmInfo = monitoringProperties.getJvmInfo() == null ? new MonitoringJvmInfoProperties() : monitoringProperties.getJvmInfo();
            jvmInfoEnable = jvmInfo.getEnable() != null && jvmInfo.getEnable();
            jvmInfoRate = jvmInfo.getRate() == null ? 60L : jvmInfo.getRate();
        } else {
            // 缺省[是否采集Java虚拟机信息，默认false]
            String jvmInfoEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.jvm-info.enable"));
            jvmInfoEnable = !StringUtils.isBlank(jvmInfoEnableStr) && Boolean.parseBoolean(jvmInfoEnableStr);
            // 缺省[与服务端或者代理端发送Java虚拟机信息的频率（秒），默认60秒，最小不能小于30秒]
            String jvmInfoRateStr = StringUtils.trimToNull(properties.getProperty("monitoring.jvm-info.rate"));
            jvmInfoRate = StringUtils.isBlank(jvmInfoRateStr) ? 60L : Long.parseLong(jvmInfoRateStr);
        }
        // 频率配置不正确
        long minimum = 30L;
        if (jvmInfoRate < minimum) {
            throw new ErrorConfigParamException("获取Java虚拟机信息频率最小不能小于30秒！");
        }
        MonitoringJvmInfoProperties monitoringJvmInfoProperties = new MonitoringJvmInfoProperties();
        monitoringJvmInfoProperties.setEnable(jvmInfoEnable);
        monitoringJvmInfoProperties.setRate(jvmInfoRate);
        MONITORING_PROPERTIES.setJvmInfo(monitoringJvmInfoProperties);
    }

}
