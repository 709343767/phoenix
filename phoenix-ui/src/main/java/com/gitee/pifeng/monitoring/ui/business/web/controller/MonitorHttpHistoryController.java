package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorHttpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HttpAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
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
 * HTTP信息历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-12
 */
@Controller
@RequestMapping("/monitor-http-history")
@Tag(name = "HTTP.HTTP历史记录")
public class MonitorHttpHistoryController {

    /**
     * HTTP信息历史记录服务类
     */
    @Autowired
    private IMonitorHttpHistoryService monitorHttpHistoryService;

    /**
     * <p>
     * 获取访问耗时图表信息
     * </p>
     *
     * @param id             HTTP ID
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param dateValue      时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    @Operation(summary = "获取访问耗时图表信息")
    @ResponseBody
    @GetMapping("/get-avg-time-chart-info")
    @Parameters(value = {
            @Parameter(name = "id", description = "HTTP ID", in = ParameterIn.QUERY),
            @Parameter(name = "hostnameSource", description = "主机名（来源）", in = ParameterIn.QUERY),
            @Parameter(name = "urlTarget", description = "URL地址（目的地）", in = ParameterIn.QUERY),
            @Parameter(name = "method", description = "请求方法", in = ParameterIn.QUERY),
            @Parameter(name = "dateValue", description = "时间", in = ParameterIn.QUERY)})
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取访问耗时图表信息")
    public LayUiAdminResultVo getAvgTimeChartInfo(@RequestParam(name = "id") Long id,
                                                  @RequestParam(name = "hostnameSource") String hostnameSource,
                                                  @RequestParam(name = "urlTarget") String urlTarget,
                                                  @RequestParam(name = "method") String method,
                                                  @RequestParam(name = "dateValue") String dateValue) {
        HttpAvgTimeChartVo httpAvgTimeChartVo = this.monitorHttpHistoryService
                .getAvgTimeChartInfo(id, hostnameSource, urlTarget, method, dateValue);
        return LayUiAdminResultVo.ok(httpAvgTimeChartVo);
    }

    /**
     * <p>
     * 清理HTTP监控历史数据
     * </p>
     *
     * @param id   HTTP ID
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/20 20:52
     */
    @Operation(summary = "清理HTTP监控历史数据")
    @Parameters(value = {
            @Parameter(name = "id", description = "HTTP ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", required = true, in = ParameterIn.QUERY)})
    @DeleteMapping("/clear-monitor-http-history")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "清理HTTP监控历史数据")
    public LayUiAdminResultVo clearMonitorHttpHistory(Long id, String time) {
        return this.monitorHttpHistoryService.clearMonitorHttpHistory(id, time);
    }

}

