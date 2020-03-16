package com.transfar.agent.business.plug.controller;

import com.alibaba.fastjson.JSON;
import com.transfar.agent.business.plug.service.IAlarmService;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;
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
 * 告警控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午2:58:09
 */
@Api(tags = "告警")
@RestController
@RequestMapping("/alarm")
@Slf4j
public class AlarmController {

    /**
     * 告警服务层接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * <p>
     * 监控代理程序接收监控客户端程序发的告警包，并返回结果
     * </p>
     *
     * @param request 请求参数
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:00:54
     */
    @ApiOperation(value = "监控代理程序接收监控客户端程序发的告警包，并返回结果", notes = "接收告警包")
    @PostMapping("/accept-alarm-package")
    public BaseResponsePackage acceptAlarmPackage(@RequestBody String request) {
        AlarmPackage heartbeatPackage = JSON.parseObject(request, AlarmPackage.class);
        log.info("代理端收到的告警包：{}", heartbeatPackage.toJsonString());
        return this.alarmService.dealAlarmPackage(heartbeatPackage);
    }

}
