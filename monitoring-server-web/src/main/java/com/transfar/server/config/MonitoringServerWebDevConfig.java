package com.transfar.server.config;

import com.transfar.common.util.PropertiesUtils;
import com.transfar.server.property.MonitoringAlarmProperties;
import com.transfar.server.property.MonitoringNetworkProperties;
import com.transfar.server.property.MonitoringServerWebProperties;
import com.transfar.server.property.MonitoringSmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * 开发环境的监控服务端配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/18 13:46
 */
@Configuration
@Slf4j
@Profile("dev")
public class MonitoringServerWebDevConfig {

    /*
     * 监控配置属性
     */
    //private static MonitoringServerWebProperties monitoringServerWebProperties = new MonitoringServerWebProperties();

    /**
     * <p>
     * 加载配置信息
     * </p>
     *
     * @return MonitoringServerWebProperties
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020年3月10日 下午2:28:37
     */
    @Bean
    public MonitoringServerWebProperties loadConfig() throws IOException {
        Properties properties = PropertiesUtils.loadProperties("monitoring-dev.properties");
        // 监控阈值(默认值为5)
        String thresholdStr = StringUtils.trimToNull(properties.getProperty("monitoring.threshold"));
        int threshold = StringUtils.isBlank(thresholdStr) ? 5 : Integer.parseInt(thresholdStr);
        // 缺省[监控告警是否打开(默认打开)]
        String alarmEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.enable"));
        boolean alarmEnable = StringUtils.isBlank(alarmEnableStr) || Boolean.parseBoolean(alarmEnableStr);
        // 告警类型
        String alarmType = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.type"));
        // 告警短信号码
        String alarmSmsPhoneNumbers = StringUtils
                .trimToNull(properties.getProperty("monitoring.alarm.sms.phone-numbers"));
        // 告警短信地址
        String alarmSmsAddress = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.address"));
        // 告警短信协议
        String alarmSmsProtocol = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.protocol"));
        // 告警短信接口商家
        String alarmSmsEnterprise = StringUtils.trimToNull(properties.getProperty("monitoring.alarm.sms.enterprise"));
        // 缺省[是否监控网络(默认打开)]
        String monitoringEnableStr = StringUtils.trimToNull(properties.getProperty("monitoring.network.enable"));
        boolean monitoringEnable = StringUtils.isBlank(monitoringEnableStr) || Boolean.parseBoolean(monitoringEnableStr);
        return wrap(threshold, alarmEnable, alarmType, alarmSmsPhoneNumbers, alarmSmsAddress, alarmSmsProtocol,
                alarmSmsEnterprise, monitoringEnable);
    }

    /**
     * <p>
     * 封装属性对象
     * </p>
     *
     * @param threshold            监控阈值
     * @param alarmEnable          告警是否打开
     * @param alarmType            告警类型
     * @param alarmSmsPhoneNumbers 告警短信号码
     * @param alarmSmsAddress      告警短信地址
     * @param alarmSmsProtocol     告警短信协议
     * @param alarmSmsEnterprise   告警短信接口商家
     * @param monitoringEnable     网络监控是否打开
     * @return MonitoringServerWebProperties
     * @author 皮锋
     * @custom.date 2020年3月10日 下午2:39:38
     */
    private MonitoringServerWebProperties wrap(int threshold, boolean alarmEnable, String alarmType,
                                               String alarmSmsPhoneNumbers, String alarmSmsAddress,
                                               String alarmSmsProtocol, String alarmSmsEnterprise,
                                               boolean monitoringEnable) {
        // 告警属性
        MonitoringAlarmProperties alarmProperties = new MonitoringAlarmProperties();
        alarmProperties.setEnable(alarmEnable);
        alarmProperties.setType(alarmType);
        // 短信属性
        MonitoringSmsProperties smsProperties = new MonitoringSmsProperties();
        smsProperties.setAddress(alarmSmsAddress);
        smsProperties.setEnterprise(alarmSmsEnterprise);
        smsProperties.setPhoneNumbers(alarmSmsPhoneNumbers != null ? alarmSmsPhoneNumbers.split(";") : null);
        smsProperties.setProtocol(alarmSmsProtocol);
        alarmProperties.setSmsProperties(smsProperties);
        // 网络属性
        MonitoringNetworkProperties networkProperties = new MonitoringNetworkProperties();
        networkProperties.setMonitoringEnable(monitoringEnable);
        // 所有监控属性
        MonitoringServerWebProperties monitoringServerWebProperties = new MonitoringServerWebProperties();
        monitoringServerWebProperties.setAlarmProperties(alarmProperties);
        monitoringServerWebProperties.setThreshold(threshold);
        monitoringServerWebProperties.setNetworkProperties(networkProperties);
        log.info("开发环境监控配置加载成功！");
        return monitoringServerWebProperties;
    }
}
