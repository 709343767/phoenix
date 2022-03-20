package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.NetworkAvgTimeChartVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
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
 * 网络信息历史记录前端控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Controller
@RequestMapping("/monitor-network-history")
@Api(tags = "网络.网络历史记录")
public class MonitorNetworkHistoryController {

    /**
     * 网络信息历史记录服务类
     */
    @Autowired
    private IMonitorNetHistoryService monitorNetHistoryService;

    /**
     * <p>
     * 获取PING耗时图表信息
     * </p>
     *
     * @param id        TCP/IP ID
     * @param ipSource  IP地址（来源）
     * @param ipTarget  IP地址（目的地）
     * @param dateValue 时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    @ApiOperation(value = "获取PING耗时图表信息")
    @ResponseBody
    @GetMapping("/get-avg-time-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "网络ID", paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "ipSource", value = "IP地址（来源）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "ipTarget", value = "IP地址（目的地）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "dateValue", value = "时间", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.QUERY, operDesc = "获取PING耗时图表信息")
    public LayUiAdminResultVo getAvgTimeChartInfo(@RequestParam(name = "id") Long id,
                                                  @RequestParam(name = "ipSource") String ipSource,
                                                  @RequestParam(name = "ipTarget") String ipTarget,
                                                  @RequestParam(name = "dateValue") String dateValue) {
        NetworkAvgTimeChartVo networkAvgTimeChartVo = this.monitorNetHistoryService
                .getAvgTimeChartInfo(id, ipSource, ipTarget, dateValue);
        return LayUiAdminResultVo.ok(networkAvgTimeChartVo);
    }

}

