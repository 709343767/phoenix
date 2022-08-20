package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRole;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorUser;
import com.gitee.pifeng.monitoring.ui.business.web.realm.MonitorUserRealm;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRoleService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorRoleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorUserVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 我的
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/7 17:40
 */
@Controller
@Api(tags = "我的")
@RequestMapping("/myself")
public class MyselfController {

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
     * 访问用户基本资料页面
     * </p>
     *
     * @return {@link ModelAndView} 用户基本资料页面
     * @author 皮锋
     * @custom.date 2020/7/7 17:42
     */
    @ApiOperation(value = "访问用户基本资料页面")
    @GetMapping("/info")
    public ModelAndView userInfo() {
        ModelAndView mv = new ModelAndView("myself/info");
        MonitorUserRealm userRealm = SpringSecurityUtils.getCurrentMonitorUserRealm();
        // 查询当前用户
        MonitorUser monitorUser = this.monitorUserService.getById(Objects.requireNonNull(userRealm).getId());
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
     * 访问修改密码页面
     * </p>
     *
     * @return {@link ModelAndView} 修改密码页面
     * @author 皮锋
     * @custom.date 2020/7/22 11:09
     */
    @ApiOperation(value = "访问修改密码页面")
    @GetMapping("/password")
    public ModelAndView password() {
        return new ModelAndView("myself/password");
    }

    /**
     * <p>
     * 修改密码
     * </p>
     *
     * @param oldPassword 原始密码
     * @param password    密码
     * @return LayUiAdmin响应对象：如果原始密码校验失败，LayUiAdminResultVo.data="verifyFail"；
     * 如果修改密码成功，LayUiAdminResultVo.data="success"；
     * 否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/7/11 15:22
     */
    @ApiOperation(value = "修改密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oldPassword", value = "原始密码", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "query", dataType = "string", dataTypeClass = String.class)})
    @PutMapping("/update-password")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.MYSELF + "#修改密码", operType = OperateTypeConstants.UPDATE, operDesc = "修改密码")
    public LayUiAdminResultVo updatePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                             @RequestParam(name = "password") String password) {
        return this.monitorUserService.updatePassword(oldPassword, password);
    }

    /**
     * <p>
     * 修改当前用户信息
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return LayUiAdmin响应对象：如果修改用户信息成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/7/13 18:55
     */
    @ApiOperation(value = "修改当前用户信息")
    @PutMapping("/update-user")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.MYSELF + "#基本资料", operType = OperateTypeConstants.UPDATE, operDesc = "修改当前用户信息")
    public LayUiAdminResultVo updateUser(MonitorUserVo monitorUserVo) {
        return this.monitorUserService.updateUser(monitorUserVo);
    }
}
