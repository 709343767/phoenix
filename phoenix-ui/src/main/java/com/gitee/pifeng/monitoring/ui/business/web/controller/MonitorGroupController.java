package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorGroupVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 分组管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Tag(name = "配置管理.分组管理")
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
    @Operation(summary = "访问分组列表页面")
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
    @Operation(summary = "获取分组列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "groupType", description = "分组类型", in = ParameterIn.QUERY),
            @Parameter(name = "groupName", description = "分组名称", in = ParameterIn.QUERY),
            @Parameter(name = "groupDesc", description = "分组描述", in = ParameterIn.QUERY)})
    @GetMapping("get-monitor-group-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#分组管理", operType = OperateTypeConstants.QUERY, operDesc = "获取分组列表")
    public LayUiAdminResultVo getMonitorGroupList(@RequestParam(value = "current") Long current,
                                                  @RequestParam(value = "size") Long size,
                                                  @RequestParam(value = "groupType", required = false) String groupType,
                                                  @RequestParam(value = "groupName", required = false) String groupName,
                                                  @RequestParam(value = "groupDesc", required = false) String groupDesc) {
        Page<MonitorGroupVo> page = this.monitorGroupService.getMonitorGroupList(current, size, groupType, groupName, groupDesc);
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
    @Operation(summary = "访问新增分组信息表单页面")
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
    @Operation(summary = "添加分组信息")
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
    @Operation(summary = "访问编辑分组信息表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "分组ID", required = true, in = ParameterIn.QUERY)})
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
    @Operation(summary = "编辑分组信息")
    @PutMapping("/edit-monitor-group")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#分组管理", operType = OperateTypeConstants.UPDATE, operDesc = "编辑分组信息")
    public LayUiAdminResultVo editMonitorGroup(MonitorGroupVo monitorGroupVo) {
        return this.monitorGroupService.editMonitorGroup(monitorGroupVo);
    }

    /**
     * <p>
     * 删除分组信息
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 12:25
     */
    @Operation(summary = "删除分组信息")
    @DeleteMapping("/delete-monitor-group")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#分组管理", operType = OperateTypeConstants.DELETE, operDesc = "删除分组信息")
    public LayUiAdminResultVo deleteMonitorGroup(@RequestBody List<Long> ids) {
        return this.monitorGroupService.deleteMonitorGroup(ids);
    }

}