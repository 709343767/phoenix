package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerLoadAverageHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerLoadAverageChartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务器平均负载历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
@Api(tags = "服务器.服务器平均负载历史记录")
@RestController
@RequestMapping("/monitor-server-load-average-history")
public class MonitorServerLoadAverageHistoryController {

    /**
     * 服务器平均负载历史记录服务类
     */
    @Autowired
    private IMonitorServerLoadAverageHistoryService monitorServerLoadAverageHistoryService;

    /**
     * <p>
     * 获取服务器详情页面服务器平均负载图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/19 14:20
     */
    @ApiOperation(value = "获取服务器详情页面服务器平均负载图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-load-average-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getServerDetailPageServerLoadAverageChartInfo(@RequestParam(name = "ip") String ip,
                                                                            @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerLoadAverageChartVo> monitorServerLoadAverageChartVos = this.monitorServerLoadAverageHistoryService.getServerDetailPageServerLoadAverageChartInfo(ip, time);
        return LayUiAdminResultVo.ok(monitorServerLoadAverageChartVos);
    }

}

