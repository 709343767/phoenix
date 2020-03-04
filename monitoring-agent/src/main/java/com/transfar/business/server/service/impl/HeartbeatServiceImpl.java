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

import com.transfar.business.server.service.IHeartbeatService;
import com.transfar.dto.HeartbeatDto;

/**
 * <p>
 * 跟服务端相关的心跳服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:13:59
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

	/**
	 * 接收心跳的包映射地址
	 */
	private static final String MAPPING_URL = "/heartbeat/accept-heartbeat-package";

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * <p>
	 * 给服务端发心跳包
	 * </p>
	 *
	 * @param heartbeatDto 心跳对象
	 * @return HeartbeatDto
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午2:16:07
	 */
	@Override
	public HeartbeatDto sendHeartbeatPackage(HeartbeatDto heartbeatDto) {
		// String url = ConfigAnalysis.getServerUrl() + MAPPING_URL;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<HeartbeatDto> entity = new HttpEntity<>(heartbeatDto, headers);
		ResponseEntity<HeartbeatDto> responseEntity = this.restTemplate.exchange(MAPPING_URL, HttpMethod.POST, entity,
				HeartbeatDto.class);
		return responseEntity.getBody();
	}

}
