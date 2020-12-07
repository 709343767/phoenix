package com.gitee.pifeng.server.business.server.service.impl;

import com.gitee.pifeng.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.server.business.server.domain.TransfarSms;
import com.gitee.pifeng.server.business.server.service.ISmsService;
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
     * 调用创发的短信接口发送短信
     * </p>
     *
     * @param sms 创发短信实体对象
     * @return 短信发送结果
     * @author 皮锋
     * @custom.date 2020年3月10日 上午11:01:47
     */
    @Override
    public boolean sendSmsByTransfarApi(TransfarSms sms) {
        String alarmSmsAddress = MonitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getAddress();
        // URL地址
        String url = alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content="
                + sms.getContent() + "&identity=" + sms.getIdentity();
        log.info("创发的短信接口URL：{}", url);
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
            String body = responseEntity.getBody();
            // 短信发送成功
            return StringUtils.isNotBlank(body);
        } catch (Exception e) {
            log.error("调用创发的短信接口发送短信异常！", e);
            return false;
        }
    }

}
