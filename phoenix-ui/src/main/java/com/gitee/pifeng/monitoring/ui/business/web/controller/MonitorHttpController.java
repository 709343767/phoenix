package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "int", dataTypeClass = Integer.class)})
    @GetMapping("/get-monitor-http-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.HTTP4SERVICE, operType = OperateTypeConstants.QUERY, operDesc = "获取HTTP列表")
    public LayUiAdminResultVo getMonitorHttpList(Long current, Long size, String hostnameSource, String urlTarget, String method, Integer status) {
        Page<MonitorHttpVo> page = this.monitorHttpService.getMonitorHttpList(current, size, hostnameSource, urlTarget, method, status);
        return LayUiAdminResultVo.ok(page);
    }

}
