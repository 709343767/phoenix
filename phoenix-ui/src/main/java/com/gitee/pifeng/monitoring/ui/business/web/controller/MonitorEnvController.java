package com.gitee.pifeng.monitoring.ui.business.web.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 监控环境管理
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Api(tags = "监控环境管理")
@Controller
@RequestMapping("/monitor-env")
public class MonitorEnvController {

    /**
     * <p>
     * 访问监控环境列表页面
     * </p>
     *
     * @return {@link ModelAndView} 监控环境列表页面
     * @author 皮锋
     * @custom.date 2021/12/23 17:53
     */
    @ApiOperation(value = "访问监控环境列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("set/env");
    }

}

