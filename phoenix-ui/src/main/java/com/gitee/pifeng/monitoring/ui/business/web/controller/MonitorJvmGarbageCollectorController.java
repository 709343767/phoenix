package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmGarbageCollectorService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorJvmGarbageCollectorVo;
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
 * java虚拟机GC信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Tag(name = "应用程序.java虚拟机GC信息")
@RestController
@RequestMapping("/monitor-jvm-garbage-collector")
public class MonitorJvmGarbageCollectorController {

    /**
     * java虚拟机GC信息服务类
     */
    @Autowired
    private IMonitorJvmGarbageCollectorService monitorJvmGarbageCollectorService;

    /**
     * <p>
     * 获取java虚拟机GC信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/15 13:10
     */
    @Operation(summary = "获取java虚拟机GC信息")
    @ResponseBody
    @GetMapping("/get-jvm-gc-info")
    @Parameters(value = {
            @Parameter(name = "instanceId", description = "应用实例ID", required = true, in = ParameterIn.QUERY)})
    public LayUiAdminResultVo getJvmGcInfo(@RequestParam(name = "instanceId") String instanceId) {
        List<MonitorJvmGarbageCollectorVo> monitorJvmGarbageCollectorVos = this.monitorJvmGarbageCollectorService.getJvmGcInfo(instanceId);
        return LayUiAdminResultVo.ok(monitorJvmGarbageCollectorVos);
    }

}

