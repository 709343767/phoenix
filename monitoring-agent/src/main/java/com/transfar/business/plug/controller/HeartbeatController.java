package com.transfar.business.plug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transfar.business.plug.service.IHeartbeatService;
import com.transfar.dto.HeartbeatDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
     * @param heartbeatDto 心跳对象
     * @return HeartbeatDto
     * @author 皮锋
     * @custom.date 2020年3月4日 下午12:27:47
     */
    @ApiOperation(value = "监控代理程序接收监控客户端程序发的心跳包，并返回结果", notes = "接收心跳包")
    @PostMapping("/accept-heartbeat-package")
    public HeartbeatDto acceptHeartbeatPackage(HeartbeatDto heartbeatDto) {
        return this.heartbeatService.dealHeartbeatPackage(heartbeatDto);
    }

}
