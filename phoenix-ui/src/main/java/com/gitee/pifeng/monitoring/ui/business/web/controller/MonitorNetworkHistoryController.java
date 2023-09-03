package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.NetworkAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网络信息历史记录前端控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Controller
@RequestMapping("/monitor-network-history")
@Tag(name = "网络.网络历史记录")
public class MonitorNetworkHistoryController {

    /**
     * 网络信息历史记录服务类
     */
    @Autowired
    private IMonitorNetHistoryService monitorNetHistoryService;

    /**
     * <p>
     * 获取Ping耗时图表信息
     * </p>
     *
     * @param id        网络ID
     * @param ipSource  IP地址（来源）
     * @param ipTarget  IP地址（目的地）
     * @param dateValue 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    @Operation(summary = "获取Ping耗时图表信息")
    @ResponseBody
    @GetMapping("/get-avg-time-chart-info")
    @Parameters(value = {
            @Parameter(name = "id", description = "网络ID", in = ParameterIn.QUERY),
            @Parameter(name = "ipSource", description = "IP地址（来源）", in = ParameterIn.QUERY),
            @Parameter(name = "ipTarget", description = "IP地址（目的地）", in = ParameterIn.QUERY),
            @Parameter(name = "dateValue", description = "时间", in = ParameterIn.QUERY)})
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.QUERY, operDesc = "获取Ping耗时图表信息")
    public LayUiAdminResultVo getAvgTimeChartInfo(@RequestParam(name = "id") Long id,
                                                  @RequestParam(name = "ipSource") String ipSource,
                                                  @RequestParam(name = "ipTarget") String ipTarget,
                                                  @RequestParam(name = "dateValue") String dateValue) {
        NetworkAvgTimeChartVo networkAvgTimeChartVo = this.monitorNetHistoryService
                .getAvgTimeChartInfo(id, ipSource, ipTarget, dateValue);
        return LayUiAdminResultVo.ok(networkAvgTimeChartVo);
    }

    /**
     * <p>
     * 清理网络监控历史数据
     * </p>
     *
     * @param id   网络ID
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/20 20:52
     */
    @Operation(summary = "清理网络监控历史数据")
    @Parameters(value = {
            @Parameter(name = "id", description = "网络ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", required = true, in = ParameterIn.QUERY)})
    @DeleteMapping("/clear-monitor-network-history")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.DELETE, operDesc = "清理网络监控历史数据")
    public LayUiAdminResultVo clearMonitorNetworkHistory(Long id, String time) {
        return this.monitorNetHistoryService.clearMonitorNetworkHistory(id, time);
    }

}

