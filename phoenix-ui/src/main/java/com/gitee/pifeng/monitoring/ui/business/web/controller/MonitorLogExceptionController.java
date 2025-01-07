package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogExceptionService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogExceptionVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 异常日志
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Tag(name = "日志.异常日志")
@RestController
@RequestMapping("/monitor-log-exception")
public class MonitorLogExceptionController {

    @Autowired
    private IMonitorLogExceptionService monitorLogExceptionService;

    /**
     * <p>
     * 访问异常日志列表页面
     * </p>
     *
     * @return {@link ModelAndView} 异常日志列表页面
     * @author 皮锋
     * @custom.date 2021/6/18 8:55
     */
    @Operation(summary = "访问异常日志列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("log/log-exception");
    }

    /**
     * <p>
     * 获取异常日志列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param instanceId   应用实例ID
     * @param instanceName 应用实例名
     * @param excName      异常名称
     * @param excMessage   异常信息
     * @param operMethod   操作方法
     * @param uri          请求URI
     * @param ip           请求IP
     * @param insertTime   插入时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/6/18 12:48
     */
    @Operation(summary = "获取异常日志列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "instanceId", description = "应用实例ID", in = ParameterIn.QUERY),
            @Parameter(name = "instanceName", description = "应用实例名", in = ParameterIn.QUERY),
            @Parameter(name = "excName", description = "异常名称", in = ParameterIn.QUERY),
            @Parameter(name = "excMessage", description = "异常信息", in = ParameterIn.QUERY),
            @Parameter(name = "operMethod", description = "操作方法", in = ParameterIn.QUERY),
            @Parameter(name = "uri", description = "请求URI", in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "请求IP", in = ParameterIn.QUERY),
            @Parameter(name = "insertTime", description = "插入时间", in = ParameterIn.QUERY)
    })
    @GetMapping("/get-monitor-log-exception-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.LOG + "#异常日志", operType = OperateTypeConstants.QUERY, operDesc = "获取异常日志列表")
    public LayUiAdminResultVo getMonitorLogExceptionList(@RequestParam(value = "current") Long current,
                                                         @RequestParam(value = "size") Long size,
                                                         @RequestParam(value = "instanceId", required = false) String instanceId,
                                                         @RequestParam(value = "instanceName", required = false) String instanceName,
                                                         @RequestParam(value = "excName", required = false) String excName,
                                                         @RequestParam(value = "excMessage", required = false) String excMessage,
                                                         @RequestParam(value = "operMethod", required = false) String operMethod,
                                                         @RequestParam(value = "uri", required = false) String uri,
                                                         @RequestParam(value = "ip", required = false) String ip,
                                                         @RequestParam(value = "insertTime", required = false) String insertTime) {
        Page<MonitorLogExceptionVo> page = this.monitorLogExceptionService.getMonitorLogExceptionList(current, size, instanceId,
                instanceName, excName, excMessage, operMethod, uri, ip, insertTime);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除异常日志
     * </p>
     *
     * @param ids 异常日志ID列表
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/18 12:36
     */
    @Operation(summary = "删除异常日志")
    @DeleteMapping("/delete-monitor-log-exception")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.LOG + "#异常日志", operType = OperateTypeConstants.DELETE, operDesc = "删除异常日志")
    public LayUiAdminResultVo deleteMonitorLogException(@RequestBody List<Long> ids) {
        return this.monitorLogExceptionService.deleteMonitorLogException(ids);
    }

    /**
     * <p>
     * 清空异常日志
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 15:20
     */
    @Operation(summary = "清空异常日志")
    @DeleteMapping("/cleanup-monitor-log-exception")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.LOG + "#异常日志", operType = OperateTypeConstants.DELETE, operDesc = "清空异常日志")
    public LayUiAdminResultVo cleanupMonitorLogException() {
        return this.monitorLogExceptionService.cleanupMonitorLogException();
    }

    /**
     * <p>
     * 访问异常日志详情页面
     * </p>
     *
     * @param id 异常日志ID
     * @return {@link ModelAndView} 异常日志详情页面
     * @author 皮锋
     * @custom.date 2021/6/18 17:20
     */
    @Operation(summary = "访问异常日志详情页面")
    @GetMapping("/monitor-log-exception-detail")
    @Parameters(value = {
            @Parameter(name = "id", description = "异常日志ID", required = true, in = ParameterIn.QUERY)})
    @OperateLog(operModule = UiModuleConstants.LOG + "#异常日志", operType = OperateTypeConstants.PAGE, operDesc = "访问异常日志详情页面")
    public ModelAndView monitorLogExceptionDetail(Long id) {
        ModelAndView mv = new ModelAndView("log/log-exception-detail");
        MonitorLogExceptionVo monitorLogExceptionInfo = this.monitorLogExceptionService.getMonitorLogExceptionInfo(id);
        mv.addObject("monitorLogExceptionInfo", monitorLogExceptionInfo);
        return mv;
    }

}

