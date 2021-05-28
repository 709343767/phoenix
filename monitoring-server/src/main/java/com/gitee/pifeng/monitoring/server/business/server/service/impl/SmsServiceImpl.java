package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.domain.KacperSms;
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
        if (EnterpriseEnums.KACPER == enterpriseEnum) {
            // 调用Kacper短信接口
            return this.callKacperSmsApi(sms);
        }
        return false;
    }

    /**
     * <p>
     * 封装数据，调用Kacper公司的短信接口发送短信
     * </p>
     *
     * @param sms 短信实体对象
     * @return boolean
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:19:36
     */
    private boolean callKacperSmsApi(Sms sms) {
        String enterprise = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getEnterpriseEnum().name();
        // 短信不支持<br>标签换行
        String text = sms.getContent().replace("<br>", "");
        String title = sms.getTitle();
        KacperSms kacperSms = KacperSms.builder()
                .content(StringUtils.isBlank(title) ? text : ("[" + title + "]" + text))
                .type(sms.getLevel())
                .phone(StringUtils.join(sms.getPhones(), ";"))
                .identity(enterprise)
                .build();
        // Kacper公司短信接口
        return this.sendSmsByKacperApi(kacperSms);
    }

    /**
     * <p>
     * 调用Kacper的短信接口发送短信
     * </p>
     *
     * @param sms Kacper短信实体对象
     * @return 短信发送结果
     * @author 皮锋
     * @custom.date 2020年3月10日 上午11:01:47
     */
    private boolean sendSmsByKacperApi(KacperSms sms) {
        String alarmSmsAddress = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getAddress();
        // URL地址
        String url = alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content="
                + sms.getContent() + "&identity=" + sms.getIdentity();
        log.info("Kacper的短信接口URL：{}", url);
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
            String body = responseEntity.getBody();
            // 短信发送成功
            return StringUtils.isNotBlank(body);
        } catch (Exception e) {
            log.error("调用Kacper的短信接口发送短信异常！", e);
            return false;
        }
    }

}
