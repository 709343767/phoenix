package com.imby.agent.business.client.controller;

import com.alibaba.fastjson.JSON;
import com.imby.agent.business.client.service.IHeartbeatService;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.HeartbeatPackage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = "信息包.心跳包")
@Slf4j
public class HeartbeatController {

    /**
     * 心跳服务接口
     */
    @Autowired
    private IHeartbeatService heartbeatService;

    /**
     * <p>
     * 监控代理程序接收监控客户端程序发的心跳包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午12:27:47
     */
    @ApiOperation(value = "接收和响应监控客户端程序发的心跳包", notes = "接收心跳包")
    @PostMapping("/accept-heartbeat-package")
    public BaseResponsePackage acceptHeartbeatPackage(@RequestBody String request) {
        HeartbeatPackage heartbeatPackage = JSON.parseObject(request, HeartbeatPackage.class);
        log.info("代理端收到的心跳包：{}", heartbeatPackage.toJsonString());
        return this.heartbeatService.dealHeartbeatPackage(heartbeatPackage);
    }

}
