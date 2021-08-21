package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.domain.MonitoringSms;
import com.gitee.pifeng.monitoring.server.business.server.domain.Sms;
import com.gitee.pifeng.monitoring.server.business.server.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 短信服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午7:57:47
 */
@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>
     * 发送告警模板短信
     * </p>
     *
     * @param sms 短信实体对象
     * @return boolean
     * @author 皮锋
     * @custom.date 2021/1/29 10:05
     */
    @Override
    public boolean sendAlarmTemplateSms(Sms sms) {
        // 短信接口商家
        EnterpriseEnums enterpriseEnum = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getEnterpriseEnum();
        // 判断短信接口商家，不同的商家调用不同的接口
        if (EnterpriseEnums.MONITORING == enterpriseEnum) {
            // 调用Monitoring短信接口
            return this.callMonitoringSmsApi(sms);
        }
        return false;
    }

    /**
     * <p>
     * 封装数据，调用Monitoring公司的短信接口发送短信
     * </p>
     *
     * @param sms 短信实体对象
     * @return boolean
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:19:36
     */
    private boolean callMonitoringSmsApi(Sms sms) {
        String enterprise = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getEnterpriseEnum().name();
        // 短信不支持<br>标签换行
        String text = sms.getContent().replace("<br>", "");
        String title = sms.getTitle();
        MonitoringSms monitoringSms = MonitoringSms.builder()
                .content(StringUtils.isBlank(title) ? text : ("[" + title + "]" + text))
                .type(sms.getLevel())
                .phone(StringUtils.join(sms.getPhones(), ";"))
                .identity(enterprise)
                .build();
        // Monitoring公司短信接口
        return this.sendSmsByMonitoringApi(monitoringSms);
    }

    /**
     * <p>
     * 调用Monitoring的短信接口发送短信
     * </p>
     *
     * @param sms Monitoring短信实体对象
     * @return 短信发送结果
     * @author 皮锋
     * @custom.date 2020年3月10日 上午11:01:47
     */
    private boolean sendSmsByMonitoringApi(MonitoringSms sms) {
        String alarmSmsAddress = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getAddress();
        // URL地址
        String url = alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content="
                + sms.getContent() + "&identity=" + sms.getIdentity();
        log.info("Monitoring的短信接口URL：{}", url);
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
            String body = responseEntity.getBody();
            // 短信发送成功
            return StringUtils.isNotBlank(body);
        } catch (Exception e) {
            log.error("调用Monitoring的短信接口发送短信异常！", e);
            return false;
        }
    }

}
