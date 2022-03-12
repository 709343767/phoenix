package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerSensorsService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerSensorsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 服务器传感器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-15
 */
@Api(tags = "服务器.服务器传感器")
@Controller
@RequestMapping("/monitor-server-sensors")
public class MonitorServerSensorsController {

    /**
     * 服务器传感器服务类
     */
    @Autowired
    private IMonitorServerSensorsService monitorServerSensorsService;

    /**
     * <p>
     * 获取服务器详情页面服务器传感器信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/1/16 23:04
     */
    @ApiOperation(value = "获取服务器详情页面服务器传感器信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-sensors-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getServerDetailPageServerSensorsInfo(@RequestParam(name = "ip") String ip) {
        MonitorServerSensorsVo serverSensorsVo = this.monitorServerSensorsService.getServerDetailPageServerSensorsInfo(ip);
        return LayUiAdminResultVo.ok(serverSensorsVo);
    }

    /**
     * <p>
     * 获取服务器详情页面服务器CPU温度图表信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/1/17 21:00
     */
    @ApiOperation(value = "获取服务器详情页面服务器CPU温度图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-sensors-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getServerDetailPageServerSensorsChartInfo(@RequestParam(name = "ip") String ip) {
        Double cpuTemperature = this.monitorServerSensorsService.getCpuTemperatureInfo(ip);
        return LayUiAdminResultVo.ok(cpuTemperature);
    }

}

