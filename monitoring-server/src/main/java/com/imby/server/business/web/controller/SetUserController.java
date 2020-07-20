package com.imby.server.business.web.controller;

import com.imby.server.business.web.entity.MonitorRole;
import com.imby.server.business.web.entity.MonitorUser;
import com.imby.server.business.web.realm.MonitorUserRealm;
import com.imby.server.business.web.service.IMonitorRoleService;
import com.imby.server.business.web.service.IMonitorUserService;
import com.imby.server.business.web.vo.MonitorRoleVo;
import com.imby.server.business.web.vo.MonitorUserVo;
import com.imby.server.util.SpringSecurityUtils;
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

/**
 * <p>
 * 我的设置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/7 17:40
 */
@Controller
@Api(tags = "我的设置")
@RequestMapping("/set/user")
public class SetUserController {

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
        ModelAndView mv = new ModelAndView("set/user/info");
        MonitorUserRealm userRealm = SpringSecurityUtils.getCurrentMonitorUserRealm();
        // 查询当前用户
        MonitorUser monitorUser = this.monitorUserService.getById(userRealm.getId());
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
     * 校验密码是否正确
     * </p>
     *
     * @param password 密码
     * @return 密码是否校验成功
     * @author 皮锋
     * @custom.date 2020/7/8 16:52
     */
    @ApiOperation(value = "校验密码是否正确")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oldPassword", value = "密码", required = true, paramType = "query", dataType = "string")})
    @PostMapping("/verify-password")
    @ResponseBody
    public boolean verifyPassword(@RequestParam(name = "oldPassword") String password) {
        return this.monitorUserService.verifyPassword(password);
    }

    /**
     * <p>
     * 修改密码
     * </p>
     *
     * @param password 密码
     * @return 密码是否修改成功
     * @author 皮锋
     * @custom.date 2020/7/11 15:22
     */
    @ApiOperation(value = "修改密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "newPassword", value = "密码", required = true, paramType = "update", dataType = "string")})
    @PostMapping("/update-password")
    @ResponseBody
    public boolean updatePassword(@RequestParam(name = "newPassword") String password) {
        return this.monitorUserService.updatePassword(password);
    }

    /**
     * <p>
     * 修改用户信息
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return 用户信息是否修改成功
     * @author 皮锋
     * @custom.date 2020/7/13 18:55
     */
    @ApiOperation(value = "修改用户信息")
    @PostMapping("/update-user")
    @ResponseBody
    public boolean updateUser(MonitorUserVo monitorUserVo) {
        return this.monitorUserService.updateUser(monitorUserVo);
    }
}
