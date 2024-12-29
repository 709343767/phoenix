package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "HTTP信息")
public class HttpController {

    /**
     * 服务端包构造器
     */
    @Autowired
    private ServerPackageConstructor serverPackageConstructor;

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
    @Operation(summary = "测试HTTP连通性", description = "测试HTTP连通性",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CiphertextPackage.class))),
            responses = @ApiResponse(content = {@Content(schema = @Schema(implementation = CiphertextPackage.class))}))
    @PostMapping("/test-monitor-http")
    public BaseResponsePackage testMonitorHttp(@RequestBody BaseRequestPackage baseRequestPackage) throws NetException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String method = extraMsg.getString("method");
        String urlTarget = extraMsg.getString("urlTarget");
        String contentType = extraMsg.getString("contentType");
        String headerParameter = extraMsg.getString("headerParameter");
        String bodyParameter = extraMsg.getString("bodyParameter");
        String httpCode = this.httpService.testMonitorHttp(method, urlTarget, contentType, headerParameter, bodyParameter);
        BaseResponsePackage baseResponsePackage = this.serverPackageConstructor.structureBaseResponsePackage(Result.builder().isSuccess(true).msg(httpCode).build());
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        if (timer.intervalSecond() > 1) {
            log.warn("测试HTTP连通性耗时：{}", betweenDay);
        }
        return baseResponsePackage;
    }

}
