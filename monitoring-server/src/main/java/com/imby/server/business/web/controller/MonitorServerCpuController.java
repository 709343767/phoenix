package com.imby.server.business.web.controller;


import com.imby.server.business.web.service.IMonitorServerCpuService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.ServerDetailPageServerCpuVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
     * @param ip   服务器IP地址
     * @param time 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/19 14:20
     */
    @ApiOperation(value = "获取服务器详情页面服务器CPU信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-cpu-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getServerDetailPageServerCpu(@RequestParam(name = "ip") String ip,
                                                           @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerCpuVo> monitorJvmMemoryVos = this.monitorServerCpuService.getServerDetailPageServerCpu(ip, time);
        return LayUiAdminResultVo.ok(monitorJvmMemoryVos);
    }

}

