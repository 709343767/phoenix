package com.transfar.server.business.server.service.impl;

import com.transfar.server.business.server.domain.TransfarSms;
import com.transfar.server.business.server.service.ISmsService;
import com.transfar.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

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
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Context-type", "text/html;charset=utf-8");
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<TransfarSms> entity = new HttpEntity<>(sms, headers);
        String alarmSmsAddress = this.config.getAlarmProperties().getSmsProperties().getAddress();
        // URL地址
        String url = alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content="
                + sms.getContent() + "&identity=" + sms.getIdentity();
        log.info("创发的短信接口URL：{}", url);
        ResponseEntity<String> responseEntity = this.restTemplate
                .exchange(url, HttpMethod.GET, entity, String.class);
        return responseEntity.getBody();
    }

}
