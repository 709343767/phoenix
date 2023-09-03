package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerCpuService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerCpuVo;
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
 * 服务器CPU
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Tag(name = "服务器.服务器CPU")
@RestController
@RequestMapping("/monitor-server-cpu")
public class MonitorServerCpuController {

    /**
     * 服务器CPU服务类
     */
    @Autowired
    private IMonitorServerCpuService monitorServerCpuService;

    /**
     * <p>
     * 获取服务器详情页面服务器CPU信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/11/1 14:34
     */
    @Operation(summary = "获取服务器详情页面服务器CPU信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-cpu-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getServerDetailPageServerCpuInfo(@RequestParam(name = "ip") String ip) {
        List<MonitorServerCpuVo> monitorServerCpuVos = this.monitorServerCpuService.getServerDetailPageServerCpuInfo(ip);
        return LayUiAdminResultVo.ok(monitorServerCpuVos);
    }

}

