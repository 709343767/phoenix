package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorEnvVo;
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
 * 环境管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Tag(name = "配置管理.环境管理")
@Controller
@RequestMapping("/monitor-env")
public class MonitorEnvController {

    /**
     * 环境服务类
     */
    @Autowired
    private IMonitorEnvService monitorEnvService;

    /**
     * <p>
     * 访问环境列表页面
     * </p>
     *
     * @return {@link ModelAndView} 环境列表页面
     * @author 皮锋
     * @custom.date 2021/12/23 17:53
     */
    @Operation(summary = "访问环境列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("set/env");
    }

    /**
     * <p>
     * 获取环境列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param envName 环境名称
     * @param envDesc 环境描述
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/12/24 9:40
     */
    @Operation(summary = "获取环境列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "envName", description = "环境名称", in = ParameterIn.QUERY),
            @Parameter(name = "envDesc", description = "环境描述", in = ParameterIn.QUERY)})
    @GetMapping("get-monitor-env-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#环境管理", operType = OperateTypeConstants.QUERY, operDesc = "获取环境列表")
    public LayUiAdminResultVo getMonitorEnvList(@RequestParam(value = "current") Long current,
                                                @RequestParam(value = "size") Long size,
                                                @RequestParam(value = "envName", required = false) String envName,
                                                @RequestParam(value = "envDesc", required = false) String envDesc) {
        Page<MonitorEnvVo> page = this.monitorEnvService.getMonitorEnvList(current, size, envName, envDesc);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 访问新增环境信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增环境信息表单页面
     * @author 皮锋
     * @custom.date 2021/12/24 10:19
     */
    @Operation(summary = "访问新增环境信息表单页面")
    @GetMapping("/add-monitor-env-form")
    public ModelAndView addMonitorEnvForm() {
        return new ModelAndView("set/add-env");
    }

    /**
     * <p>
     * 添加环境信息
     * </p>
     *
     * @param monitorEnvVo 环境信息表现层对象
     * @return layUiAdmin响应对象：如果已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 10:30
     */
    @Operation(summary = "添加环境信息")
    @PostMapping("/save-monitor-env")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#环境管理", operType = OperateTypeConstants.ADD, operDesc = "添加环境信息")
    public LayUiAdminResultVo saveMonitorEnv(MonitorEnvVo monitorEnvVo) {
        return this.monitorEnvService.saveMonitorEnv(monitorEnvVo);
    }

    /**
     * <p>
     * 访问编辑环境信息表单页面
     * </p>
     *
     * @param id 环境ID
     * @return {@link ModelAndView} 编辑环境信息表单页面
     * @author 皮锋
     * @custom.date 2021/12/24 10:58
     */
    @Operation(summary = "访问编辑环境信息表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "环境ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-env-form")
    public ModelAndView editMonitorEnvForm(@RequestParam(name = "id") Long id) {
        MonitorEnv monitorEnv = this.monitorEnvService.getById(id);
        MonitorEnvVo monitorEnvVo = MonitorEnvVo.builder().build().convertFor(monitorEnv);
        ModelAndView mv = new ModelAndView("set/edit-env");
        mv.addObject(monitorEnvVo);
        return mv;
    }

    /**
     * <p>
     * 编辑环境信息
     * </p>
     *
     * @param monitorEnvVo 环境信息表现层对象
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 11:07
     */
    @Operation(summary = "编辑环境信息")
    @PutMapping("/edit-monitor-env")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#环境管理", operType = OperateTypeConstants.UPDATE, operDesc = "编辑环境信息")
    public LayUiAdminResultVo editMonitorEnv(MonitorEnvVo monitorEnvVo) {
        return this.monitorEnvService.editMonitorEnv(monitorEnvVo);
    }

    /**
     * <p>
     * 删除环境信息
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 10:09
     */
    @Operation(summary = "删除环境信息")
    @DeleteMapping("/delete-monitor-env")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#环境管理", operType = OperateTypeConstants.DELETE, operDesc = "删除环境信息")
    public LayUiAdminResultVo deleteMonitorEnv(@RequestBody List<Long> ids) {
        return this.monitorEnvService.deleteMonitorEnv(ids);
    }

}

