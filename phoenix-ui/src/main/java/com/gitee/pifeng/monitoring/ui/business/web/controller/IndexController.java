package com.gitee.pifeng.monitoring.ui.business.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 首页
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/5/16 20:18
 */
@Controller
@Tag(name = "首页")
public class IndexController {

    /**
     * <p>
     * 访问首页
     * </p>
     *
     * @return {@link ModelAndView} 首页
     * @author 皮锋
     * @custom.date 2020/5/16 20:25
     */
    @Operation(summary = "访问首页")
    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

}
