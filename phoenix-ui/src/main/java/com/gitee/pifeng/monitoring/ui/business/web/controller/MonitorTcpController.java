package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * TCP信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
@Slf4j
@Controller
@RequestMapping("/monitor-tcp")
@Tag(name = "TCP")
public class MonitorTcpController {

    /**
     * 网络信息服务类
     */
    @Autowired
    private IMonitorNetService monitorNetService;

    /**
     * TCP信息服务类
     */
    @Autowired
    private IMonitorTcpService monitorTcpService;

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
     * 访问TCP列表页面
     * </p>
     *
     * @return {@link ModelAndView} TCP列表页面
     * @author 皮锋
     * @custom.date 2022/1/11 9:27
     */
    @Operation(summary = "访问TCP列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("tcp/tcp");
        // 源IP
        mv.addObject("ipSource", this.monitorNetService.getSourceIp());
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.TCP4SERVICE);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 获取TCP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     目标端口
     * @param status         状态（0：不通，1：正常）
     * @param monitorEnv     监控环境
     * @param monitorGroup   监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/1/11 9:31
     */
    @Operation(summary = "获取TCP列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "hostnameSource", description = "主机名（来源）", in = ParameterIn.QUERY),
            @Parameter(name = "hostnameTarget", description = "主机名（目的地）", in = ParameterIn.QUERY),
            @Parameter(name = "portTarget", description = "目标端口", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态（0：不通，1：正常）", in = ParameterIn.QUERY),
            @Parameter(name = "monitorEnv", description = "监控环境", in = ParameterIn.QUERY),
            @Parameter(name = "monitorGroup", description = "监控分组", in = ParameterIn.QUERY)})
    @GetMapping("/get-monitor-tcp-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取TCP列表")
    public LayUiAdminResultVo getMonitorTcpList(@RequestParam(value = "current") Long current,
                                                @RequestParam(value = "size") Long size,
                                                @RequestParam(value = "hostnameSource", required = false) String hostnameSource,
                                                @RequestParam(value = "hostnameTarget", required = false) String hostnameTarget,
                                                @RequestParam(value = "portTarget", required = false) Integer portTarget,
                                                @RequestParam(value = "status", required = false) String status,
                                                @RequestParam(value = "monitorEnv", required = false) String monitorEnv,
                                                @RequestParam(value = "monitorGroup", required = false) String monitorGroup) {
        Page<MonitorTcpVo> page = this.monitorTcpService.getMonitorTcpList(current, size, hostnameSource, hostnameTarget,
                portTarget, status, monitorEnv, monitorGroup);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除TCP
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    @Operation(summary = "删除TCP")
    @DeleteMapping("/delete-monitor-tcp")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "删除TCP")
    public LayUiAdminResultVo deleteMonitorTcp(@RequestBody List<Long> ids) {
        return this.monitorTcpService.deleteMonitorTcp(ids);
    }

