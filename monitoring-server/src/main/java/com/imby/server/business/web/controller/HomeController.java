package com.imby.server.business.web.controller;

import com.imby.server.business.web.service.IMonitorAlarmRecordService;
import com.imby.server.business.web.service.IMonitorInstanceService;
import com.imby.server.business.web.service.IMonitorServerOsService;
import com.imby.server.business.web.vo.HomeAlarmRecordVo;
import com.imby.server.business.web.vo.HomeInstanceVo;
import com.imby.server.business.web.vo.HomeServerOsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 应用实例服务类
     */
    @Autowired
    private IMonitorInstanceService monitorInstanceService;

    /**
     * 服务器服务类
     */
    @Autowired
    private IMonitorServerOsService monitorServerOsService;

    /**
     * 告警记录服务类
     */
    @Autowired
    private IMonitorAlarmRecordService monitorAlarmRecordService;

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
        ModelAndView mv = new ModelAndView("home");
        HomeInstanceVo homeInstanceVo = this.monitorInstanceService.getHomeInstanceInfo();
        mv.addObject("homeInstanceVo", homeInstanceVo);
        HomeServerOsVo homeServerOsVo = this.monitorServerOsService.getHomeServerOsInfo();
        mv.addObject("homeServerOsVo", homeServerOsVo);
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordService.getHomeAlarmRecordInfo();
        mv.addObject("homeAlarmRecordVo", homeAlarmRecordVo);
        return mv;
    }
}
