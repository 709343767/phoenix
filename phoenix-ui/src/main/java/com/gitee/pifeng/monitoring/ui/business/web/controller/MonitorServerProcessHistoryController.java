package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerProcessHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerProcessChartVo;
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
 * 服务器进程历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
@RestController
@Tag(name = "服务器.服务器进程历史记录")
@RequestMapping("/monitor-server-process-history")
public class MonitorServerProcessHistoryController {

    /**
     * 服务器进程历史记录服务类
     */
    @Autowired
    private IMonitorServerProcessHistoryService monitorServerProcessHistoryService;

    /**
     * <p>
     * 获取服务器详情页面服务器进程图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/9/18 12:56
     */
    @Operation(summary = "获取服务器详情页面服务器进程图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-process-chart-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getServerDetailPageServerProcessChartInfo(@RequestParam(name = "ip") String ip,
                                                                        @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerProcessChartVo> monitorServerProcessChartVos = this.monitorServerProcessHistoryService.getServerDetailPageServerProcessChartInfo(ip, time);
        return LayUiAdminResultVo.ok(monitorServerProcessChartVos);
    }

}

