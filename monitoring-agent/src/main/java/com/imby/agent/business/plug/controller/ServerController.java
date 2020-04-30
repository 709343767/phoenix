package com.imby.agent.business.plug.controller;

import com.alibaba.fastjson.JSON;
import com.imby.agent.business.plug.service.IServerService;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.ServerPackage;

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
     * 服务器信息服务接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 监控代理程序接收监控客户端程序发的服务器信息包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:00:54
     */
    @ApiOperation(value = "接收和响应监控客户端程序发的服务器信息包", notes = "接收服务器信息包")
    @PostMapping("/accept-server-package")
    public BaseResponsePackage acceptServerPackage(@RequestBody String request) {
        ServerPackage serverPackage = JSON.parseObject(request, ServerPackage.class);
        log.info("代理端收到的服务器信息包：{}", serverPackage.toJsonString());
        return this.serverService.dealServerPackage(serverPackage);
    }
}
