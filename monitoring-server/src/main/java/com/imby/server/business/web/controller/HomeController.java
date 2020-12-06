package com.imby.server.business.web.controller;

import com.imby.server.business.web.service.IMonitorAlarmRecordService;
import com.imby.server.business.web.service.IMonitorInstanceService;
import com.imby.server.business.web.service.IMonitorNetService;
import com.imby.server.business.web.service.IMonitorServerService;
import com.imby.server.business.web.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IMonitorServerService monitorServerService;

    /**
     * 告警记录服务类
     */
    @Autowired
    private IMonitorAlarmRecordService monitorAlarmRecordService;

    /**
     * 网络信息服务类
     */
    @Autowired
    private IMonitorNetService monitorNetService;

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
        HomeNetVo homeNetVo = this.monitorNetService.getHomeNetInfo();
        mv.addObject("homeNetVo", homeNetVo);
        HomeServerVo homeServerVo = this.monitorServerService.getHomeServerInfo();
        mv.addObject("homeServerVo", homeServerVo);
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordService.getHomeAlarmRecordInfo();
        mv.addObject("homeAlarmRecordVo", homeAlarmRecordVo);
        return mv;
    }

    /**
     * <p>
     * 获取home页的摘要信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/13 14:31
     */
    @ApiOperation(value = "获取home页的摘要信息")
    @ResponseBody
    @PostMapping("/home/get-summary-info")
    public LayUiAdminResultVo getSummaryInfo() {
        HomeInstanceVo homeInstanceVo = this.monitorInstanceService.getHomeInstanceInfo();
        HomeNetVo homeNetVo = this.monitorNetService.getHomeNetInfo();
        HomeServerVo homeServerVo = this.monitorServerService.getHomeServerInfo();
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordService.getHomeAlarmRecordInfo();
        Map<String, Object> map = new HashMap<>(16);
        map.put("homeInstanceVo", homeInstanceVo);
        map.put("homeNetVo", homeNetVo);
        map.put("homeServerVo", homeServerVo);
        map.put("homeAlarmRecordVo", homeAlarmRecordVo);
        return LayUiAdminResultVo.ok(map);
    }

    /**
     * <p>
     * 获取最近7天的告警统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/18 10:20
     */
    @ApiOperation(value = "获取最近7天的告警统计信息")
    @ResponseBody
    @PostMapping("/home/get-last-7-days-alarm-record-statistics")
    public LayUiAdminResultVo getLast7DaysAlarmRecordStatistics() {
        return this.monitorAlarmRecordService.getLast7DaysAlarmRecordStatistics();
    }

    /**
     * <p>
     * 获取告警类型统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/23 9:51
     */
    @ApiOperation(value = "获取告警类型统计信息")
    @ResponseBody
    @PostMapping("/home/get-alarm-record-type-statistics")
    public LayUiAdminResultVo getAlarmRecordTypeStatistics() {
        return this.monitorAlarmRecordService.getAlarmRecordTypeStatistics();
    }

    /**
     * <p>
     * 获取最新的5条告警记录
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/24 16:35
     */
    @ApiOperation(value = "获取最新的5条告警记录")
    @ResponseBody
    @PostMapping("/home/get-last-5-alarm-record")
    public LayUiAdminResultVo getLast5AlarmRecord() {
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = this.monitorAlarmRecordService.getMonitorAlarmRecordList(1L, 5L, null, null, null, null, null).getRecords();
        return LayUiAdminResultVo.ok(monitorAlarmRecordVos);
    }

}
