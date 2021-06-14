package com.gitee.pifeng.monitoring.ui.business.web.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogExceptionService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogOperationVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Api(tags = "日志.操作日志")
@RestController
@RequestMapping("/monitor-log-operation")
public class MonitorLogOperationController {

    /**
     * 操作日志服务类
     */
    @Autowired
    private IMonitorLogOperationService monitorLogOperationService;

    /**
     * 异常日志服务类
     */
    @Autowired
    private IMonitorLogExceptionService monitorLogExceptionService;

    /**
     * <p>
     * 访问操作日志列表页面
     * </p>
     *
     * @return {@link ModelAndView} 操作日志列表页面
     * @author 皮锋
     * @custom.date 2021/6/14 21:18
     */
    @ApiOperation(value = "访问操作日志列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("log/log-operation");
    }

    /**
     * <p>
     * 获取操作日志列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/6/14 21:24
     */
    @ApiOperation(value = "获取操作日志列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/get-monitor-log-operation-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.LOG + "#操作日志", operType = OperateTypeConstants.QUERY, operDesc = "获取操作日志列表")
    public LayUiAdminResultVo getMonitorLogOperationList(Long current, Long size) {
        Page<MonitorLogOperationVo> page = this.monitorLogOperationService.getMonitorLogOperationList(current, size);
        return LayUiAdminResultVo.ok(page);
    }

}

