package com.imby.server.business.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 监控配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/3 12:57
 */
@Controller
@RequestMapping("/monitor-config")
@Api(tags = "配置.监控")
public class MonitorConfigController {

    /**
     * <p>
     * 访问监控配置页
     * </p>
     *
     * @return {@link ModelAndView} 监控配置页
     * @author 皮锋
     * @custom.date 2020/11/3 13:01
     */
    @ApiOperation(value = "访问监控配置页")
    @GetMapping("/config")
    public ModelAndView config() {
        return new ModelAndView("set/config/config");
    }
}
