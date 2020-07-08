package com.imby.server.business.web.controller;

import com.imby.server.business.web.entity.MonitorUser;
import com.imby.server.business.web.realm.MonitorUserRealm;
import com.imby.server.business.web.service.IMonitorUserService;
import com.imby.server.business.web.vo.MonitorUserVo;
import com.imby.server.util.SpringSecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/7 17:40
 */
@Controller
@Api(tags = "用户")
@RequestMapping("/user")
public class UserController {

    /**
     * 监控用户服务类
     */
    @Autowired
    private IMonitorUserService monitorUserService;

    /**
     * <p>
     * 访问用户信息页面
     * </p>
     *
     * @return {@link ModelAndView} 用户信息页面
     * @author 皮锋
     * @custom.date 2020/7/7 17:42
     */
    @ApiOperation(value = "访问用户信息页面", notes = "访问用户信息页面")
    @GetMapping("/user-info")
    public ModelAndView userInfo() {
        ModelAndView mv = new ModelAndView("user/user-info");
        MonitorUserRealm userRealm = SpringSecurityUtils.getCurrentPrincipal();
        MonitorUser monitorUser = this.monitorUserService.getById(userRealm.getId());
        // 转换成监控用户表现层对象
        MonitorUserVo monitorUserVo = MonitorUserVo.builder().build().convertFor(monitorUser);
        mv.addObject("user", monitorUserVo);
        return mv;
    }
}
