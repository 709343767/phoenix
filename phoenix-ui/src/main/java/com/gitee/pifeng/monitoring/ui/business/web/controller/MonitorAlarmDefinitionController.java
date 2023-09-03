package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmDefinition;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmDefinitionService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmDefinitionVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "配置管理.告警定义")
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
    @Operation(summary = "访问告警定义列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("set/alarm-definition");
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
    @Operation(summary = "获取告警定义列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "告警类型", in = ParameterIn.QUERY),
            @Parameter(name = "grade", description = "告警级别", in = ParameterIn.QUERY),
            @Parameter(name = "title", description = "告警标题", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "告警内容", in = ParameterIn.QUERY)})
    @ResponseBody
    @GetMapping("/get-monitor-alarm-definition-list")
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#告警定义", operType = OperateTypeConstants.QUERY, operDesc = "获取告警定义列表")
    public LayUiAdminResultVo getMonitorAlarmDefinitionList(@RequestParam(value = "current") Long current,
                                                            @RequestParam(value = "size") Long size,
                                                            @RequestParam(value = "type", required = false) String type,
                                                            @RequestParam(value = "grade", required = false) String grade,
                                                            @RequestParam(value = "title", required = false) String title,
                                                            @RequestParam(value = "content", required = false) String content) {
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
    @Operation(summary = "访问新增告警定义表单页面")
    @GetMapping("/add-monitor-alarm-definition-form")
    public ModelAndView addMonitorAlarmDefinitionForm() {
        return new ModelAndView("set/add-alarm-definition");
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
    @Operation(summary = "访问编辑告警定义表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "告警ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-alarm-definition-form")
    public ModelAndView editMonitorAlarmDefinitionForm(@RequestParam(name = "id") Long id) {
        MonitorAlarmDefinition monitorAlarmDefinition = this.monitorAlarmDefinitionService.getById(id);
        MonitorAlarmDefinitionVo monitorAlarmDefinitionVo = MonitorAlarmDefinitionVo.builder().build().convertFor(monitorAlarmDefinition);
        ModelAndView mv = new ModelAndView("set/edit-alarm-definition");
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
    @Operation(summary = "添加告警定义")
    @PostMapping("/save-monitor-alarm-definition")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#告警定义", operType = OperateTypeConstants.ADD, operDesc = "添加告警定义")
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
    @Operation(summary = "编辑告警定义")
    @PutMapping("/edit-monitor-alarm-definition")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#告警定义", operType = OperateTypeConstants.UPDATE, operDesc = "编辑告警定义")
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
    @Operation(summary = "删除告警定义")
    @DeleteMapping("/delete-monitor-alarm-definition")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#告警定义", operType = OperateTypeConstants.DELETE, operDesc = "删除告警定义")
    public LayUiAdminResultVo deleteMonitorAlarmDefinition(@RequestBody List<MonitorAlarmDefinitionVo> monitorAlarmDefinitionVos) {
        return this.monitorAlarmDefinitionService.deleteMonitorAlarmDefinition(monitorAlarmDefinitionVos);
    }

}

