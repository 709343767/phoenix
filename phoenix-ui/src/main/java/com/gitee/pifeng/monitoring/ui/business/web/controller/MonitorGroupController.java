package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorGroupVo;
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

/**
 * <p>
 * 分组管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Api(tags = "配置管理.分组管理")
@Controller
@RequestMapping("/monitor-group")
public class MonitorGroupController {

    /**
     * 分组服务类
     */
    @Autowired
    private IMonitorGroupService monitorGroupService;

    /**
     * <p>
     * 访问分组列表页面
     * </p>
     *
     * @return {@link ModelAndView} 分组列表页面
     * @author 皮锋
     * @custom.date 2021/12/23 17:53
     */
    @ApiOperation(value = "访问分组列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("set/group");
    }

    /**
     * <p>
     * 获取分组列表
     * </p>
     *
     * @param current   当前页
     * @param size      每页显示条数
     * @param groupName 分组名称
     * @param groupDesc 分组描述
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/12/24 9:40
     */
    @ApiOperation(value = "获取分组列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "groupName", value = "分组名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "groupDesc", value = "分组描述", paramType = "query", dataType = "string")})
    @GetMapping("get-monitor-group-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#分组管理", operType = OperateTypeConstants.QUERY, operDesc = "获取分组列表")
    public LayUiAdminResultVo getMonitorGroupList(Long current, Long size, String groupName, String groupDesc) {
        Page<MonitorGroupVo> page = this.monitorGroupService.getMonitorGroupList(current, size, groupName, groupDesc);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 访问新增分组信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增分组信息表单页面
     * @author 皮锋
     * @custom.date 2021/12/24 10:19
     */
    @ApiOperation(value = "访问新增分组信息表单页面")
    @GetMapping("/add-monitor-group-form")
    public ModelAndView addMonitorGroupForm() {
        return new ModelAndView("set/add-group");
    }

    /**
     * <p>
     * 添加分组信息
     * </p>
     *
     * @param monitorGroupVo 分组信息表现层对象
     * @return layUiAdmin响应对象：如果已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 10:30
     */
    @ApiOperation(value = "添加分组信息")
    @PostMapping("/save-monitor-group")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#分组管理", operType = OperateTypeConstants.ADD, operDesc = "添加分组信息")
    public LayUiAdminResultVo saveMonitorGroup(MonitorGroupVo monitorGroupVo) {
        return this.monitorGroupService.saveMonitorGroup(monitorGroupVo);
    }

    /**
     * <p>
     * 访问编辑分组信息表单页面
     * </p>
     *
     * @param id 分组ID
     * @return {@link ModelAndView} 编辑分组信息表单页面
     * @author 皮锋
     * @custom.date 2021/12/24 10:58
     */
    @ApiOperation(value = "访问编辑分组信息表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "分组ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/edit-monitor-group-form")
    public ModelAndView editMonitorGroupForm(@RequestParam(name = "id") Long id) {
        MonitorGroup monitorGroup = this.monitorGroupService.getById(id);
        MonitorGroupVo monitorGroupVo = MonitorGroupVo.builder().build().convertFor(monitorGroup);
        ModelAndView mv = new ModelAndView("set/edit-group");
        mv.addObject(monitorGroupVo);
        return mv;
    }

    /**
     * <p>
     * 编辑分组信息
     * </p>
     *
     * @param monitorGroupVo 分组信息表现层对象
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 11:07
     */
    @ApiOperation(value = "编辑分组信息")
    @PutMapping("/edit-monitor-group")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#分组管理", operType = OperateTypeConstants.UPDATE, operDesc = "编辑分组信息")
    public LayUiAdminResultVo editMonitorGroup(MonitorGroupVo monitorGroupVo) {
        return this.monitorGroupService.editMonitorGroup(monitorGroupVo);
    }

}