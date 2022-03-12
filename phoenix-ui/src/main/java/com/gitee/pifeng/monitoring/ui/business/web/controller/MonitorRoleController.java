package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRole;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRoleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorRoleVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
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

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 角色管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/3 10:46
 */
@Api(tags = "用户管理.角色")
@Controller
@RequestMapping("/role")
public class MonitorRoleController {

    /**
     * 监控用户角色服务类
     */
    @Autowired
    private IMonitorRoleService monitorRoleService;

    /**
     * <p>
     * 访问角色列表页面
     * </p>
     *
     * @return {@link ModelAndView} 角色列表页面
     * @author 皮锋
     * @custom.date 2020/7/23 14:46
     */
    @ApiOperation(value = "访问角色列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("user/role");
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
     * 获取监控角色列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param roleId  角色ID
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:05
     */
    @ApiOperation(value = "获取监控角色列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "roleId", value = "角色ID", paramType = "query", dataType = "long", dataTypeClass = Long.class)})
    @GetMapping("/get-monitor-role-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.USER_MANAGE + "#角色", operType = OperateTypeConstants.QUERY, operDesc = "获取监控角色列表")
    public LayUiAdminResultVo getMonitorRoleList(Long current, Long size, Long roleId) {
        Page<MonitorRoleVo> page = this.monitorRoleService.getMonitorRoleList(current, size, roleId);
        return LayUiAdminResultVo.ok(page);
    }


}
