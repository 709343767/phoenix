package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerGpuService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerGpuVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务器GPU
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-06-09
 */
@Tag(name = "服务器.服务器GPU")
@RestController
@RequestMapping("/monitor-server-gpu")
public class MonitorServerGpuController {

    /**
     * 服务器GPU服务类
     */
    @Autowired
    private IMonitorServerGpuService monitorServerGpuService;

    /**
     * <p>
     * 获取服务器详情页面服务器GPU信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2025/6/16 11:36
     */
    @Operation(summary = "获取服务器详情页面服务器GPU信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-gpu-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getServerDetailPageServerGpuInfo(@RequestParam(name = "ip") String ip) {
        List<ServerDetailPageServerGpuVo> serverDetailPageServerGpuVos = this.monitorServerGpuService.getServerDetailPageServerGpuInfo(ip);
        return LayUiAdminResultVo.ok(serverDetailPageServerGpuVos);
    }

}

