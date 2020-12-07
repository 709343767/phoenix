package com.gitee.pifeng.server.business.server.controller;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.dto.BaseResponsePackage;
import com.gitee.pifeng.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.server.business.server.service.IAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * <p>
 * 告警包控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:46:11
 */
@Slf4j
@RestController
@RequestMapping("/alarm")
@Api(tags = "信息包.告警包")
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
        log.debug("收到告警包：{}", request);
        try {
            AlarmPackage alarmPackage = JSON.parseObject(request, AlarmPackage.class);
            Future<Result> resultFuture = this.alarmService.dealAlarmPackage(alarmPackage);
            Result result = resultFuture.get();
            return new PackageConstructor().structureBaseResponsePackage(result);
        } catch (Exception e) {
            log.error("处理告警包异常！", e);
            return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(false).msg(e.getMessage()).build());
        }
    }

}
