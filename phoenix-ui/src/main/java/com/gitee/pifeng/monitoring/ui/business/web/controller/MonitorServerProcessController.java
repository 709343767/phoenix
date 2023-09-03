package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerProcessService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerProcessVo;
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
 * 服务器进程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
@RestController
@Tag(name = "服务器.服务器进程")
@RequestMapping("/monitor-server-process")
public class MonitorServerProcessController {

    /**
     * 服务器进程服务类
     */
    @Autowired
    private IMonitorServerProcessService monitorServerProcessService;

    /**
     * <p>
     * 获取服务器详情页面服务器进程信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/9/18 15:55
     */
    @Operation(summary = "获取服务器详情页面服务器进程信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-process-info")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP地址", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getServerDetailPageServerProcessInfo(@RequestParam(name = "ip") String ip) {
        List<MonitorServerProcessVo> monitorServerCpuVos = this.monitorServerProcessService.getServerDetailPageServerProcessInfo(ip);
        return LayUiAdminResultVo.ok(monitorServerCpuVos);
    }

}

