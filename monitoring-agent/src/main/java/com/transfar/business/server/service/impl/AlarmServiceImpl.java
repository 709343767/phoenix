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

import com.transfar.business.constant.Urlconstants;
import com.transfar.business.server.service.IAlarmService;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.BaseResponsePackage;

/**
 * <p>
 * 跟服务端相关的告警服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:30:01
 */
@Service
public class AlarmServiceImpl implements IAlarmService {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * <p>
	 * 给服务端发告警包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午3:27:17
	 * @param alarmPackage 告警包
	 * @return BaseResponsePackage
	 */
	@Override
	public BaseResponsePackage sendAlarmPackage(AlarmPackage alarmPackage) {
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Context-type", "text/html;charset=utf-8");
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<AlarmPackage> entity = new HttpEntity<>(alarmPackage, headers);
		ResponseEntity<BaseResponsePackage> responseEntity = this.restTemplate.exchange(Urlconstants.ALARM_URL,
				HttpMethod.POST, entity, BaseResponsePackage.class);
		return responseEntity.getBody();
	}

}
