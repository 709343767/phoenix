package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 服务器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/2 21:50
 */
@Controller
@RequestMapping("/monitor-server")
@Api(tags = "服务器")
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
    @ApiOperation(value = "访问服务器列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("server/server");
        // 监控配置
        MonitorConfigPageFormVo monitorConfigPageFormInfo = this.monitorConfigService.getMonitorConfigPageFormInfo();
        mv.addObject("monitorConfigPageFormInfo", monitorConfigPageFormInfo);
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
     * 获取服务器列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param ip           IP
     * @param isOnline     状态
     * @param serverName   服务器名
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/4 12:34
     */
    @ApiOperation(value = "获取服务器列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "ip", value = "IP", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "serverName", value = "服务器名", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "isOnline", value = "状态", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "monitorEnv", value = "监控环境", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "monitorGroup", value = "监控分组", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/get-monitor-server-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.QUERY, operDesc = "获取服务器列表")
    public LayUiAdminResultVo getMonitorServerList(Long current, Long size, String ip, String serverName, String isOnline, String monitorEnv, String monitorGroup) {
        Page<MonitorServerVo> page = this.monitorServerService.getMonitorServerList(current, size, ip, serverName, isOnline, monitorEnv, monitorGroup);
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
    @ApiOperation(value = "删除服务器")
    @DeleteMapping("/delete-monitor-server")
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
    @ApiOperation(value = "清理服务器监控历史数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "time", value = "时间", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @DeleteMapping("/clear-monitor-server-history")
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
    @ApiOperation(value = "访问服务器详情页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "服务器主键ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "ip", value = "服务器IP", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/server-detail")
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.PAGE, operDesc = "访问服务器详情页面")
    public ModelAndView serverDetail(Long id, String ip) {
        ModelAndView mv = new ModelAndView("server/server-detail");
        mv.addObject("id", id);
        mv.addObject("ip", ip);
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
    @ApiOperation(value = "访问清理服务器监控历史数据表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
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
    @ApiOperation(value = "访问服务器编辑页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "服务器主键ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "ip", value = "服务器IP", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/edit-monitor-server-form")
    public ModelAndView editMonitorServerForm(Long id, String ip) {
        ModelAndView mv = new ModelAndView("server/edit-server");
        mv.addObject("id", id);
        mv.addObject("ip", ip);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        // 服务器信息
        MonitorServerVo monitorServerVo = this.monitorServerService.getMonitorServerInfo(id, ip);
        mv.addObject("serverSummary", monitorServerVo.getServerSummary());
        mv.addObject("env", monitorServerVo.getMonitorEnv());
        mv.addObject("group", monitorServerVo.getMonitorGroup());
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
    @ApiOperation(value = "编辑服务器信息")
    @PutMapping("/edit-monitor-server")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.SERVER, operType = OperateTypeConstants.UPDATE, operDesc = "编辑服务器信息")
    public LayUiAdminResultVo editMonitorServer(MonitorServerVo monitorServerVo) {
        return this.monitorServerService.editMonitorServer(monitorServerVo);
    }

}
