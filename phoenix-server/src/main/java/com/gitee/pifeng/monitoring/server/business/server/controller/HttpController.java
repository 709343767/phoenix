package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpService;
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
 * HTTP信息控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:29
 */
@Slf4j
@RestController
@RequestMapping("/http")
@Api(tags = "HTTP信息")
public class HttpController {

    /**
     * HTTP信息服务接口
     */
    @Autowired
    private IHttpService httpService;

    /**
     * <p>
     * 测试HTTP连通性
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    @ApiOperation(value = "测试HTTP连通性", notes = "测试HTTP连通性")
    @PostMapping("/test-monitor-http")
    public BaseResponsePackage testMonitorHttp(@RequestBody BaseRequestPackage baseRequestPackage) throws NetException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String method = extraMsg.getString("method");
        String urlTarget = extraMsg.getString("urlTarget");
        String parameter = extraMsg.getString("parameter");
        String httpCode = this.httpService.testMonitorHttp(method, urlTarget, parameter);
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(httpCode).build());
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        if (timer.intervalSecond() > 1) {
            log.warn("测试HTTP连通性耗时：{}", betweenDay);
        }
        return baseResponsePackage;
    }

}
