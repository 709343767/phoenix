package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "登录")
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
    @Operation(summary = "访问登录页面")
    @GetMapping("/login")
    @OperateLog(operModule = UiModuleConstants.LOGIN, operType = OperateTypeConstants.PAGE, operDesc = "访问登录页面")
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
    @Operation(summary = "登录成功重定向到首页")
    @GetMapping("/login-success")
    @OperateLog(operModule = UiModuleConstants.LOGIN, operType = OperateTypeConstants.LOGIN, operDesc = "用户成功登录")
    public String loginSuccess() {
        return "redirect:index";
    }

    /**
     * <p>
     * 退出登录成功重定向到首页
     * </p>
     *
     * @return 重定向首页URL
     * @author 皮锋
     * @custom.date 2021/9/30 21:10
     */
    @Operation(summary = "退出登录成功重定向到首页")
    @GetMapping("/logout-success")
    @OperateLog(operModule = UiModuleConstants.LOGOUT, operType = OperateTypeConstants.LOGOUT, operDesc = "退出登录成功重定向到首页")
    public String logoutSuccess() {
        return "redirect:index";
    }

}
