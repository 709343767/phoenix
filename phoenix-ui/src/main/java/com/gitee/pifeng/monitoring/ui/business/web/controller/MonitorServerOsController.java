package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerOsService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerOsVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 服务器操作系统
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-21
 */
@Tag(name = "服务器.服务器操作系统")
@Controller
@RequestMapping("/monitor-server-os")
public class MonitorServerOsController {

    /**
     * 服务器操作系统服务类
     */
    @Autowired
    private IMonitorServerOsService monitorServerOsService;

    /**
     * <p>
     * 获取服务器操作系统信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/26 20:24
     */
    @Operation(summary = "获取服务器操作系统信息")
    @ResponseBody
    @GetMapping("/get-server-os-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getMonitorServerOsInfo(@RequestParam(name = "ip") String ip) {
        MonitorServerOsVo monitorServerOsVo = this.monitorServerOsService.getMonitorServerOsInfo(ip);
        return LayUiAdminResultVo.ok(monitorServerOsVo);
    }

}

