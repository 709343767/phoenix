package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmThreadService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorJvmThreadVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * java虚拟机线程信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Tag(name = "应用程序.java虚拟机线程信息")
@RestController
@RequestMapping("/monitor-jvm-thread")
public class MonitorJvmThreadController {

    /**
     * java虚拟机线程信息服务类
     */
    @Autowired
    private IMonitorJvmThreadService monitorJvmThreadService;

    /**
     * <p>
     * 获取java虚拟机线程信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/15 12:43
     */
    @Operation(summary = "获取java虚拟机线程信息")
    @ResponseBody
    @GetMapping("/get-jvm-thread-info")
    @Parameters(value = {
            @Parameter(name = "instanceId", description = "应用实例ID", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getJvmThreadInfo(@RequestParam(name = "instanceId") String instanceId) {
        MonitorJvmThreadVo monitorJvmThreadVo = this.monitorJvmThreadService.getJvmThreadInfo(instanceId);
        return LayUiAdminResultVo.ok(monitorJvmThreadVo);
    }

}

