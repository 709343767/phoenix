package com.imby.server.business.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * home页
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/3 15:11
 */
@Controller
@Api(tags = "home页")
public class HomeController {

    /**
     * <p>
     * 访问home页
     * </p>
     *
     * @return {@link ModelAndView} home页
     * @author 皮锋
     * @custom.date 2020/8/3 15:14
     */
    @ApiOperation(value = "访问home页")
    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
}
