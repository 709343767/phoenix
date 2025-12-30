package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorConfigService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorConfigPageFormVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerVo;
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
 * 服务器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/2 21:50
 */
@Controller
@RequestMapping("/monitor-server")
@Tag(name = "服务器")
public class MonitorServerController {

    /**
     * 服务器服务类
     */
    @Autowired
    private IMonitorServerService monitorServerService;

    /**
     * 监控配置服务类
     */
    @Autowired
    private IMonitorConfigService monitorConfigService;

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
     * 访问服务器列表页面
     * </p>
     *
     * @return {@link ModelAndView} 服务器列表页面
     * @author 皮锋
     * @custom.date 2020/9/2 21:46
     */
    @Operation(summary = "访问服务器列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("server/server");
        // 监控配置
        MonitorConfigPageFormVo monitorConfigPageFormInfo = this.monitorConfigService.getMonitorConfigPageFormInfo();
        mv.addObject("monitorConfigPageFormInfo", monitorConfigPageFormInfo);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.SERVER);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @param current       当前页
     * @param size          每页显示条数
     * @param ip            IP
     * @param isOnline      状态
     * @param serverName    服务器名
     * @param monitorEnv    监控环境
     * @param monitorGroup  监控分组
     * @param osName        系统
     * @param serverSummary 描述
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/4 12:34
     */
    @Operation(summary = "获取服务器列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "IP", in = ParameterIn.QUERY),
            @Parameter(name = "serverName", description = "服务器名", in = ParameterIn.QUERY),
            @Parameter(name = "isOnline", description = "状态", in = ParameterIn.QUERY),
            @Parameter(name = "monitorEnv", description = "监控环境", in = ParameterIn.QUERY),
            @Parameter(name = "monitorGroup", description = "监控分组", in = ParameterIn.QUERY),
            @Parameter(name = "osName", description = "系统", in = ParameterIn.QUERY),
            @Parameter(name = "serverSummary", description = "描述", in = ParameterIn.QUERY)})
    @GetMapping("/get-monitor-server-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.QUERY, operDesc = "获取服务器列表")
    public LayUiAdminResultVo getMonitorServerList(@RequestParam(value = "current") Long current,
                                                   @RequestParam(value = "size") Long size,
                                                   @RequestParam(value = "ip", required = false) String ip,
                                                   @RequestParam(value = "serverName", required = false) String serverName,
                                                   @RequestParam(value = "isOnline", required = false) String isOnline,
                                                   @RequestParam(value = "monitorEnv", required = false) String monitorEnv,
                                                   @RequestParam(value = "monitorGroup", required = false) String monitorGroup,
                                                   @RequestParam(value = "osName", required = false) String osName,
                                                   @RequestParam(value = "serverSummary", required = false) String serverSummary) {
        Page<MonitorServerVo> page = this.monitorServerService.getMonitorServerList(current, size, ip, serverName, isOnline, monitorEnv, monitorGroup, osName, serverSummary);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除服务器
     * </p>
     *
     * @param monitorServerVos 服务器信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/4 16:10
     */
    @Operation(summary = "删除服务器")
    @DeleteMapping("/delete-monitor-server")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.DELETE, operDesc = "删除服务器")
    public LayUiAdminResultVo deleteMonitorServer(@RequestBody List<MonitorServerVo> monitorServerVos) {
        return this.monitorServerService.deleteMonitorServer(monitorServerVos);
    }

    /**
     * <p>
     * 清理服务器监控历史数据
     * </p>
     *
     * @param ip   IP地址
     * @param time 时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/20 20:52
     */
    @Operation(summary = "清理服务器监控历史数据")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "time", description = "时间", required = true, in = ParameterIn.QUERY)})
    @DeleteMapping("/clear-monitor-server-history")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.DELETE, operDesc = "清理服务器监控历史数据")
    public LayUiAdminResultVo clearMonitorServerHistory(String ip, String time) {
        return this.monitorServerService.clearMonitorServerHistory(ip, time);
    }

    /**
     * <p>
     * 访问服务器详情页面
     * </p>
     *
     * @param id 服务器主键ID
     * @param ip 服务器IP
     * @return {@link ModelAndView} 服务器详情页面
     * @author 皮锋
     * @custom.date 2020/10/16 15:59
     */
    @Operation(summary = "访问服务器详情页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "服务器主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "服务器IP", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/server-detail")
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.PAGE, operDesc = "访问服务器详情页面")
    public ModelAndView serverDetail(Long id, String ip) {
        ModelAndView mv = new ModelAndView("server/server-detail");
        mv.addObject("id", id);
        mv.addObject("ip", ip);
        // 监控配置
        MonitorConfigPageFormVo monitorConfigPageFormInfo = this.monitorConfigService.getMonitorConfigPageFormInfo();
        mv.addObject("monitorConfigPageFormInfo", monitorConfigPageFormInfo);
        // 获取服务器网卡地址
        List<String> netcardAddresses = this.monitorServerService.getNetcardAddress(ip);
        mv.addObject("netcardAddresses", netcardAddresses);
        return mv;
    }


    /**
     * <p>
     * 访问清理服务器监控历史数据表单页面
     * </p>
     *
     * @param ip 服务器IP
     * @return {@link ModelAndView} 清理服务器监控历史数据表单页面
     * @author 皮锋
     * @custom.date 2021/7/20 20:56
     */
    @Operation(summary = "访问清理服务器监控历史数据表单页面")
    @Parameters(value = {
            @Parameter(name = "ip", description = "服务器IP", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/server-clear")
    public ModelAndView serverClear(String ip) {
        ModelAndView mv = new ModelAndView("server/server-clear-form");
        mv.addObject("ip", ip);
        return mv;
    }

    /**
     * <p>
     * 访问服务器编辑页面
     * </p>
     *
     * @param id 服务器主键ID
     * @param ip 服务器IP
     * @return {@link ModelAndView} 服务器编辑页面
     * @author 皮锋
     * @custom.date 2021/8/27 12:37
     */
    @Operation(summary = "访问服务器编辑页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "服务器主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "服务器IP", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-server-form")
    public ModelAndView editMonitorServerForm(Long id, String ip) {
        ModelAndView mv = new ModelAndView("server/edit-server");
        mv.addObject("id", id);
        mv.addObject("ip", ip);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.SERVER);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        // 服务器信息
        MonitorServerVo monitorServerVo = this.monitorServerService.getMonitorServerInfo(id, ip);
        mv.addObject("serverSummary", monitorServerVo.getServerSummary());
        mv.addObject("env", monitorServerVo.getMonitorEnv());
        mv.addObject("group", monitorServerVo.getMonitorGroup());
        mv.addObject("isEnableMonitor", monitorServerVo.getIsEnableMonitor());
        mv.addObject("isEnableAlarm", monitorServerVo.getIsEnableAlarm());
        return mv;
    }

    /**
     * <p>
     * 编辑服务器信息
     * </p>
     *
     * @param monitorServerVo 服务器信息
     * @return 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/8/27 13:45
     */
    @Operation(summary = "编辑服务器信息")
    @PutMapping("/edit-monitor-server")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.UPDATE, operDesc = "编辑服务器信息")
    public LayUiAdminResultVo editMonitorServer(MonitorServerVo monitorServerVo) {
        return this.monitorServerService.editMonitorServer(monitorServerVo);
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param ip              IP地址
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Operation(summary = "设置是否开启监控（0：不开启监控；1：开启监控）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "IP地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableMonitor", description = "是否开启监控（0：不开启监控；1：开启监控）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-monitor")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启监控（0：不开启监控；1：开启监控）")
    public LayUiAdminResultVo setIsEnableMonitor(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "ip") String ip,
                                                 @RequestParam(value = "isEnableMonitor") String isEnableMonitor) {
        return this.monitorServerService.setIsEnableMonitor(id, ip, isEnableMonitor);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param ip            IP地址
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Operation(summary = "设置是否开启告警（0：不开启告警；1：开启告警）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "IP地址", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableAlarm", description = "是否开启告警（0：不开启告警；1：开启告警）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-alarm")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启告警（0：不开启告警；1：开启告警）")
    public LayUiAdminResultVo setIsEnableAlarm(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "ip") String ip,
                                               @RequestParam(value = "isEnableAlarm") String isEnableAlarm) {
        return this.monitorServerService.setIsEnableAlarm(id, ip, isEnableAlarm);
    }

    /**
     * <p>
     * 获取服务器信息(Map形式)
     * </p>
     *
     * @return com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo
     * @author 皮锋
     * @custom.date 2022/12/21 14:58
     */
    @Operation(summary = "获取服务器信息(Map形式)")
    @PostMapping("/get-monitor-server-to-map")
    @ResponseBody
    public LayUiAdminResultVo getMonitorServer2Map() {
        Map<String, MonitorServerVo> monitorServer2Map = this.monitorServerService.getMonitorServer2Map();
        return LayUiAdminResultVo.ok(monitorServer2Map);
    }

}
