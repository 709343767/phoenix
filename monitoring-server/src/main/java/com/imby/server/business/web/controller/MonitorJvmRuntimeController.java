package com.imby.server.business.web.controller;


import com.imby.server.business.web.service.IMonitorJvmRuntimeService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorJvmRuntimeVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * java虚拟机运行时信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@RestController
@RequestMapping("/monitor-jvm-runtime")
public class MonitorJvmRuntimeController {

    /**
     * java虚拟机运行时信息服务类
     */
    @Autowired
    private IMonitorJvmRuntimeService monitorJvmRuntimeService;

    /**
     * <p>
     * 获取java虚拟机运行时信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/15 19:43
     */
    @ApiOperation(value = "获取java虚拟机运行时信息")
    @ResponseBody
    @GetMapping("/get-jvm-runtime-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getJvmRuntimeInfo(@RequestParam(name = "instanceId") String instanceId) {
        MonitorJvmRuntimeVo monitorJvmRuntimeVo = this.monitorJvmRuntimeService.getJvmRuntimeInfo(instanceId);
        return LayUiAdminResultVo.ok(monitorJvmRuntimeVo);
    }

}

