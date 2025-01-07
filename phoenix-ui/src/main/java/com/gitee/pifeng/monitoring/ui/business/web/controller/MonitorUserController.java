package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NotFoundUserException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRole;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorUser;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRoleService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorRoleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorUserVo;
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

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 用户管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/23 14:44
 */
@Controller
@Tag(name = "用户管理.用户")
@RequestMapping("/user")
public class MonitorUserController {

    /**
     * 监控用户服务类
     */
    @Autowired
    private IMonitorUserService monitorUserService;

    /**
     * 监控用户角色服务类
     */
    @Autowired
    private IMonitorRoleService monitorRoleService;

    /**
     * <p>
     * 访问用户列表页面
     * </p>
     *
     * @return {@link ModelAndView} 用户列表页面
     * @author 皮锋
     * @custom.date 2020/7/23 14:46
     */
    @Operation(summary = "访问用户列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("user/user");
    }

    /**
     * <p>
     * 获取监控用户列表
     * </p>
     *
     * @param current  当前页
     * @param size     每页显示条数
     * @param account  账号
     * @param username 用户名
     * @param email    电子邮箱
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/7/23 16:25
     */
    @Operation(summary = "获取监控用户列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "account", description = "账号", in = ParameterIn.QUERY),
            @Parameter(name = "username", description = "用户名", in = ParameterIn.QUERY),
            @Parameter(name = "email", description = "电子邮箱", in = ParameterIn.QUERY)})
    @GetMapping("/get-monitor-user-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.USER_MANAGE + "#用户", operType = OperateTypeConstants.QUERY, operDesc = "获取监控用户列表")
    public LayUiAdminResultVo getMonitorUserList(@RequestParam(value = "current") Long current,
                                                 @RequestParam(value = "size") Long size,
                                                 @RequestParam(value = "account", required = false) String account,
                                                 @RequestParam(value = "username", required = false) String username,
                                                 @RequestParam(value = "email", required = false) String email) {
        Page<MonitorUserVo> page = this.monitorUserService.getMonitorUserList(current, size, account, username, email);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 访问新增用户表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增用户表单页面
     * @author 皮锋
     * @custom.date 2020/8/1 18:49
     */
    @Operation(summary = "访问新增用户表单页面")
    @GetMapping("/add-user-form")
    public ModelAndView addUserForm() {
        ModelAndView mv = new ModelAndView("user/add-user");
        // 查询角色列表
        List<MonitorRole> monitorRoles = this.monitorRoleService.list();
        // 转换成监控用户角色表现层对象
        List<MonitorRoleVo> monitorRoleVos = new LinkedList<>();
        for (MonitorRole monitorRole : monitorRoles) {
            MonitorRoleVo monitorRoleVo = MonitorRoleVo.builder().build().convertFor(monitorRole);
            monitorRoleVos.add(monitorRoleVo);
        }
        mv.addObject("roles", monitorRoleVos);
        return mv;
    }

    /**
     * <p>
     * 访问编辑用户表单页面
     * </p>
     *
     * @param userId 用户ID
     * @return {@link ModelAndView} 编辑用户表单页面
     * @author 皮锋
     * @custom.date 2020/8/2 20:20
     */
    @Operation(summary = "访问编辑用户表单页面")
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-user-form")
    public ModelAndView editUserForm(@RequestParam(name = "userId") Long userId) {
        ModelAndView mv = new ModelAndView("user/edit-user");
        // 查询当前用户
        MonitorUser monitorUser = this.monitorUserService.getById(userId);
        // 转换成监控用户表现层对象
        MonitorUserVo monitorUserVo = MonitorUserVo.builder().build().convertFor(monitorUser);
        mv.addObject("user", monitorUserVo);
        // 查询角色列表
        List<MonitorRole> monitorRoles = this.monitorRoleService.list();
        // 转换成监控用户角色表现层对象
        List<MonitorRoleVo> monitorRoleVos = new LinkedList<>();
        for (MonitorRole monitorRole : monitorRoles) {
            MonitorRoleVo monitorRoleVo = MonitorRoleVo.builder().build().convertFor(monitorRole);
            monitorRoleVos.add(monitorRoleVo);
        }
        mv.addObject("roles", monitorRoleVos);
        return mv;
    }

    /**
     * <p>
     * 添加用户
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return layUiAdmin响应对象：如果数据库中已经有此账号，LayUiAdminResultVo.data="exist"；
     * 如果添加用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/1 21:19
     */
    @Operation(summary = "添加用户")
    @PostMapping("/save-user")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.USER_MANAGE + "#用户", operType = OperateTypeConstants.ADD, operDesc = "添加用户")
    public LayUiAdminResultVo saveUser(MonitorUserVo monitorUserVo) {
        return this.monitorUserService.saveUser(monitorUserVo);
    }

    /**
     * <p>
     * 编辑用户
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return layUiAdmin响应对象：如果编辑用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/2 20:43
     */
    @Operation(summary = "编辑用户")
    @PutMapping("/edit-user")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.USER_MANAGE + "#用户", operType = OperateTypeConstants.UPDATE, operDesc = "编辑用户")
    public LayUiAdminResultVo editUser(MonitorUserVo monitorUserVo) {
        return this.monitorUserService.editUser(monitorUserVo);
    }

    /**
     * <p>
     * 删除用户
     * </p>
     *
     * @param ids 用户ID集合
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NotFoundUserException 找不到用户异常
     * @author 皮锋
     * @custom.date 2020/8/2 16:43
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/delete-user")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.USER_MANAGE + "#用户", operType = OperateTypeConstants.DELETE, operDesc = "删除用户")
    public LayUiAdminResultVo deleteUser(@RequestBody List<Long> ids) throws NotFoundUserException {
        return this.monitorUserService.deleteUser(ids);
    }

}
