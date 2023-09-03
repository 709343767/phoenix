package com.gitee.pifeng.monitoring.server.business.server.controller;

import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.dto.OfflinePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
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
 * 下线信息包控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/6/1 8:33
 */
@Slf4j
@RestController
@RequestMapping("/offline")
@Tag(name = "信息包.下线信息包")
public class OfflineController {

    /**
     * 服务端包构造器
     */
    @Autowired
    private ServerPackageConstructor serverPackageConstructor;

    /**
     * <p>
     * 监控服务端程序接收监控代理端程序或者监控客户端程序发的下线信息包，并返回结果
     * </p>
     *
     * @param offlinePackage 下线信息包
     * @return {@link BaseResponsePackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2023年6月1日 上午8:01:20
     */
    @Operation(description = "接收和响应监控代理端程序或者监控客户端程序发的下线信息包", summary = "接收下线信息包",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CiphertextPackage.class))),
            responses = @ApiResponse(content = {@Content(schema = @Schema(implementation = CiphertextPackage.class))}))
    @PostMapping("/accept-offline-package")
    public BaseResponsePackage acceptOfflinePackage(@RequestBody OfflinePackage offlinePackage) throws NetException {
        if (log.isDebugEnabled()) {
            log.debug("下线信息包：{}", offlinePackage.toJsonString());
        }
        return this.serverPackageConstructor.structureBaseResponsePackage(Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build());
    }

}
