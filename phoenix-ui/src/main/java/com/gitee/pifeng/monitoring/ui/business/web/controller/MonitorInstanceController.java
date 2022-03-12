package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorInstanceService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorJvmMemoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorInstanceVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

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
     * 监控环境服务类
     */
    @Autowired
    private IMonitorEnvService monitorEnvService;

    /**
     * 监控分组服务类
     */
    @Autowired
    private IMonitorGroupService monitorGroupService;

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
        ModelAndView mv = new ModelAndView("instance/instance");
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
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
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 10:59
     */
    @ApiOperation(value = "获取应用程序列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "instanceName", value = "应用实例名", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "endpoint", value = "端点", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "isOnline", value = "应用状态", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "monitorEnv", value = "监控环境", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "monitorGroup", value = "监控分组", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/get-monitor-instance-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.QUERY, operDesc = "获取应用程序列表")
    public LayUiAdminResultVo getMonitorInstanceList(Long current, Long size, String instanceName, String endpoint,
                                                     String isOnline, String monitorEnv, String monitorGroup) {
        Page<MonitorInstanceVo> page = this.monitorInstanceService.getMonitorInstanceList(current, size, instanceName, endpoint, isOnline, monitorEnv, monitorGroup);
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
    @DeleteMapping("/delete-monitor-instance")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.DELETE, operDesc = "删除应用程序")
    public LayUiAdminResultVo deleteMonitorInstance(@RequestBody List<MonitorInstanceVo> monitorInstanceVos) {
        return this.monitorInstanceService.deleteMonitorInstance(monitorInstanceVos);
    }

    /**
     * <p>
     * 清理应用程序监控历史数据
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param time       时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/20 20:52
     */
    @ApiOperation(value = "清理应用程序监控历史数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "time", value = "时间", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @DeleteMapping("/clear-monitor-instance-history")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.DELETE, operDesc = "清理应用程序监控历史数据")
    public LayUiAdminResultVo clearMonitorInstanceHistory(String instanceId, String time) {
        return this.monitorInstanceService.clearMonitorInstanceHistory(instanceId, time);
    }

    /**
     * <p>
     * 访问应用程序详情页面
     * </p>
     *
     * @param id         应用实例主键ID
     * @param instanceId 应用实例ID
     * @return {@link ModelAndView} 应用程序详情页面
     * @author 皮锋
     * @custom.date 2020/9/26 10:53
     */
    @ApiOperation(value = "访问应用程序详情页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "应用实例主键ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/instance-detail")
    public ModelAndView instanceDetail(Long id, String instanceId) {
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

    /**
     * <p>
     * 访问清理应用程序监控历史数据表单页面
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return {@link ModelAndView} 清理应用程序监控历史数据表单页面
     * @author 皮锋
     * @custom.date 2021/7/20 20:56
     */
    @ApiOperation(value = "访问清理应用程序监控历史数据表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("instance-clear")
    public ModelAndView instanceClear(String instanceId) {
        ModelAndView mv = new ModelAndView("instance/instance-clear-form");
        mv.addObject("instanceId", instanceId);
        return mv;
    }

    /**
     * <p>
     * 访问应用程序编辑页面
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return {@link ModelAndView} 应用程序编辑页面
     * @author 皮锋
     * @custom.date 2021/8/28 20:18
     */
    @ApiOperation(value = "访问应用程序编辑页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "instanceId", value = "应用实例ID", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("edit-monitor-instance-form")
    public ModelAndView editMonitorInstanceForm(String instanceId) {
        ModelAndView mv = new ModelAndView("instance/edit-instance");
        mv.addObject("instanceId", instanceId);
        MonitorInstanceVo monitorInstanceVo = this.monitorInstanceService.getMonitorInstanceInfo(instanceId);
        mv.addObject("instanceSummary", monitorInstanceVo.getInstanceSummary());
        mv.addObject("instanceDesc", monitorInstanceVo.getInstanceDesc());
        mv.addObject("env", monitorInstanceVo.getMonitorEnv());
        mv.addObject("group", monitorInstanceVo.getMonitorGroup());
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 编辑应用程序信息
     * </p>
     *
     * @param monitorInstanceVo 应用程序信息
     * @return 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/8/28 20:44
     */
    @ApiOperation(value = "编辑应用程序信息")
    @PutMapping("/edit-monitor-instance")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑应用程序信息")
    public LayUiAdminResultVo editMonitorInstance(MonitorInstanceVo monitorInstanceVo) {
        return this.monitorInstanceService.editMonitorInstance(monitorInstanceVo);
    }

}
