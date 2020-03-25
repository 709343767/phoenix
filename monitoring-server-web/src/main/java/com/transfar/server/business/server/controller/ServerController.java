package com.transfar.server.business.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.ServerPackage;
import com.transfar.server.business.server.core.PackageConstructor;
import com.transfar.server.business.server.service.IServerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
public class ServerController {

    /**
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 监控服务端程序接收监控代理程序或者监控客户端程序发的服务器信息包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:00:54
     */
    @ApiOperation(value = "监控服务端程序接收监控代理程序或者监控客户端程序发的服务器信息包，并返回结果", notes = "接收服务器信息包")
    @PostMapping("/accept-server-package")
    public BaseResponsePackage acceptServerPackage(@RequestBody String request) {
        ServerPackage serverPackage = JSON.parseObject(request, ServerPackage.class);
        boolean result = this.serverService.dealServerPackage(serverPackage);
        if (result) {
            // 成功
            return new PackageConstructor().structureBaseResponsePackageBySuccess();
        } else {
            // 失败
            return new PackageConstructor().structureBaseResponsePackageByFail();
        }
    }

}
