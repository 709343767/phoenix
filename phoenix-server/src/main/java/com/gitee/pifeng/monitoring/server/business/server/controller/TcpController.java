package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpService;
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
 * TCP信息控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:29
 */
@Slf4j
@RestController
@RequestMapping("/tcp")
@Api(tags = "TCP信息")
public class TcpController {

    /**
     * TCP信息服务接口
     */
    @Autowired
    private ITcpService tcpService;

    /**
     * <p>
     * 测试TCP连通性
     * </p>
     *
     * @return {@link BaseResponsePackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    @ApiOperation(value = "测试TCP连通性", notes = "测试TCP连通性")
    @PostMapping("/test-monitor-tcp")
    public BaseResponsePackage testMonitorTcp(@RequestBody BaseRequestPackage baseRequestPackage) throws NetException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String hostnameTarget = extraMsg.getString("hostnameTarget");
        Integer portTarget = extraMsg.getInteger("portTarget");
        Boolean isConnected = this.tcpService.testMonitorTcp(hostnameTarget, portTarget);
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(String.valueOf(isConnected)).build());
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        if (timer.intervalSecond() > 1) {
            log.warn("测试TCP连通性耗时：{}", betweenDay);
        }
        return baseResponsePackage;
    }

}
