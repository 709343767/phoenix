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
import com.transfar.business.server.service.IServerService;
import com.transfar.dto.ServerPackage;

/**
 * <p>
 * 跟服务端相关的服务器信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:27:43
 */
@Service
public class ServerServiceImpl implements IServerService {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * <p>
	 * 给服务端发服务器信息包
	 * </p>
	 *
	 * @author 皮锋
	 * @param serverPackage 服务器信息包
	 * @custom.date 2020年3月7日 下午5:24:47
	 * @return Boolean
	 */
	@Override
	public Boolean sendServerPackage(ServerPackage serverPackage) {
		HttpHeaders headers = new HttpHeaders();
		// headers.add("Context-type", "text/html;charset=utf-8");
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<ServerPackage> entity = new HttpEntity<>(serverPackage, headers);
		ResponseEntity<Boolean> responseEntity = this.restTemplate.exchange(Urlconstants.SERVER_URL, HttpMethod.POST,
				entity, Boolean.class);
		return responseEntity.getBody();
	}

}
