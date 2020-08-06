package com.imby.server.business.web.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imby.server.business.web.service.IMonitorAlarmDefinitionService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorAlarmDefinitionVo;
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
 * 告警定义
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-06
 */
@Controller
@Api(tags = "告警定义")
@RequestMapping("/monitor-alarm-definition")
public class MonitorAlarmDefinitionController {

    /**
     * 告警定义服务类
     */
    @Autowired
    private IMonitorAlarmDefinitionService monitorAlarmDefinitionService;


    /**
     * <p>
     * 访问告警定义列表页面
     * </p>
     *
     * @return {@link ModelAndView} 告警定义列表页面
     * @author 皮锋
     * @custom.date 2020/8/6 20:13
     */
    @ApiOperation(value = "访问告警定义列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("alarm/alarm-definition");
    }

    /**
     * <p>
     * 获取告警定义列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param type    告警类型
     * @param grade   告警级别
     * @param title   告警标题
     * @param content 告警内容
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/8/6 20:18
     */
    @ApiOperation(value = "获取告警定义列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "type", value = "告警类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "grade", value = "告警级别", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "告警标题", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "告警内容", paramType = "query", dataType = "string")})
    @ResponseBody
    @GetMapping("/get-monitor-alarm-definition-list")
    public LayUiAdminResultVo getMonitorAlarmDefinitionList(Long current, Long size, String type, String grade, String title, String content) {
        Page<MonitorAlarmDefinitionVo> page = this.monitorAlarmDefinitionService.getMonitorAlarmDefinitionList(current, size, type, grade, title, content);
        return LayUiAdminResultVo.ok(page);
    }

}

