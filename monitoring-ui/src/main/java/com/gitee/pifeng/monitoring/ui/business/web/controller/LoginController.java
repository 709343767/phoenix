package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 登录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/15 8:57
 */
@Controller
@Api(tags = "登录")
public class LoginController {

    /**
     * <p>
     * 访问登录页面
     * </p>
     *
     * @return {@link ModelAndView} 登录页面
     * @author 皮锋
     * @custom.date 2020/5/15 9:12
     */
    @ApiOperation(value = "访问登录页面")
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("user/login");
    }

    /**
     * <p>
     * 登录成功重定向到首页
     * </p>
     *
     * @return 重定向首页URL
     * @author 皮锋
     * @custom.date 2021/7/18 21:09
     */
    @ApiOperation(value = "登录成功重定向到首页")
    @GetMapping("/login-success")
    @OperateLog(operModule = UiModuleConstants.LOGIN, operType = OperateTypeConstants.LOGIN, operDesc = "用户成功登录")
    public String loginSuccess() {
        return "redirect:index";
    }

}
