package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIp;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpIpService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpIpVo;
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

/**
 * <p>
 * TCP/IP信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
@Controller
@RequestMapping("/monitor-tcpip")
@Api(tags = "TCPIP")
public class MonitorTcpIpController {

    /**
     * 网络信息服务类
     */
    @Autowired
    private IMonitorNetService monitorNetService;

    /**
     * TCP/IP信息服务类
     */
    @Autowired
    private IMonitorTcpIpService monitorTcpIpService;

    /**
     * <p>
     * 访问TCP/IP列表页面
     * </p>
     *
     * @return {@link ModelAndView} TCP/IP列表页面
     * @author 皮锋
     * @custom.date 2022/1/11 9:27
     */
    @ApiOperation(value = "访问TCP/IP列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("tcpip/tcpip");
        // 源IP
        mv.addObject("ipSource", this.monitorNetService.getSourceIp());
        return mv;
    }

    /**
     * <p>
     * 获取TCP/IP列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param ipSource   IP地址（来源）
     * @param ipTarget   IP地址（目的地）
     * @param portTarget 目标端口
     * @param protocol   协议
     * @param status     状态（0：不通，1：正常）
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/1/11 9:31
     */
    @ApiOperation(value = "获取TCP/IP列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "ipSource", value = "IP地址（来源）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "ipTarget", value = "IP地址（目的地）", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "portTarget", value = "目标端口", paramType = "query", dataType = "int", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "protocol", value = "协议", paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "状态（0：不通，1：正常）", paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @GetMapping("/get-monitor-tcpip-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCPIP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取TCP/IP列表")
    public LayUiAdminResultVo getMonitorTcpIpList(Long current, Long size, String ipSource, String ipTarget, Integer portTarget, String protocol, String status) {
        Page<MonitorTcpIpVo> page = this.monitorTcpIpService.getMonitorTcpIpList(current, size, ipSource, ipTarget, portTarget, protocol, status);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除TCP/IP
     * </p>
     *
     * @param monitorTcpIpVos TCP/IP信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    @ApiOperation(value = "删除TCP/IP")
    @DeleteMapping("/delete-monitor-tcpip")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCPIP4SERVICE, operType = OperateTypeConstants.DELETE, operDesc = "删除TCP/IP")
    public LayUiAdminResultVo deleteMonitorTcpIp(@RequestBody List<MonitorTcpIpVo> monitorTcpIpVos) {
        return this.monitorTcpIpService.deleteMonitorTcpIp(monitorTcpIpVos);
    }

    /**
     * <p>
     * 访问新增TCP/IP信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增TCP/IP信息表单页面
     * @author 皮锋
     * @custom.date 2022/1/11 10:52
     */
    @ApiOperation(value = "访问新增TCP/IP信息表单页面")
    @GetMapping("/add-monitor-tcpip-form")
    public ModelAndView addMonitorTcpIpForm() {
        return new ModelAndView("tcpip/add-tcpip");
    }

    /**
     * <p>
     * 添加TCP/IP信息
     * </p>
     *
     * @param monitorTcpIpVo TCP/IP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:16
     */
    @ApiOperation(value = "添加TCP/IP信息")
    @PostMapping("/add-monitor-tcpip")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCPIP4SERVICE, operType = OperateTypeConstants.ADD, operDesc = "添加TCP/IP信息")
    public LayUiAdminResultVo addMonitorTcpIp(MonitorTcpIpVo monitorTcpIpVo) throws NetException {
        // 获取被监控TCP/IP源IP地址，获取失败则返回null
        String sourceIp = this.monitorNetService.getSourceIp();
        monitorTcpIpVo.setIpSource(sourceIp);
        return this.monitorTcpIpService.addMonitorTcpIp(monitorTcpIpVo);
    }

    /**
     * <p>
     * 访问编辑TCP/IP信息表单页面
     * </p>
     *
     * @param id TCP/IP ID
     * @return {@link ModelAndView} 编辑TCP/IP信息表单页面
     * @author 皮锋
     * @custom.date 2022/1/11 11:20
     */
    @ApiOperation(value = "访问编辑TCP/IP信息表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "TCP/IP ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
    @GetMapping("/edit-monitor-tcpip-form")
    public ModelAndView editMonitorTcpIpForm(@RequestParam(name = "id") Long id) {
        MonitorTcpIp monitorTcpIp = this.monitorTcpIpService.getById(id);
        MonitorTcpIpVo monitorTcpIpVo = MonitorTcpIpVo.builder().build().convertFor(monitorTcpIp);
        ModelAndView mv = new ModelAndView("tcpip/edit-tcpip");
        mv.addObject(monitorTcpIpVo);
        return mv;
    }

    /**
     * <p>
     * 编辑TCP/IP信息
     * </p>
     *
     * @param monitorTcpIpVo TCP/IP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 12:26
     */
    @ApiOperation(value = "编辑TCP/IP信息")
    @PutMapping("/edit-monitor-tcpip")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.TCPIP4SERVICE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑TCP/IP信息")
    public LayUiAdminResultVo editMonitorTcpIp(MonitorTcpIpVo monitorTcpIpVo) {
        // 获取被监控TCP/IP源IP地址，获取失败则返回null
        String sourceIp = this.monitorNetService.getSourceIp();
        monitorTcpIpVo.setIpSource(sourceIp);
        return this.monitorTcpIpService.editMonitorTcpIp(monitorTcpIpVo);
    }

    /**
     * <p>
     * 访问平均时间页面
     * </p>
     *
     * @param id TCP/IP ID
     * @return {@link ModelAndView} 平均时间页面
     * @author 皮锋
     * @custom.date 2022/3/17 11:13
     */
    @ApiOperation(value = "访问平均时间页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "TCP/IP ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
    @GetMapping("/avg-time")
    public ModelAndView avgTime(Long id) {
        MonitorTcpIp monitorTcpIp = this.monitorTcpIpService.getById(id);
        MonitorTcpIpVo monitorTcpIpVo = MonitorTcpIpVo.builder().build().convertFor(monitorTcpIp);
        ModelAndView mv = new ModelAndView("tcpip/avg-time");
        mv.addObject(monitorTcpIpVo);
        return mv;
    }

}