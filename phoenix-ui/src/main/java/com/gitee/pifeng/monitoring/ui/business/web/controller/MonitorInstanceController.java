package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
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
@Tag(name = "应用程序")
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
    @Operation(summary = "访问应用程序列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("instance/instance");
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.INSTANCE);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 获取应用程序列表
     * </p>
     *
     * @param current         当前页
     * @param size            每页显示条数
     * @param instanceName    应用实例名
     * @param endpoint        端点
     * @param isOnline        应用状态
     * @param monitorEnv      监控环境
     * @param monitorGroup    监控分组
     * @param ip              IP
     * @param appServerType   应用服务器
     * @param instanceDesc    描述
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @param isEnableAlarm   是否开启告警（0：不开启告警；1：开启告警）
     * @param language        程序语言
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 10:59
     */
    @Operation(summary = "获取应用程序列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceName", description = "应用实例名", in = ParameterIn.QUERY),
            @Parameter(name = "endpoint", description = "端点", in = ParameterIn.QUERY),
            @Parameter(name = "isOnline", description = "应用状态", in = ParameterIn.QUERY),
            @Parameter(name = "monitorEnv", description = "监控环境", in = ParameterIn.QUERY),
            @Parameter(name = "monitorGroup", description = "监控分组", in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "IP", in = ParameterIn.QUERY),
            @Parameter(name = "appServerType", description = "应用服务器", in = ParameterIn.QUERY),
            @Parameter(name = "instanceDesc", description = "描述", in = ParameterIn.QUERY),
            @Parameter(name = "isEnableMonitor", description = "是否开启监控（0：不开启监控；1：开启监控）", in = ParameterIn.QUERY),
            @Parameter(name = "isEnableAlarm", description = "是否开启告警（0：不开启告警；1：开启告警）", in = ParameterIn.QUERY),
            @Parameter(name = "language", description = "程序语言", in = ParameterIn.QUERY)
    })
    @GetMapping("/get-monitor-instance-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.QUERY, operDesc = "获取应用程序列表")
    public LayUiAdminResultVo getMonitorInstanceList(@RequestParam(value = "current") Long current,
                                                     @RequestParam(value = "size") Long size,
                                                     @RequestParam(value = "instanceName", required = false) String instanceName,
                                                     @RequestParam(value = "endpoint", required = false) String endpoint,
                                                     @RequestParam(value = "isOnline", required = false) String isOnline,
                                                     @RequestParam(value = "monitorEnv", required = false) String monitorEnv,
                                                     @RequestParam(value = "monitorGroup", required = false) String monitorGroup,
                                                     @RequestParam(value = "ip", required = false) String ip,
                                                     @RequestParam(value = "appServerType", required = false) String appServerType,
                                                     @RequestParam(value = "instanceDesc", required = false) String instanceDesc,
                                                     @RequestParam(value = "isEnableMonitor", required = false) String isEnableMonitor,
                                                     @RequestParam(value = "isEnableAlarm", required = false) String isEnableAlarm,
                                                     @RequestParam(value = "language", required = false) String language) {
        Page<MonitorInstanceVo> page = this.monitorInstanceService.getMonitorInstanceList(current, size, instanceName,
                endpoint, isOnline, monitorEnv, monitorGroup, ip, appServerType, instanceDesc, isEnableMonitor,
                isEnableAlarm, language);
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
    @Operation(summary = "删除应用程序")
    @DeleteMapping("/delete-monitor-instance")
    @PreAuthorize("hasAuthority('超级管理员')")
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
    @Operation(summary = "清理应用程序监控历史数据")
    @Parameters(value = {
            @Parameter(name = "instanceId", description = "应用实例ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", required = true, in = ParameterIn.QUERY)})
    @DeleteMapping("/clear-monitor-instance-history")
    @PreAuthorize("hasAuthority('超级管理员')")
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
    @Operation(summary = "访问应用程序详情页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "应用实例主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceId", description = "应用实例ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/instance-detail")
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.PAGE, operDesc = "访问应用程序详情页面")
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
    @Operation(summary = "访问清理应用程序监控历史数据表单页面")
    @Parameters(value = {
            @Parameter(name = "instanceId", description = "应用实例ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/instance-clear")
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
    @Operation(summary = "访问应用程序编辑页面")
    @Parameters(value = {
            @Parameter(name = "instanceId", description = "应用实例ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-instance-form")
    public ModelAndView editMonitorInstanceForm(String instanceId) {
        ModelAndView mv = new ModelAndView("instance/edit-instance");
        mv.addObject("instanceId", instanceId);
        MonitorInstanceVo monitorInstanceVo = this.monitorInstanceService.getMonitorInstanceInfo(instanceId);
        mv.addObject("instanceSummary", monitorInstanceVo.getInstanceSummary());
        mv.addObject("instanceDesc", monitorInstanceVo.getInstanceDesc());
        mv.addObject("env", monitorInstanceVo.getMonitorEnv());
        mv.addObject("group", monitorInstanceVo.getMonitorGroup());
        mv.addObject("isEnableMonitor", monitorInstanceVo.getIsEnableMonitor());
        mv.addObject("isEnableAlarm", monitorInstanceVo.getIsEnableAlarm());
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.INSTANCE);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
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
    @Operation(summary = "编辑应用程序信息")
    @PutMapping("/edit-monitor-instance")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑应用程序信息")
    public LayUiAdminResultVo editMonitorInstance(MonitorInstanceVo monitorInstanceVo) {
        return this.monitorInstanceService.editMonitorInstance(monitorInstanceVo);
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param instanceId      应用实例ID
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Operation(summary = "设置是否开启监控（0：不开启监控；1：开启监控）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceId", description = "IP地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableMonitor", description = "是否开启监控（0：不开启监控；1：开启监控）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-monitor")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启监控（0：不开启监控；1：开启监控）")
    public LayUiAdminResultVo setIsEnableMonitor(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "instanceId") String instanceId,
                                                 @RequestParam(value = "isEnableMonitor") String isEnableMonitor) {
        return this.monitorInstanceService.setIsEnableMonitor(id, instanceId, isEnableMonitor);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param instanceId    应用实例ID
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Operation(summary = "设置是否开启告警（0：不开启告警；1：开启告警）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceId", description = "IP地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableAlarm", description = "是否开启告警（0：不开启告警；1：开启告警）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-alarm")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.INSTANCE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启告警（0：不开启告警；1：开启告警）")
    public LayUiAdminResultVo setIsEnableAlarm(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "instanceId") String instanceId,
                                               @RequestParam(value = "isEnableAlarm") String isEnableAlarm) {
        return this.monitorInstanceService.setIsEnableAlarm(id, instanceId, isEnableAlarm);
    }

    /**
     * <p>
     * 获取应用程序信息(Map形式)
     * </p>
     *
     * @return clayUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2023/4/16 11:17
     */
    @Operation(summary = "获取应用程序信息(Map形式)")
    @PostMapping("/get-monitor-instance-to-map")
    @ResponseBody
    public LayUiAdminResultVo getMonitorInstance2Map() {
        Map<String, MonitorInstanceVo> map = this.monitorInstanceService.getMonitorInstance2Map();
        return LayUiAdminResultVo.ok(map);
    }

}
