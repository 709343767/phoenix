package com.imby.server.business.web.controller;


import com.imby.server.business.web.service.IMonitorServerMemoryService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.ServerDetailPageServerMemoryChartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务器内存
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Api(tags = "服务器内存")
@RestController
@RequestMapping("/monitor-server-memory")
public class MonitorServerMemoryController {

    /**
     * 服务器内存服务类
     */
    @Autowired
    private IMonitorServerMemoryService monitorServerMemoryService;

    /**
     * <p>
     * 获取服务器详情页面服务器内存图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/21 12:35
     */
    @ApiOperation(value = "获取服务器详情页面服务器内存图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-memory-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getServerDetailPageServerMemoryChartInfo(@RequestParam(name = "ip") String ip,
                                                                       @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerMemoryChartVo> monitorJvmMemoryChartVos = this.monitorServerMemoryService.getServerDetailPageServerMemoryChartInfo(ip, time);
        return LayUiAdminResultVo.ok(monitorJvmMemoryChartVos);
    }

}

