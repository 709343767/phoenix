package com.imby.server.business.web.controller;


import com.imby.server.business.web.service.IMonitorJvmClassLoadingService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorJvmClassLoadingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * java虚拟机类加载信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Api(tags = "java虚拟机类加载信息")
@RestController
@RequestMapping("/monitor-jvm-class-loading")
public class MonitorJvmClassLoadingController {

    /**
     * java虚拟机类加载信息服务类
     */
    @Autowired
    private IMonitorJvmClassLoadingService monitorJvmClassLoadingService;

    /**
     * <p>
     * 获取java虚拟机类加载信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/15 13:44
     */
    @ApiOperation(value = "获取java虚拟机类加载信息")
    @ResponseBody
    @GetMapping("/get-jvm-class-loading-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getJvmClassLoadingInfo(@RequestParam(name = "instanceId") String instanceId) {
        MonitorJvmClassLoadingVo monitorJvmClassLoadingVo = this.monitorJvmClassLoadingService.getJvmClassLoadingInfo(instanceId);
        return LayUiAdminResultVo.ok(monitorJvmClassLoadingVo);
    }

}

