package com.imby.server.config;

import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.util.PropertiesUtils;
import com.imby.server.property.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * 监控属性加载器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 14:13
 */
public class MonitoringPropertiesLoader {

    /**
     * <p>
     * 获取监控配置属性
     * </p>
     *
     * @param configFileName 配置文件名字
     * @return {@link MonitoringServerWebProperties}
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020/3/23 14:16
     */
    public MonitoringServerWebProperties getMonitoringServerWebProperties(final String configFileName) throws IOException {
        Properties properties = PropertiesUtils.loadProperties(configFileName);
        // 监控阈值(默认值为5)
        String thresholdStr = StringUtils.trimToNull(properties.getProperty("monitoring.threshold"));
        int threshold = StringUtils.isBlank(thresholdStr) ? 5 : Integer.parseInt(thresholdStr);
        // 缺省[监控告警是否打开(默认打开)]
        String alarmEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.enable"));
        boolean alarmEnable = StringUtils.isBlank(alarmEnableStr) || Boolean.parseBoolean(alarmEnableStr);
        // 缺省[监控告警级别，四级：INFO < WARN < ERROR < FATAL，(默认WARN，level >= WARN告警)]
        String alarmLevelTmp = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.level"));
        String alarmLevel = StringUtils.isBlank(alarmLevelTmp) ? AlarmLevelEnums.WARN.name() : alarmLevelTmp;
        // 告警方式
        String alarmWay = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.way"));
        // 告警短信号码
        String alarmSmsPhoneNumbers = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.phone-numbers"));
        // 告警短信地址
        String alarmSmsAddress = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.address"));
        // 告警短信协议
        String alarmSmsProtocol = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.protocol"));
        // 告警短信接口商家
        String alarmSmsEnterprise = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.enterprise"));
        // 收件人邮箱地址
        String mailTo = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.mail.to"));
        // 缺省[是否监控网络(默认打开)]
        String monitoringEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.network.enable"));
        boolean monitoringEnable = StringUtils.isBlank(monitoringEnableStr) || Boolean.parseBoolean(monitoringEnableStr);
        // 封装短信属性
        MonitoringSmsProperties smsProperties = this.wrapMonitoringSmsProperties(alarmSmsPhoneNumbers, alarmSmsAddress, alarmSmsProtocol, alarmSmsEnterprise);
        // 封装邮箱属性
        MonitoringMailProperties mailProperties = this.wrapMonitoringMailProperties(mailTo);
        // 封装告警属性
        MonitoringAlarmProperties alarmProperties = this.wrapMonitoringAlarmProperties(alarmEnable, alarmLevel, alarmWay, smsProperties, mailProperties);
        // 封装网络属性
        MonitoringNetworkProperties networkProperties = this.wrapMonitoringNetworkProperties(monitoringEnable);
        // 封装所有监控属性
        return this.wrapMonitoringServerWebProperties(threshold, alarmProperties, networkProperties);
    }

    /**
     * <p>
     * 封装短信配置属性
     * </p>
     *
     * @param alarmSmsPhoneNumbers 告警短信号码
     * @param alarmSmsAddress      告警短信地址
     * @param alarmSmsProtocol     告警短信协议
     * @param alarmSmsEnterprise   告警短信接口商家
     * @return {@link MonitoringSmsProperties}
     * @author 皮锋
     * @custom.date 2020/10/27 20:01
     */
    private MonitoringSmsProperties wrapMonitoringSmsProperties(String alarmSmsPhoneNumbers, String alarmSmsAddress, String alarmSmsProtocol, String alarmSmsEnterprise) {
        MonitoringSmsProperties smsProperties = new MonitoringSmsProperties();
        smsProperties.setAddress(alarmSmsAddress);
        smsProperties.setEnterprise(alarmSmsEnterprise);
        smsProperties.setPhoneNumbers(alarmSmsPhoneNumbers != null ? alarmSmsPhoneNumbers.split(";") : null);
        smsProperties.setProtocol(alarmSmsProtocol);
        return smsProperties;
    }

    /**
     * <p>
     * 封装邮箱配置属性
     * </p>
     *
     * @param mailTo 收件人邮箱地址
     * @return {@link MonitoringMailProperties}
     * @author 皮锋
     * @custom.date 2020/10/27 19:59
     */
    private MonitoringMailProperties wrapMonitoringMailProperties(String mailTo) {
        MonitoringMailProperties mailProperties = new MonitoringMailProperties();
        mailProperties.setTo(mailTo != null ? mailTo.split(";") : null);
        return mailProperties;
    }

    /**
     * <p>
     * 封装告警配置属性
     * </p>
     *
     * @param alarmEnable    告警是否打开
     * @param alarmLevel     告警级别
     * @param alarmWay       告警方式
     * @param smsProperties  短信配置属性
     * @param mailProperties 邮箱配置属性
     * @return {@link MonitoringAlarmProperties}
     * @author 皮锋
     * @custom.date 2020/10/27 19:55
     */
    private MonitoringAlarmProperties wrapMonitoringAlarmProperties(boolean alarmEnable, String alarmLevel, String alarmWay,
                                                                    MonitoringSmsProperties smsProperties, MonitoringMailProperties mailProperties) {
        MonitoringAlarmProperties alarmProperties = new MonitoringAlarmProperties();
        alarmProperties.setEnable(alarmEnable);
        alarmProperties.setLevel(alarmLevel);
        alarmProperties.setWay(alarmWay != null ? alarmWay.split(";") : null);
        alarmProperties.setSmsProperties(smsProperties);
        alarmProperties.setMailProperties(mailProperties);
        return alarmProperties;
    }

    /**
     * <p>
     * 封装网络配置属性
     * </p>
     *
     * @param monitoringEnable 网络监控是否打开
     * @return {@link MonitoringNetworkProperties}
     * @author 皮锋
     * @custom.date 2020/10/27 20:00
     */
    private MonitoringNetworkProperties wrapMonitoringNetworkProperties(boolean monitoringEnable) {
        MonitoringNetworkProperties networkProperties = new MonitoringNetworkProperties();
        networkProperties.setMonitoringEnable(monitoringEnable);
        return networkProperties;
    }

    /**
     * <p>
     * 封装所有监控配置属性
     * </p>
     *
     * @param threshold         监控阈值
     * @param alarmProperties   告警配置属性
     * @param networkProperties 网络配置属性
     * @return {@link MonitoringServerWebProperties}
     * @author 皮锋
     * @custom.date 2020/10/27 19:57
     */
    private MonitoringServerWebProperties wrapMonitoringServerWebProperties(int threshold, MonitoringAlarmProperties alarmProperties,
                                                                            MonitoringNetworkProperties networkProperties) {
        MonitoringServerWebProperties monitoringServerWebProperties = new MonitoringServerWebProperties();
        monitoringServerWebProperties.setAlarmProperties(alarmProperties);
        monitoringServerWebProperties.setThreshold(threshold);
        monitoringServerWebProperties.setNetworkProperties(networkProperties);
        return monitoringServerWebProperties;
    }

}
