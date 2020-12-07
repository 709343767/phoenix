package com.gitee.pifeng.server.business.web.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.entity.MonitorAlarmDefinition;
import com.gitee.pifeng.server.business.web.service.IMonitorAlarmDefinitionService;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.business.web.vo.MonitorAlarmDefinitionVo;
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
 * 告警定义
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
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


    /**
     * <p>
     * 访问新增告警定义表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增告警定义表单页面
     * @author 皮锋
     * @custom.date 2020/8/7 11:00
     */
    @ApiOperation(value = "访问新增告警定义表单页面")
    @GetMapping("/add-monitor-alarm-definition-form")
    public ModelAndView addMonitorAlarmDefinitionForm() {
        return new ModelAndView("alarm/add-alarm-definition");
    }

    /**
     * <p>
     * 访问编辑告警定义表单页面
     * </p>
     *
     * @param id 告警ID
     * @return {@link ModelAndView} 编辑告警定义表单页面
     * @author 皮锋
     * @custom.date 2020/8/7 11:07
     */
    @ApiOperation(value = "访问编辑告警定义表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "告警ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/edit-monitor-alarm-definition-form")
    public ModelAndView editMonitorAlarmDefinitionForm(@RequestParam(name = "id") Long id) {
        MonitorAlarmDefinition monitorAlarmDefinition = this.monitorAlarmDefinitionService.getById(id);
        MonitorAlarmDefinitionVo monitorAlarmDefinitionVo = MonitorAlarmDefinitionVo.builder().build().convertFor(monitorAlarmDefinition);
        ModelAndView mv = new ModelAndView("alarm/edit-alarm-definition");
        mv.addObject("monitorAlarmDefinitionVo", monitorAlarmDefinitionVo);
        return mv;
    }

    /**
     * <p>
     * 添加告警定义
     * </p>
     *
     * @param monitorAlarmDefinitionVo 告警定义
     * @return layUiAdmin响应对象：如果数据库中已经有此告警定义，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 12:19
     */
    @ApiOperation(value = "添加告警定义")
    @PostMapping("/save-monitor-alarm-definition")
    @ResponseBody
    public LayUiAdminResultVo saveMonitorAlarmDefinition(MonitorAlarmDefinitionVo monitorAlarmDefinitionVo) {
        return this.monitorAlarmDefinitionService.saveMonitorAlarmDefinition(monitorAlarmDefinitionVo);
    }

    /**
     * <p>
     * 编辑告警定义
     * </p>
     *
     * @param monitorAlarmDefinitionVo 告警定义
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 15:24
     */
    @ApiOperation(value = "编辑告警定义")
    @PostMapping("/edit-monitor-alarm-definition")
    @ResponseBody
    public LayUiAdminResultVo editMonitorAlarmDefinition(MonitorAlarmDefinitionVo monitorAlarmDefinitionVo) {
        return this.monitorAlarmDefinitionService.editMonitorAlarmDefinition(monitorAlarmDefinitionVo);
    }

    /**
     * <p>
     * 删除告警定义
     * </p>
     *
     * @param monitorAlarmDefinitionVos 告警定义
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 15:34
     */
    @ApiOperation(value = "删除告警定义")
    @PostMapping("/delete-monitor-alarm-definition")
    @ResponseBody
    public LayUiAdminResultVo deleteMonitorAlarmDefinition(@RequestBody List<MonitorAlarmDefinitionVo> monitorAlarmDefinitionVos) {
        return this.monitorAlarmDefinitionService.deleteMonitorAlarmDefinition(monitorAlarmDefinitionVos);
    }

}

