package com.gitee.pifeng.server.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.service.IMonitorInstanceService;
import com.gitee.pifeng.server.business.web.service.IMonitorJvmMemoryService;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.business.web.vo.MonitorInstanceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 应用程序
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/26 10:53
 */
@Controller
@RequestMapping("/monitor-instance")
@Api(tags = "应用程序")
public class MonitorInstanceController {

    /**
     * 应用实例服务类
     */
    @Autowired
    private IMonitorInstanceService monitorInstanceService;

    /**
     * java虚拟机内存信息服务类
     */
    @Autowired
    private IMonitorJvmMemoryService monitorJvmMemoryService;

    /**
     * <p>
     * 访问应用程序列表页面
     * </p>
     *
     * @return {@link ModelAndView} 应用程序列表页面
     * @author 皮锋
     * @custom.date 2020/9/26 10:53
     */
    @ApiOperation(value = "访问应用程序列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("instance/instance");
    }

    /**
     * <p>
     * 获取应用程序列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param instanceName 应用实例名
     * @param endpoint     端点
     * @param isOnline     应用状态
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 10:59
     */
    @ApiOperation(value = "获取应用程序列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "instanceName", value = "应用实例名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "endpoint", value = "端点", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isOnline", value = "应用状态", paramType = "query", dataType = "string")})
    @GetMapping("/get-monitor-instance-list")
    @ResponseBody
    public LayUiAdminResultVo getMonitorInstanceList(Long current, Long size, String instanceName, String endpoint, String isOnline) {
        Page<MonitorInstanceVo> page = this.monitorInstanceService.getMonitorInstanceList(current, size, instanceName, endpoint, isOnline);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除应用程序
     * </p>
     *
     * @param monitorInstanceVos 应用程序信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 12:59
     */
    @ApiOperation(value = "删除应用程序")
    @PostMapping("/delete-monitor-instance")
    @ResponseBody
    public LayUiAdminResultVo deleteMonitorInstance(@RequestBody List<MonitorInstanceVo> monitorInstanceVos) {
        return this.monitorInstanceService.deleteMonitorInstance(monitorInstanceVos);
    }


    /**
     * <p>
     * 访问应用程序详情页面
     * </p>
     *
     * @param id         应用实例表的主键ID
     * @param instanceId 应用实例ID
     * @return {@link ModelAndView} 应用程序详情页面
     * @author 皮锋
     * @custom.date 2020/9/26 10:53
     */
    @ApiOperation(value = "访问应用程序详情页面")
    @GetMapping("/instance-detail")
    public ModelAndView instanceDetail(String id, String instanceId) {
        ModelAndView mv = new ModelAndView("instance/instance-detail");
        mv.addObject("id", id);
        mv.addObject("instanceId", instanceId);
        // 获取jvm内存类型
        List<String> jvmMemoryTypes = this.monitorJvmMemoryService.getJvmMemoryTypes(instanceId);
        jvmMemoryTypes.remove("Heap");
        jvmMemoryTypes.remove("Non_Heap");
        mv.addObject("jvmMemoryTypes", jvmMemoryTypes);
        return mv;
    }

}
