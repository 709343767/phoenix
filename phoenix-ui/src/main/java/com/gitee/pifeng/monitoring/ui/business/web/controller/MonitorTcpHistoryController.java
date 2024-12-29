package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.TcpAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * TCP信息历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Controller
@RequestMapping("/monitor-tcp-history")
@Tag(name = "TCP.TCP历史记录")
public class MonitorTcpHistoryController {

    /**
     * TCP信息历史记录服务类
     */
    @Autowired
    private IMonitorTcpHistoryService monitorTcpHistoryService;

    /**
     * <p>
     * 获取Telnet耗时图表信息
     * </p>
     *
     * @param id             TCP ID
     * @param hostnameSource 主机名（来源）
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     端口号
     * @param dateValue      时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    @Operation(summary = "获取Telnet耗时图表信息")
    @ResponseBody
    @GetMapping("/get-avg-time-chart-info")
    @Parameters(value = {
            @Parameter(name = "id", description = "TCP ID", in = ParameterIn.QUERY),
            @Parameter(name = "hostnameSource", description = "主机名（来源）", in = ParameterIn.QUERY),
            @Parameter(name = "hostnameTarget", description = "主机名（目的地）", in = ParameterIn.QUERY),
            @Parameter(name = "portTarget", description = "端口号", in = ParameterIn.QUERY),
            @Parameter(name = "dateValue", description = "时间", in = ParameterIn.QUERY)})
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取Telnet耗时图表信息")
    public LayUiAdminResultVo getAvgTimeChartInfo(@RequestParam(name = "id") Long id,
                                                  @RequestParam(name = "hostnameSource") String hostnameSource,
                                                  @RequestParam(name = "hostnameTarget") String hostnameTarget,
                                                  @RequestParam(name = "portTarget") Integer portTarget,
                                                  @RequestParam(name = "dateValue") String dateValue) {
        TcpAvgTimeChartVo tcpAvgTimeChartVo = this.monitorTcpHistoryService
                .getAvgTimeChartInfo(id, hostnameSource, hostnameTarget, portTarget, dateValue);
        return LayUiAdminResultVo.ok(tcpAvgTimeChartVo);
    }

    /**
     * <p>
     * 清理TCP监控历史数据
     * </p>
     *
     * @param id   TCP ID
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/20 20:52
     */
    @Operation(summary = "清理TCP监控历史数据")
    @Parameters(value = {
            @Parameter(name = "id", description = "TCP ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", required = true, in = ParameterIn.QUERY)})
    @DeleteMapping("/clear-monitor-tcp-history")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "清理TCP监控历史数据")
    public LayUiAdminResultVo clearMonitorTcpHistory(Long id, String time) {
        return this.monitorTcpHistoryService.clearMonitorTcpHistory(id, time);
    }

}