    /**
     * <p>
     * 访问新增TCP信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增TCP信息表单页面
     * @author 皮锋
     * @custom.date 2022/1/11 10:52
     */
    @Operation(summary = "访问新增TCP信息表单页面")
    @GetMapping("/add-monitor-tcp-form")
    public ModelAndView addMonitorTcpForm() {
        ModelAndView mv = new ModelAndView("tcp/add-tcp");
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.TCP4SERVICE);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 添加TCP信息
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/1/11 10:16
     */
    @Operation(summary = "添加TCP信息")
    @PostMapping("/add-monitor-tcp")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.ADD, operDesc = "添加TCP信息")
    public LayUiAdminResultVo addMonitorTcp(MonitorTcpVo monitorTcpVo) throws NetException, SigarException, IOException {
        // 获取被监控TCP源IP地址，获取失败则返回null
        String sourceIp = this.monitorNetService.getSourceIp();
        monitorTcpVo.setHostnameSource(sourceIp);
        LayUiAdminResultVo layUiAdminResultVo = this.monitorTcpService.addMonitorTcp(monitorTcpVo);
        // 测试TCP连通性
        this.monitorTcpService.testMonitorTcp(monitorTcpVo);
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 访问编辑TCP信息表单页面
     * </p>
     *
     * @param id TCP ID
     * @return {@link ModelAndView} 编辑TCP信息表单页面
     * @author 皮锋
     * @custom.date 2022/1/11 11:20
     */
    @Operation(summary = "访问编辑TCP信息表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "TCP ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-tcp-form")
    public ModelAndView editMonitorTcpForm(@RequestParam(name = "id") Long id) {
        MonitorTcp monitorTcp = this.monitorTcpService.getById(id);
        MonitorTcpVo monitorTcpVo = MonitorTcpVo.builder().build().convertFor(monitorTcp);
        ModelAndView mv = new ModelAndView("tcp/edit-tcp");
        mv.addObject(monitorTcpVo);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<MonitorGroup> monitorGroupList = this.monitorGroupService.getMonitorGroupList(MonitorTypeEnums.TCP4SERVICE);
        List<String> monitorGroups = monitorGroupList.stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        mv.addObject("env", monitorTcpVo.getMonitorEnv());
        mv.addObject("group", monitorTcpVo.getMonitorGroup());
        return mv;
    }

    /**
     * <p>
     * 编辑TCP信息
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/1/11 12:26
     */
    @Operation(summary = "编辑TCP信息")
    @PutMapping("/edit-monitor-tcp")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑TCP信息")
    public LayUiAdminResultVo editMonitorTcp(MonitorTcpVo monitorTcpVo) throws SigarException, IOException {
        // 获取被监控TCP源IP地址，获取失败则返回null
        String sourceIp = this.monitorNetService.getSourceIp();
        monitorTcpVo.setHostnameSource(sourceIp);
        LayUiAdminResultVo layUiAdminResultVo = this.monitorTcpService.editMonitorTcp(monitorTcpVo);
        // 测试TCP连通性
        this.monitorTcpService.testMonitorTcp(monitorTcpVo);
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Operation(summary = "设置是否开启监控（0：不开启监控；1：开启监控）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableMonitor", description = "是否开启监控（0：不开启监控；1：开启监控）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-monitor")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启监控（0：不开启监控；1：开启监控）")
    public LayUiAdminResultVo setIsEnableMonitor(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "isEnableMonitor") String isEnableMonitor) {
        return this.monitorTcpService.setIsEnableMonitor(id, isEnableMonitor);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Operation(summary = "设置是否开启告警（0：不开启告警；1：开启告警）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableAlarm", description = "是否开启告警（0：不开启告警；1：开启告警）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-alarm")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启告警（0：不开启告警；1：开启告警）")
    public LayUiAdminResultVo setIsEnableAlarm(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "isEnableAlarm") String isEnableAlarm) {
        return this.monitorTcpService.setIsEnableAlarm(id, isEnableAlarm);
    }

    /**
     * <p>
     * 访问平均时间页面
     * </p>
     *
     * @param id TCP ID
     * @return {@link ModelAndView} 平均时间页面
     * @author 皮锋
     * @custom.date 2022/3/17 11:13
     */
    @Operation(summary = "访问平均时间页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "TCP ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/avg-time")
    public ModelAndView avgTime(Long id) {
        MonitorTcp monitorTcp = this.monitorTcpService.getById(id);
        MonitorTcpVo monitorTcpVo = MonitorTcpVo.builder().build().convertFor(monitorTcp);
        ModelAndView mv = new ModelAndView("tcp/avg-time");
        mv.addObject(monitorTcpVo);
        return mv;
    }

    /**
     * <p>
     * 访问清理TCP服务监控历史数据表单页面
     * </p>
     *
     * @param id 主键ID
     * @return {@link ModelAndView} 清理TCP服务监控历史数据表单页面
     * @author 皮锋
     * @custom.date 2021/7/20 20:56
     */
    @Operation(summary = "访问清理TCP服务监控历史数据表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/tcp-clear")
    public ModelAndView tcpClear(Long id) {
        ModelAndView mv = new ModelAndView("tcp/tcp-clear-form");
        mv.addObject("id", id);
        return mv;
    }

    /**
     * <p>
     * 测试TCP连通性
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：TCP连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    @Operation(summary = "测试TCP连通性")
    @PostMapping("/test-monitor-tcp")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCP4SERVICE, operType = OperateTypeConstants.TEST, operDesc = "测试TCP连通性")
    public LayUiAdminResultVo testMonitorTcp(MonitorTcpVo monitorTcpVo) throws SigarException, IOException {
        return this.monitorTcpService.testMonitorTcp(monitorTcpVo);
    }

}