package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmMemoryHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.InstanceDetailPageJvmMemoryChartVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
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

import java.util.List;

/**
 * <p>
 * java虚拟机内存历史记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Api(tags = "应用程序.java虚拟机内存历史记录")
@Controller
@RequestMapping("/monitor-jvm-memory-history")
public class MonitorJvmMemoryHistoryController {

    /**
     * java虚拟机内存历史记录服务类
     */
    @Autowired
    private IMonitorJvmMemoryHistoryService monitorJvmMemoryHistoryService;

    /**
     * <p>
     * 获取应用实例详情页面java虚拟机内存图表信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param memoryType 内存类型
     * @param time       时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/14 11:52
     */
    @ApiOperation(value = "获取应用实例详情页面java虚拟机内存图表信息")
    @ResponseBody
    @GetMapping("/get-instance-detail-page-jvm-memory-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "memoryType", value = "内存类型", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "time", value = "时间", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getInstanceDetailPageJvmMemoryChartInfo(@RequestParam(name = "instanceId") String instanceId,
                                                                      @RequestParam(name = "memoryType", required = false) String memoryType,
                                                                      @RequestParam(name = "time", required = false) String time) {
        List<InstanceDetailPageJvmMemoryChartVo> monitorJvmMemoryChartVos = this.monitorJvmMemoryHistoryService.getInstanceDetailPageJvmMemoryChartInfo(instanceId, memoryType, time);
        return LayUiAdminResultVo.ok(monitorJvmMemoryChartVos);
    }

}

