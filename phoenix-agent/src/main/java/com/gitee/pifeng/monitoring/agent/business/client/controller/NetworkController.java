package com.gitee.pifeng.monitoring.agent.business.client.controller;

import com.gitee.pifeng.monitoring.agent.business.client.service.IBaseRequestPackageService;
import com.gitee.pifeng.monitoring.agent.constant.UrlConstants;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
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
 * 网络信息控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/6 21:56
 */
@Slf4j
@RestController
@RequestMapping("/network")
@Tag(name = "网络信息")
public class NetworkController {

    /**
     * 基础请求包服务接口
     */
    @Autowired
    private IBaseRequestPackageService baseRequestPackageService;

    /**
     * <p>
     * 获取被监控网络源IP地址
     * </p>
     *
     * @return {@link BaseResponsePackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/10/6 22:04
     */
    @Operation(summary = "获取被监控网络源IP地址", description = "获取被监控网络源IP地址",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CiphertextPackage.class))),
            responses = @ApiResponse(content = {@Content(schema = @Schema(implementation = CiphertextPackage.class))}))
    @PostMapping("/get-source-ip")
    public BaseResponsePackage getSourceIp(@RequestBody BaseRequestPackage baseRequestPackage) throws NetException {
        return this.baseRequestPackageService.dealBaseRequestPackage(baseRequestPackage, UrlConstants.GET_SOURCE_IP_URL);
    }

    /**
     * <p>
     * 测试网络连通性
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    @Operation(summary = "测试网络连通性", description = "测试网络连通性",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CiphertextPackage.class))),
            responses = @ApiResponse(content = {@Content(schema = @Schema(implementation = CiphertextPackage.class))}))
    @PostMapping("/test-monitor-network")
    public BaseResponsePackage testMonitorNetwork(@RequestBody BaseRequestPackage baseRequestPackage) throws NetException {
        return this.baseRequestPackageService.dealBaseRequestPackage(baseRequestPackage, UrlConstants.TEST_MONITOR_NETWORK_URL);
    }

}
