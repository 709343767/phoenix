package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerProcessService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "服务器进程")
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
    @ApiOperation(value = "获取服务器详情页面服务器进程信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-process-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    public LayUiAdminResultVo getServerDetailPageServerProcessInfo(@RequestParam(name = "ip") String ip) {
        List<MonitorServerProcessVo> monitorServerCpuVos = this.monitorServerProcessService.getServerDetailPageServerProcessInfo(ip);
        return LayUiAdminResultVo.ok(monitorServerCpuVos);
    }

}

