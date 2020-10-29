package com.imby.server.business.server.controller;

import com.alibaba.fastjson.JSON;
import com.imby.common.domain.Result;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.JvmPackage;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.service.IJvmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Java虚拟机信息包控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:29
 */
@RestController
@RequestMapping("/jvm")
@Api(tags = "信息包.Java虚拟机信息包")
public class JvmController {

    /**
     * java虚拟机信息服务层接口
     */
    @Autowired
    private IJvmService jvmService;

    /**
     * <p>
     * 监控服务端程序接收监控代理端程序或者监控客户端程序发的Java虚拟机信息包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:00:54
     */
    @ApiOperation(value = "接收和响应监控代理端程序或者监控客户端程序发的Java虚拟机信息包", notes = "接收Java虚拟机信息包")
    @PostMapping("/accept-jvm-package")
    public BaseResponsePackage acceptJvmPackage(@RequestBody String request) {
        JvmPackage jvmPackage = JSON.parseObject(request, JvmPackage.class);
        Result result = this.jvmService.dealJvmPackage(jvmPackage);
        return new PackageConstructor().structureBaseResponsePackage(result);
    }
}
