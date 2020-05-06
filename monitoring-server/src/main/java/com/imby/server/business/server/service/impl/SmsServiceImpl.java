package com.imby.server.business.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.imby.server.business.server.domain.TransfarSms;
import com.imby.server.business.server.service.ISmsService;
import com.imby.server.property.MonitoringServerWebProperties;

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
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties config;

    /**
     * <p>
     * 调用创发的短信接口发送短信
     * </p>
     *
     * @param sms 创发短信实体对象
     * @return 短信发送结果
     * @author 皮锋
     * @custom.date 2020年3月10日 上午11:01:47
     */
    @Override
    public String sendSmsByTransfarApi(TransfarSms sms) {
        String alarmSmsAddress = this.config.getAlarmProperties().getSmsProperties().getAddress();
        // URL地址
        String url = alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content="
                + sms.getContent() + "&identity=" + sms.getIdentity();
        log.info("创发的短信接口URL：{}", url);
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("调用创发的短信接口发送短信异常！", e);
            return "null";
        }
    }

}
