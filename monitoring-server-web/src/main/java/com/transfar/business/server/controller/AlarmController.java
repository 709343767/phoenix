package com.transfar.business.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.transfar.dto.AlarmPackage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 告警控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:46:11
 */
@RestController
@RequestMapping("/alarm")
@Api(tags = "告警")
@Slf4j
public class AlarmController {

	/**
	 * <p>
	 * 监控服务端程序接收监控代理程序发的告警包，并返回结果
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午3:49:45
	 * @param request 请求参数
	 * @return Boolean
	 */
	@ApiOperation(value = "监控服务端程序接收监控代理程序发的告警包，并返回结果", notes = "接收告警包")
	@PostMapping("/accept-alarm-package")
	public Boolean acceptAlarmPackage(@RequestBody(required = true) String request) {
		AlarmPackage alarmPackage = JSON.parseObject(request, AlarmPackage.class);
		log.info(alarmPackage.toJsonString());
		return true;
	}
}
