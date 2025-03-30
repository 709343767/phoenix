package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNet;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorNetVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 网络
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/26 13:19
 */
@Controller
@RequestMapping("/monitor-network")
@Tag(name = "网络")
public class MonitorNetworkController {

    /**
     * 网络信息服务类
     */
    @Autowired
    private IMonitorNetService monitorNetService;

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
     * 访问网络列表页面
     * </p>
     *
     * @return {@link ModelAndView} 网络列表页面
     * @author 皮锋
     * @custom.date 2020/9/26 10:53
     */
    @Operation(summary = "访问网络列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("network/network");
        // 源IP
        mv.addObject("ipSource", this.monitorNetService.getSourceIp());
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
     * 获取网络列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param ipSource     IP地址（来源）
     * @param ipTarget     IP地址（目的地）
     * @param status       状态（0：网络不通，1：网络正常）
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 10:59
     */
    @Operation(summary = "获取网络列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "ipSource", description = "IP地址（来源）", in = ParameterIn.QUERY),
            @Parameter(name = "ipTarget", description = "IP地址（目的地）", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态（0：网络不通，1：网络正常）", in = ParameterIn.QUERY),
            @Parameter(name = "monitorEnv", description = "监控环境", in = ParameterIn.QUERY),
            @Parameter(name = "monitorGroup", description = "监控分组", in = ParameterIn.QUERY)})
    @GetMapping("/get-monitor-network-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.QUERY, operDesc = "获取网络列表")
    public LayUiAdminResultVo getMonitorNetList(@RequestParam(value = "current") Long current,
                                                @RequestParam(value = "size") Long size,
                                                @RequestParam(value = "ipSource", required = false) String ipSource,
                                                @RequestParam(value = "ipTarget", required = false) String ipTarget,
                                                @RequestParam(value = "status", required = false) String status,
                                                @RequestParam(value = "monitorEnv", required = false) String monitorEnv,
                                                @RequestParam(value = "monitorGroup", required = false) String monitorGroup) {
        Page<MonitorNetVo> page = this.monitorNetService.getMonitorNetList(current, size, ipSource, ipTarget, status, monitorEnv, monitorGroup);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除网络
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 12:59
     */
    @Operation(summary = "删除网络")
    @DeleteMapping("/delete-monitor-network")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.DELETE, operDesc = "删除网络")
    public LayUiAdminResultVo deleteMonitorNet(@RequestBody List<Long> ids) {
        return this.monitorNetService.deleteMonitorNet(ids);
    }

    /**
     * <p>
     * 访问编辑网络信息表单页面
     * </p>
     *
     * @param id 网络ID
     * @return {@link ModelAndView} 编辑网络信息表单页面
     * @author 皮锋
     * @custom.date 2020/11/20 9:17
     */
    @Operation(summary = "访问编辑网络信息表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "网络ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-network-form")
    public ModelAndView editMonitorNetworkForm(@RequestParam(name = "id") Long id) {
        MonitorNet monitorNet = this.monitorNetService.getById(id);
        MonitorNetVo monitorNetVo = MonitorNetVo.builder().build().convertFor(monitorNet);
        ModelAndView mv = new ModelAndView("network/edit-network");
        mv.addObject(monitorNetVo);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        mv.addObject("env", monitorNetVo.getMonitorEnv());
        mv.addObject("group", monitorNetVo.getMonitorGroup());
        return mv;
    }

    /**
     * <p>
     * 访问新增网络信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增网络信息表单页面
     * @author 皮锋
     * @custom.date 2020/11/20 14:54
     */
    @Operation(summary = "访问新增网络信息表单页面")
    @GetMapping("/add-monitor-network-form")
    public ModelAndView addMonitorNetworkForm() {
        ModelAndView mv = new ModelAndView("network/add-network");
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
     * 编辑网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/11/20 13:56
     */
    @Operation(summary = "编辑网络信息")
    @PutMapping("/edit-monitor-network")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.UPDATE, operDesc = "编辑网络信息")
    public LayUiAdminResultVo editMonitorNetwork(MonitorNetVo monitorNetVo) throws SigarException, IOException {
        LayUiAdminResultVo layUiAdminResultVo = this.monitorNetService.editMonitorNetwork(monitorNetVo);
        // 测试网络连通性
        this.monitorNetService.testMonitorNetwork(monitorNetVo);
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 添加网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/11/20 15:05
     */
    @Operation(summary = "添加网络信息")
    @PostMapping("/add-monitor-network")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.ADD, operDesc = "添加网络信息")
    public LayUiAdminResultVo addMonitorNetwork(MonitorNetVo monitorNetVo) throws NetException, SigarException, IOException {
        LayUiAdminResultVo layUiAdminResultVo = this.monitorNetService.addMonitorNetwork(monitorNetVo);
        // 测试网络连通性
        this.monitorNetService.testMonitorNetwork(monitorNetVo);
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
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启监控（0：不开启监控；1：开启监控）")
    public LayUiAdminResultVo setIsEnableMonitor(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "isEnableMonitor") String isEnableMonitor) {
        return this.monitorNetService.setIsEnableMonitor(id, isEnableMonitor);
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
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启告警（0：不开启告警；1：开启告警）")
    public LayUiAdminResultVo setIsEnableAlarm(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "isEnableAlarm") String isEnableAlarm) {
        return this.monitorNetService.setIsEnableAlarm(id, isEnableAlarm);
    }

    /**
     * <p>
     * 访问平均时间页面
     * </p>
     *
     * @param id 网络ID
     * @return {@link ModelAndView} 平均时间页面
     * @author 皮锋
     * @custom.date 2022/3/17 11:13
     */
    @Operation(summary = "访问平均时间页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "网络ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/avg-time")
    public ModelAndView avgTime(Long id) {
        MonitorNet monitorNet = this.monitorNetService.getById(id);
        MonitorNetVo monitorNetVo = MonitorNetVo.builder().build().convertFor(monitorNet);
        ModelAndView mv = new ModelAndView("network/avg-time");
        mv.addObject(monitorNetVo);
        return mv;
    }

    /**
     * <p>
     * 访问清理网络监控历史数据表单页面
     * </p>
     *
     * @param id 网络ID
     * @return {@link ModelAndView} 清理网络监控历史数据表单页面
     * @author 皮锋
     * @custom.date 2021/7/20 20:56
     */
    @Operation(summary = "访问清理网络监控历史数据表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "网络ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/network-clear")
    public ModelAndView networkClear(Long id) {
        ModelAndView mv = new ModelAndView("network/network-clear-form");
        mv.addObject("id", id);
        return mv;
    }

    /**
     * <p>
     * 测试网络连通性
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：网络连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    @Operation(summary = "测试网络连通性")
    @PostMapping("/test-monitor-network")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.NET, operType = OperateTypeConstants.TEST, operDesc = "测试网络连通性")
    public LayUiAdminResultVo testMonitorNetwork(MonitorNetVo monitorNetVo) throws SigarException, IOException {
        return this.monitorNetService.testMonitorNetwork(monitorNetVo);
    }

}
