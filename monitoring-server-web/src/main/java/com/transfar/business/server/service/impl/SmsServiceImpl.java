package com.transfar.business.server.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.transfar.business.server.domain.TransfarSms;
import com.transfar.business.server.service.ISmsService;
import com.transfar.property.MonitoringServerWebProperties;

/**
 * <p>
 * 短信服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午7:57:47
 */
@Service
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
	 * @author 皮锋
	 * @custom.date 2020年3月10日 上午11:01:47
	 * @param sms 创发短信实体对象
	 * @return 短信发送结果
	 */
	@Override
	public String sendSmsByTransfarApi(TransfarSms sms) {
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Context-type", "text/html;charset=utf-8");
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		HttpEntity<TransfarSms> entity = new HttpEntity<>(sms, headers);
		String alarmSmsAddress = this.config.getAlarmProperties().getSmsProperties().getAddress();
		ResponseEntity<String> responseEntity = this.restTemplate
				.exchange(
						alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content="
								+ sms.getContent() + "&identity=" + sms.getIdentity(),
						HttpMethod.GET, entity, String.class);
		return responseEntity.getBody();
	}

}
