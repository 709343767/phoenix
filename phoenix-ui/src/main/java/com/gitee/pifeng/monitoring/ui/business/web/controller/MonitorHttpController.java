package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorHttpService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorHttpVo;
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
 * HTTP信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/11 9:56
 */
@Controller
@RequestMapping("/monitor-http")
@Tag(name = "HTTP")
public class MonitorHttpController {

    /**
     * 网络信息服务类
     */
    @Autowired
    private IMonitorNetService monitorNetService;

    /**
     * HTTP信息服务类
     */
    @Autowired
    private IMonitorHttpService monitorHttpService;

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
     * 访问HTTP列表页面
     * </p>
     *
     * @return {@link ModelAndView} HTTP列表页面
     * @author 皮锋
     * @custom.date 2022/1/11 9:27
     */
    @Operation(summary = "访问HTTP列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("http/http");
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
     * 获取HTTP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param status         状态
     * @param monitorEnv     监控环境
     * @param monitorGroup   监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/1/11 9:31
     */
    @Operation(summary = "获取HTTP列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "hostnameSource", description = "主机名（来源）", in = ParameterIn.QUERY),
            @Parameter(name = "urlTarget", description = "URL地址（目的地）", in = ParameterIn.QUERY),
            @Parameter(name = "method", description = "请求方法", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态", in = ParameterIn.QUERY),
            @Parameter(name = "monitorEnv", description = "监控环境", in = ParameterIn.QUERY),
            @Parameter(name = "monitorGroup", description = "监控分组", in = ParameterIn.QUERY)})
    @GetMapping("/get-monitor-http-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取HTTP列表")
    public LayUiAdminResultVo getMonitorHttpList(@RequestParam(value = "current") Long current,
                                                 @RequestParam(value = "size") Long size,
                                                 @RequestParam(value = "hostnameSource", required = false) String hostnameSource,
                                                 @RequestParam(value = "urlTarget", required = false) String urlTarget,
                                                 @RequestParam(value = "method", required = false) String method,
                                                 @RequestParam(value = "status", required = false) Integer status,
                                                 @RequestParam(value = "monitorEnv", required = false) String monitorEnv,
                                                 @RequestParam(value = "monitorGroup", required = false) String monitorGroup) {
        Page<MonitorHttpVo> page = this.monitorHttpService.getMonitorHttpList(current, size, hostnameSource, urlTarget,
                method, status, monitorEnv, monitorGroup);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除HTTP
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    @Operation(summary = "删除HTTP")
    @DeleteMapping("/delete-monitor-http")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "删除HTTP")
    public LayUiAdminResultVo deleteMonitorHttp(@RequestBody List<Long> ids) {
        return this.monitorHttpService.deleteMonitorHttp(ids);
    }

    /**
     * <p>
     * 访问新增HTTP信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增HTTP信息表单页面
     * @author 皮锋
     * @custom.date 2022/1/11 10:52
     */
    @Operation(summary = "访问新增HTTP信息表单页面")
    @GetMapping("/add-monitor-http-form")
    public ModelAndView addMonitorHttpForm() {
        ModelAndView mv = new ModelAndView("http/add-http");
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
     * 添加HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/1/11 10:16
     */
    @Operation(summary = "添加HTTP信息")
    @PostMapping("/add-monitor-http")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.ADD, operDesc = "添加HTTP信息")
    public LayUiAdminResultVo addMonitorHttp(MonitorHttpVo monitorHttpVo) throws NetException, SigarException, IOException {
        // 获取被监控HTTP源IP地址，获取失败则返回null
        String sourceIp = this.monitorNetService.getSourceIp();
        monitorHttpVo.setHostnameSource(sourceIp);
        LayUiAdminResultVo layUiAdminResultVo = this.monitorHttpService.addMonitorHttp(monitorHttpVo);
        // 测试HTTP连通性
        this.monitorHttpService.testMonitorHttp(monitorHttpVo);
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 访问编辑HTTP信息表单页面
     * </p>
     *
     * @param id HTTP ID
     * @return {@link ModelAndView} 编辑HTTP信息表单页面
     * @author 皮锋
     * @custom.date 2022/1/11 11:20
     */
    @Operation(summary = "访问编辑HTTP信息表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "HTTP ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-http-form")
    public ModelAndView editMonitorHttpForm(@RequestParam(name = "id") Long id) {
        MonitorHttp monitorHttp = this.monitorHttpService.getById(id);
        MonitorHttpVo monitorHttpVo = MonitorHttpVo.builder().build().convertFor(monitorHttp);
        ModelAndView mv = new ModelAndView("http/edit-http");
        mv.addObject(monitorHttpVo);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        mv.addObject("env", monitorHttpVo.getMonitorEnv());
        mv.addObject("group", monitorHttpVo.getMonitorGroup());
        JSONObject bodyParameterJson = JSON.parseObject(monitorHttpVo.getBodyParameter());
        mv.addObject("bodyApplicationJsonParameter", bodyParameterJson != null ? bodyParameterJson.get("bodyApplicationJsonParameter") : null);
        return mv;
    }

    /**
     * <p>
     * 编辑HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/1/11 12:26
     */
    @Operation(summary = "编辑HTTP信息")
    @PutMapping("/edit-monitor-http")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑HTTP信息")
    public LayUiAdminResultVo editMonitorHttp(MonitorHttpVo monitorHttpVo) throws SigarException, IOException {
        // 获取被监控HTTP源IP地址，获取失败则返回null
        String sourceIp = this.monitorNetService.getSourceIp();
        monitorHttpVo.setHostnameSource(sourceIp);
        LayUiAdminResultVo layUiAdminResultVo = this.monitorHttpService.editMonitorHttp(monitorHttpVo);
        // 测试HTTP连通性
        this.monitorHttpService.testMonitorHttp(monitorHttpVo);
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
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启监控（0：不开启监控；1：开启监控）")
    public LayUiAdminResultVo setIsEnableMonitor(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "isEnableMonitor") String isEnableMonitor) {
        return this.monitorHttpService.setIsEnableMonitor(id, isEnableMonitor);
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
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启告警（0：不开启告警；1：开启告警）")
    public LayUiAdminResultVo setIsEnableAlarm(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "isEnableAlarm") String isEnableAlarm) {
        return this.monitorHttpService.setIsEnableAlarm(id, isEnableAlarm);
    }

    /**
     * <p>
     * 访问平均时间页面
     * </p>
     *
     * @param id HTTP ID
     * @return {@link ModelAndView} 平均时间页面
     * @author 皮锋
     * @custom.date 2022/3/17 11:13
     */
    @Operation(summary = "访问平均时间页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "HTTP ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/avg-time")
    public ModelAndView avgTime(Long id) {
        MonitorHttp monitorHttp = this.monitorHttpService.getById(id);
        MonitorHttpVo monitorHttpVo = MonitorHttpVo.builder().build().convertFor(monitorHttp);
        ModelAndView mv = new ModelAndView("http/avg-time");
        mv.addObject(monitorHttpVo);
        return mv;
    }

    /**
     * <p>
     * 访问清理HTTP服务监控历史数据表单页面
     * </p>
     *
     * @param id 主键ID
     * @return {@link ModelAndView} 清理HTTP服务监控历史数据表单页面
     * @author 皮锋
     * @custom.date 2021/7/20 20:56
     */
    @Operation(summary = "访问清理HTTP服务监控历史数据表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/http-clear")
    public ModelAndView httpClear(Long id) {
        ModelAndView mv = new ModelAndView("http/http-clear-form");
        mv.addObject("id", id);
        return mv;
    }

    /**
     * <p>
     * 访问HTTP服务详情页面
     * </p>
     *
     * @param id HTTP ID
     * @return {@link ModelAndView} HTTP服务详情页面
     * @author 皮锋
     * @custom.date 2022/4/23 11:52
     */
    @Operation(summary = "访问HTTP服务详情页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "HTTP ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/http-detail")
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.PAGE, operDesc = "访问HTTP服务详情页面")
    public ModelAndView httpDetail(Long id) {
        ModelAndView mv = new ModelAndView("http/http-detail");
        MonitorHttpVo monitorHttpVo = this.monitorHttpService.getMonitorHttpVoById(id);
        mv.addObject("monitorHttpVo", monitorHttpVo);
        return mv;
    }

    /**
     * <p>
     * 测试HTTP连通性
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：HTTP连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    @Operation(summary = "测试HTTP连通性")
    @PostMapping("/test-monitor-http")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.TEST, operDesc = "测试HTTP连通性")
    public LayUiAdminResultVo testMonitorHttp(MonitorHttpVo monitorHttpVo) throws NetException, SigarException, IOException {
        return this.monitorHttpService.testMonitorHttp(monitorHttpVo);
    }

}