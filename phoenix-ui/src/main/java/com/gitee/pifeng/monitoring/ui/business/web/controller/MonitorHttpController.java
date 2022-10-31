package com.gitee.pifeng.monitoring.ui.business.web.controller;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "HTTP")
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
    @ApiOperation(value = "访问HTTP列表页面")
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
    @ApiOperation(value = "获取HTTP列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "hostnameSource", value = "主机名（来源）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "urlTarget", value = "URL地址（目的地）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "method", value = "请求方法", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "monitorEnv", value = "监控环境", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "monitorGroup", value = "监控分组", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/get-monitor-http-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取HTTP列表")
    public LayUiAdminResultVo getMonitorHttpList(Long current, Long size, String hostnameSource, String urlTarget,
                                                 String method, Integer status, String monitorEnv, String monitorGroup) {
        Page<MonitorHttpVo> page = this.monitorHttpService.getMonitorHttpList(current, size, hostnameSource, urlTarget,
                method, status, monitorEnv, monitorGroup);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除HTTP
     * </p>
     *
     * @param monitorHttpVos HTTP信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    @ApiOperation(value = "删除HTTP")
    @DeleteMapping("/delete-monitor-http")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "删除HTTP")
    public LayUiAdminResultVo deleteMonitorHttp(@RequestBody List<MonitorHttpVo> monitorHttpVos) {
        return this.monitorHttpService.deleteMonitorHttp(monitorHttpVos);
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
    @ApiOperation(value = "访问新增HTTP信息表单页面")
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
    @ApiOperation(value = "添加HTTP信息")
    @PostMapping("/add-monitor-http")
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
    @ApiOperation(value = "访问编辑HTTP信息表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "HTTP ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
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
    @ApiOperation(value = "编辑HTTP信息")
    @PutMapping("/edit-monitor-http")
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
     * 访问平均时间页面
     * </p>
     *
     * @param id HTTP ID
     * @return {@link ModelAndView} 平均时间页面
     * @author 皮锋
     * @custom.date 2022/3/17 11:13
     */
    @ApiOperation(value = "访问平均时间页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "HTTP ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
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
    @ApiOperation(value = "访问清理HTTP服务监控历史数据表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "主键ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
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
    @ApiOperation(value = "访问HTTP服务详情页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "HTTP ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
    @GetMapping("/http-detail")
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.PAGE, operDesc = "访问HTTP服务详情页面")
    public ModelAndView httpDetail(Long id) {
        ModelAndView mv = new ModelAndView("http/http-detail");
        MonitorHttp monitorHttp = this.monitorHttpService.getById(id);
        MonitorHttpVo monitorHttpVo = MonitorHttpVo.builder().build().convertFor(monitorHttp);
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
    @ApiOperation(value = "测试HTTP连通性")
    @PostMapping("/test-monitor-http")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.TEST, operDesc = "测试HTTP连通性")
    public LayUiAdminResultVo testMonitorHttp(MonitorHttpVo monitorHttpVo) throws NetException, SigarException, IOException {
        return this.monitorHttpService.testMonitorHttp(monitorHttpVo);
    }

}