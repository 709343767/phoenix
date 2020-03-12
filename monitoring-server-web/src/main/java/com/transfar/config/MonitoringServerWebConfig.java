package com.transfar.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.transfar.property.MonitoringAlarmProperties;
import com.transfar.property.MonitoringServerWebProperties;
import com.transfar.property.MonitoringSmsProperties;
import com.transfar.util.PropertiesUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 监控服务端配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:13:47
 */
@Configuration
@Slf4j
public class MonitoringServerWebConfig {

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
        Properties properties = PropertiesUtils.loadProperties("monitoring.properties");
        // 监控阈值(默认值为5)
        String thresholdStr = StringUtils.trimToNull(properties.getProperty("monitoring.threshold"));
        int threshold = StringUtils.isBlank(thresholdStr) ? 5 : Integer.parseInt(thresholdStr);
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
        return wrap(threshold, alarmType, alarmSmsPhoneNumbers, alarmSmsAddress, alarmSmsProtocol, alarmSmsEnterprise);
    }

    /**
     * <p>
     * 封装属性对象
     * </p>
     *
     * @param threshold            监控阈值
     * @param alarmType            告警类型
     * @param alarmSmsPhoneNumbers 告警短信号码
     * @param alarmSmsAddress      告警短信地址
     * @param alarmSmsProtocol     告警短信协议
     * @param alarmSmsEnterprise   告警短信接口商家
     * @return MonitoringServerWebProperties
     * @author 皮锋
     * @custom.date 2020年3月10日 下午2:39:38
     */
    private MonitoringServerWebProperties wrap(int threshold, String alarmType, String alarmSmsPhoneNumbers, String alarmSmsAddress,
                                               String alarmSmsProtocol, String alarmSmsEnterprise) {
        // 告警属性
        MonitoringAlarmProperties alarmProperties = new MonitoringAlarmProperties();
        alarmProperties.setType(alarmType);
        // 短信属性
        MonitoringSmsProperties smsProperties = new MonitoringSmsProperties();
        smsProperties.setAddress(alarmSmsAddress);
        smsProperties.setEnterprise(alarmSmsEnterprise);
        smsProperties.setPhoneNumbers(alarmSmsPhoneNumbers != null ? alarmSmsPhoneNumbers.split(",") : null);
        smsProperties.setProtocol(alarmSmsProtocol);
        alarmProperties.setSmsProperties(smsProperties);
        // 所有监控属性
        MonitoringServerWebProperties monitoringServerWebProperties = new MonitoringServerWebProperties();
        monitoringServerWebProperties.setAlarmProperties(alarmProperties);
        monitoringServerWebProperties.setThreshold(threshold);
        log.info("监控配置加载成功！");
        return monitoringServerWebProperties;
    }

}
