package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogOperationVo;
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
 * 操作日志
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Tag(name = "日志.操作日志")
@RestController
@RequestMapping("/monitor-log-operation")
public class MonitorLogOperationController {

    /**
     * 操作日志服务类
     */
    @Autowired
    private IMonitorLogOperationService monitorLogOperationService;

    /**
     * <p>
     * 访问操作日志列表页面
     * </p>
     *
     * @return {@link ModelAndView} 操作日志列表页面
     * @author 皮锋
     * @custom.date 2021/6/14 21:18
     */
    @Operation(summary = "访问操作日志列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("log/log-operation");
    }

    /**
     * <p>
     * 获取操作日志列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param operModule 功能模块
     * @param operDesc   操作描述
     * @param operType   操作类型
     * @param username   操作用户
     * @param ip         请求IP
     * @param insertTime 插入时间
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/6/14 21:24
     */
    @Operation(summary = "获取操作日志列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "operModule", description = "功能模块", in = ParameterIn.QUERY),
            @Parameter(name = "operDesc", description = "操作描述", in = ParameterIn.QUERY),
            @Parameter(name = "operType", description = "操作类型", in = ParameterIn.QUERY),
            @Parameter(name = "username", description = "操作用户", in = ParameterIn.QUERY),
            @Parameter(name = "ip", description = "请求IP", in = ParameterIn.QUERY),
            @Parameter(name = "insertTime", description = "插入时间", in = ParameterIn.QUERY)
    })
    @GetMapping("/get-monitor-log-operation-list")
    @ResponseBody
    // @OperateLog(operModule = UiModuleConstants.LOG + "#操作日志", operType = OperateTypeConstants.QUERY, operDesc = "获取操作日志列表")
    public LayUiAdminResultVo getMonitorLogOperationList(@RequestParam(value = "current") Long current,
                                                         @RequestParam(value = "size") Long size,
                                                         @RequestParam(value = "operModule", required = false) String operModule,
                                                         @RequestParam(value = "operDesc", required = false) String operDesc,
                                                         @RequestParam(value = "operType", required = false) String operType,
                                                         @RequestParam(value = "username", required = false) String username,
                                                         @RequestParam(value = "ip", required = false) String ip,
                                                         @RequestParam(value = "insertTime", required = false) String insertTime) {
        Page<MonitorLogOperationVo> page = this.monitorLogOperationService.getMonitorLogOperationList(current, size, operModule, operDesc, operType, username, ip, insertTime);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除操作日志
     * </p>
     *
     * @param monitorLogOperationVos 操作日志信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/15 14:30
     */
    @Operation(summary = "删除操作日志")
    @DeleteMapping("/delete-monitor-log-operation")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.LOG + "#操作日志", operType = OperateTypeConstants.DELETE, operDesc = "删除操作日志")
    public LayUiAdminResultVo deleteMonitorLogOperation(@RequestBody List<MonitorLogOperationVo> monitorLogOperationVos) {
        return this.monitorLogOperationService.deleteMonitorLogOperation(monitorLogOperationVos);
    }

    /**
     * <p>
     * 清空操作日志
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 15:38
     */
    @Operation(summary = "清空操作日志")
    @DeleteMapping("/cleanup-monitor-log-operation")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.LOG + "#操作日志", operType = OperateTypeConstants.DELETE, operDesc = "清空操作日志")
    public LayUiAdminResultVo cleanupMonitorLogOperation() {
        return this.monitorLogOperationService.cleanupMonitorLogOperation();
    }

    /**
     * <p>
     * 访问操作日志详情页面
     * </p>
     *
     * @param id 操作日志ID
     * @return {@link ModelAndView} 操作日志详情页面
     * @author 皮锋
     * @custom.date 2021/6/18 17:20
     */
    @Operation(summary = "访问操作日志详情页面")
    @GetMapping("/monitor-log-operation-detail")
    @Parameters(value = {
            @Parameter(name = "id", description = "操作日志ID", required = true, in = ParameterIn.QUERY)})
    @OperateLog(operModule = UiModuleConstants.LOG + "#操作日志", operType = OperateTypeConstants.PAGE, operDesc = "访问操作日志详情页面")
    public ModelAndView monitorLogOperationDetail(Long id) {
        ModelAndView mv = new ModelAndView("log/log-operation-detail");
        MonitorLogOperationVo monitorLogOperationInfo = this.monitorLogOperationService.getMonitorLogOperationInfo(id);
        mv.addObject("monitorLogOperationInfo", monitorLogOperationInfo);
        return mv;
    }

}