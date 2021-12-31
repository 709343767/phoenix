package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
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
     * @param alarmPackage 告警包
     * @return {@link BaseResponsePackage}
     * @throws ExecutionException   执行异常
     * @throws InterruptedException 中断异常
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:49:45
     */
    @ApiOperation(value = "接收和响应监控代理端程序或者监控客户端程序发的告警包", notes = "接收告警包")
    @PostMapping("/accept-alarm-package")
    public BaseResponsePackage acceptAlarmPackage(@RequestBody AlarmPackage alarmPackage) throws ExecutionException, InterruptedException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        Future<Result> resultFuture = this.alarmService.dealAlarmPackage(alarmPackage);
        // 返回值
        Result result = resultFuture.get();
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(result);
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        if (timer.intervalSecond() > 1) {
            log.warn("处理告警包耗时：{}", betweenDay);
        }
        return baseResponsePackage;
    }

}
