package com.transfar.business.server.controller;

import java.util.Date;

import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transfar.business.server.dto.ServerResponseHeartbeatPackage;
import com.transfar.dto.HeartbeatPackage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 心跳控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午12:16:06
 */
@RestController
@RequestMapping("/heartbeat")
@Api(tags = "心跳")
@Slf4j
public class HeartbeatController {

	/**
	 * <p>
	 * 监控服务端程序接收监控代理程序发的心跳包，并返回结果
	 * </p>
	 *
	 * @param requestEntity 请求实体对象
	 * @return HeartbeatPackage
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午12:27:47
	 */
	@ApiOperation(value = "监控服务端程序接收监控代理程序发的心跳包，并返回结果", notes = "接收心跳包")
	@PostMapping("/accept-heartbeat-package")
	public HeartbeatPackage acceptHeartbeatPackage(RequestEntity<HeartbeatPackage> requestEntity) {
		HeartbeatPackage heartbeatPackage = requestEntity.getBody();
		log.info(heartbeatPackage.toJsonString());
		return new ServerResponseHeartbeatPackage()//
				.setDateTime(new Date())//
				.setResult(true);
	}

}
