package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerProcessHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerProcessChartVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "服务器进程历史记录")
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
    @ApiOperation(value = "获取服务器详情页面服务器进程图表信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-process-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getServerDetailPageServerProcessChartInfo(@RequestParam(name = "ip") String ip,
                                                                        @RequestParam(name = "time", required = false) String time) {
        List<ServerDetailPageServerProcessChartVo> monitorServerProcessChartVos = this.monitorServerProcessHistoryService.getServerDetailPageServerProcessChartInfo(ip, time);
        return LayUiAdminResultVo.ok(monitorServerProcessChartVos);
    }

}

