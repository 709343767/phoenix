package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorEnvVo;
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

import java.util.List;

/**
 * <p>
 * 环境管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Api(tags = "配置管理.环境管理")
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
    @ApiOperation(value = "访问环境列表页面")
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
    @ApiOperation(value = "获取环境列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "envName", value = "环境名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "envDesc", value = "环境描述", paramType = "query", dataType = "string")})
    @GetMapping("get-monitor-env-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#环境管理", operType = OperateTypeConstants.QUERY, operDesc = "获取环境列表")
    public LayUiAdminResultVo getMonitorEnvList(Long current, Long size, String envName, String envDesc) {
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
    @ApiOperation(value = "访问新增环境信息表单页面")
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
    @ApiOperation(value = "添加环境信息")
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
    @ApiOperation(value = "访问编辑环境信息表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "环境ID", required = true, paramType = "query", dataType = "long")})
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
    @ApiOperation(value = "编辑环境信息")
    @PutMapping("/edit-monitor-env")
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
     * @param monitorEnvVos 环境信息表现层对象
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 10:09
     */
    @ApiOperation(value = "删除环境信息")
    @DeleteMapping("/delete-monitor-env")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.CONFIG_MANAGE + "#环境管理", operType = OperateTypeConstants.DELETE, operDesc = "删除环境信息")
    public LayUiAdminResultVo deleteMonitorEnv(@RequestBody List<MonitorEnvVo> monitorEnvVos) {
        return this.monitorEnvService.deleteMonitorEnv(monitorEnvVos);
    }

}

