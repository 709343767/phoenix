package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerNetcardService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerNetcardVo;
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
 * 服务器网卡
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Tag(name = "服务器.服务器网卡")
@RestController
@RequestMapping("/monitor-server-netcard")
public class MonitorServerNetcardController {

    /**
     * 服务器网卡服务类
     */
    @Autowired
    private IMonitorServerNetcardService monitorServerNetcardService;

    /**
     * <p>
     * 获取服务器详情页面服务器网卡信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/26 20:24
     */
    @Operation(summary = "获取服务器详情页面服务器网卡信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-netcard-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getServerDetailPageServerNetcardInfo(@RequestParam(name = "ip") String ip) {
        List<ServerDetailPageServerNetcardVo> serverDetailPageServerNetcardVos = this.monitorServerNetcardService.getServerDetailPageServerNetcardInfo(ip);
        return LayUiAdminResultVo.ok(serverDetailPageServerNetcardVos);
    }

}

