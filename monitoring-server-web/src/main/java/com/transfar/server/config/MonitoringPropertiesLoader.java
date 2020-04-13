package com.transfar.server.config;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.util.PropertiesUtils;
import com.transfar.server.property.*;
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
        // 告警类型
        String alarmType = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.type"));
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
        return wrap(threshold, alarmEnable, alarmLevel, alarmType, alarmSmsPhoneNumbers, alarmSmsAddress, alarmSmsProtocol,
                alarmSmsEnterprise, mailTo, monitoringEnable);
    }

    /**
     * <p>
     * 封装属性对象
     * </p>
     *
     * @param threshold            监控阈值
     * @param alarmEnable          告警是否打开
     * @param alarmLevel           告警级别
     * @param alarmType            告警类型
     * @param alarmSmsPhoneNumbers 告警短信号码
     * @param alarmSmsAddress      告警短信地址
     * @param alarmSmsProtocol     告警短信协议
     * @param alarmSmsEnterprise   告警短信接口商家
     * @param mailTo               收件人邮箱地址
     * @param monitoringEnable     网络监控是否打开
     * @return {@link MonitoringServerWebProperties}
     * @author 皮锋
     * @custom.date 2020年3月10日 下午2:39:38
     */
    private MonitoringServerWebProperties wrap(int threshold, boolean alarmEnable, String alarmLevel, String alarmType,
                                               String alarmSmsPhoneNumbers, String alarmSmsAddress,
                                               String alarmSmsProtocol, String alarmSmsEnterprise,
                                               String mailTo, boolean monitoringEnable) {
        // 告警属性
        MonitoringAlarmProperties alarmProperties = new MonitoringAlarmProperties();
        alarmProperties.setEnable(alarmEnable);
        alarmProperties.setLevel(alarmLevel);
        alarmProperties.setType(alarmType);
        // 短信属性
        MonitoringSmsProperties smsProperties = new MonitoringSmsProperties();
        smsProperties.setAddress(alarmSmsAddress);
        smsProperties.setEnterprise(alarmSmsEnterprise);
        smsProperties.setPhoneNumbers(alarmSmsPhoneNumbers != null ? alarmSmsPhoneNumbers.split(";") : null);
        smsProperties.setProtocol(alarmSmsProtocol);
        alarmProperties.setSmsProperties(smsProperties);
        // 邮箱属性
        MonitoringMailProperties mailProperties = new MonitoringMailProperties();
        mailProperties.setTo(mailTo != null ? mailTo.split(";") : null);
        alarmProperties.setMailProperties(mailProperties);
        // 网络属性
        MonitoringNetworkProperties networkProperties = new MonitoringNetworkProperties();
        networkProperties.setMonitoringEnable(monitoringEnable);
        // 所有监控属性
        MonitoringServerWebProperties monitoringServerWebProperties = new MonitoringServerWebProperties();
        monitoringServerWebProperties.setAlarmProperties(alarmProperties);
        monitoringServerWebProperties.setThreshold(threshold);
        monitoringServerWebProperties.setNetworkProperties(networkProperties);
        return monitoringServerWebProperties;
    }
}
