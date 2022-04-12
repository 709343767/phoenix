package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorHttpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HttpAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "HTTP.HTTP历史记录")
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
    @ApiOperation(value = "获取访问耗时图表信息")
    @ResponseBody
    @GetMapping("/get-avg-time-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "HTTP ID", paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "hostnameSource", value = "主机名（来源）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "urlTarget", value = "URL地址（目的地）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "method", value = "请求方法", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "dateValue", value = "时间", paramType = "query", dataType = "string", dataTypeClass = String.class)})
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
    @ApiOperation(value = "清理HTTP监控历史数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "HTTP ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "time", value = "时间", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @DeleteMapping("/clear-monitor-http-history")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "清理HTTP监控历史数据")
    public LayUiAdminResultVo clearMonitorHttpHistory(Long id, String time) {
        return this.monitorHttpHistoryService.clearMonitorHttpHistory(id, time);
    }

}

