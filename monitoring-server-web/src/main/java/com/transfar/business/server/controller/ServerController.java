package com.transfar.business.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.transfar.dto.ServerPackage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务器信息控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@RestController
@RequestMapping("/server")
@Api(tags = "服务器信息")
@Slf4j
public class ServerController {

	/**
	 * <p>
	 * 监控服务端程序接收监控代理程序发的服务器信息包，并返回结果
	 * </p>
	 * 
	 * @param request 请求参数
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午3:00:54
	 * @return Boolean
	 */
	@ApiOperation(value = "监控服务端程序接收监控代理程序发的服务器信息包，并返回结果", notes = "接收服务器信息包")
	@PostMapping("/accept-server-package")
	public Boolean acceptServerPackage(@RequestBody(required = true) String request) {
		ServerPackage serverPackage = JSON.parseObject(request, ServerPackage.class);
		log.info(serverPackage.toJsonString());
		return true;
	}
}
