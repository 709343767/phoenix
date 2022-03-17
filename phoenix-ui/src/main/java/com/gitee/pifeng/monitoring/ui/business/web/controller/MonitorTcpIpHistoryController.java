package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpIpHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerCpuChartVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.TcpIpAvgTimeChartVo;
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
 * TCP/IP信息历史记录表 前端控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Controller
@RequestMapping("/monitor-tcpip-history")
@Api(tags = "TCPIP.TCPIP历史记录")
public class MonitorTcpIpHistoryController {

    /**
     * TCP/IP信息历史记录服务类
     */
    @Autowired
    private IMonitorTcpIpHistoryService monitorTcpIpHistoryService;

    /**
     * <p>
     * 获取TCPIP连接耗时图表信息
     * </p>
     *
     * @param id         TCP/IP ID
     * @param ipSource   IP地址（来源）
     * @param ipTarget   IP地址（目的地）
     * @param portTarget 端口号
     * @param protocol   协议
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/3/17 21:37
     */
    @ApiOperation(value = "获取TCPIP连接耗时图表信息")
    @ResponseBody
    @GetMapping("/get-avg-time-chart-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "TCP/IP ID", paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "ipSource", value = "IP地址（来源）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "ipTarget", value = "IP地址（目的地）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "portTarget", value = "端口号", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "protocol", value = "协议", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getAvgTimeChartInfo(@RequestParam(name = "id") Long id,
                                                  @RequestParam(name = "ipSource") String ipSource,
                                                  @RequestParam(name = "ipTarget") String ipTarget,
                                                  @RequestParam(name = "portTarget") Integer portTarget,
                                                  @RequestParam(name = "protocol") String protocol,
                                                  @RequestParam(name = "startTime") String startTime,
                                                  @RequestParam(name = "endTime") String endTime) {
        List<TcpIpAvgTimeChartVo> monitorServerCpuChartVos = this.monitorTcpIpHistoryService
                .getAvgTimeChartInfo(id, ipSource, ipTarget, portTarget, protocol, startTime, endTime);
        return LayUiAdminResultVo.ok(monitorServerCpuChartVos);
    }

}

