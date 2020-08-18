package com.imby.server.business.server.controller;

import com.alibaba.fastjson.JSON;
import com.imby.common.domain.Result;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.service.IAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class AlarmController {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * <p>
     * 监控服务端程序接收监控代理端程序或者监控客户端程序发的告警包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:49:45
     */
    @ApiOperation(value = "接收和响应监控代理端程序或者监控客户端程序发的告警包", notes = "接收告警包")
    @PostMapping("/accept-alarm-package")
    public BaseResponsePackage acceptAlarmPackage(@RequestBody String request) {
        AlarmPackage alarmPackage = JSON.parseObject(request, AlarmPackage.class);
        Result result = this.alarmService.dealAlarmPackage(alarmPackage);
        return new PackageConstructor().structureBaseResponsePackage(result);
    }
}
