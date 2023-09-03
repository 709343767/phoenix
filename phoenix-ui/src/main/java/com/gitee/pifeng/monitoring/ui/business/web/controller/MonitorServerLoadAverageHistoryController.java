package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerLoadAverageHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerLoadAverageChartVo;
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
 * 服务器平均负载历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-06-17
 */
@Tag(name = "服务器.服务器平均负载历史记录")
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
    @Operation(summary = "获取服务器详情页面服务器平均负载图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-load-average-chart-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getServerDetailPageServerLoadAverageChartInfo(@RequestParam(name = "ip") String ip,
                                                                            @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerLoadAverageChartVo> monitorServerLoadAverageChartVos = this.monitorServerLoadAverageHistoryService.getServerDetailPageServerLoadAverageChartInfo(ip, time);
        return LayUiAdminResultVo.ok(monitorServerLoadAverageChartVos);
    }

}

