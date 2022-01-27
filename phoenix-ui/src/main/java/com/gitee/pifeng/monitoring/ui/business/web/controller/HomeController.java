package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.*;
import com.gitee.pifeng.monitoring.ui.business.web.vo.*;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 主页
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/3 15:11
 */
@Controller
@Api(tags = "主页")
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
     * 数据库表服务类
     */
    @Autowired
    private IMonitorDbService monitorDbService;

    /**
     * TCP/IP信息服务类
     */
    @Autowired
    private IMonitorTcpIpService monitorTcpIpService;

    /**
     * <p>
     * 访问主页
     * </p>
     *
     * @return {@link ModelAndView} 主页
     * @author 皮锋
     * @custom.date 2020/8/3 15:14
     */
    @ApiOperation(value = "访问主页")
    @GetMapping("/home")
    @OperateLog(operModule = UiModuleConstants.HOME, operType = OperateTypeConstants.PAGE, operDesc = "访问主页")
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
        HomeDbVo homeDbVo = this.monitorDbService.getHomeDbInfo();
        mv.addObject("homeDbVo", homeDbVo);
        HomeTcpIpVo homeTcpIpVo = this.monitorTcpIpService.getHomeTcpIpInfo();
        mv.addObject("homeTcpIpVo", homeTcpIpVo);
        return mv;
    }

    /**
     * <p>
     * 获取主页的摘要信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/13 14:31
     */
    @ApiOperation(value = "获取主页的摘要信息")
    @ResponseBody
    @PostMapping("/home/get-summary-info")
    public LayUiAdminResultVo getSummaryInfo() {
        HomeInstanceVo homeInstanceVo = this.monitorInstanceService.getHomeInstanceInfo();
        HomeNetVo homeNetVo = this.monitorNetService.getHomeNetInfo();
        HomeServerVo homeServerVo = this.monitorServerService.getHomeServerInfo();
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordService.getHomeAlarmRecordInfo();
        HomeDbVo homeDbVo = this.monitorDbService.getHomeDbInfo();
        HomeTcpIpVo homeTcpIpVo = this.monitorTcpIpService.getHomeTcpIpInfo();
        Map<String, Object> map = new HashMap<>(16);
        map.put("homeInstanceVo", homeInstanceVo);
        map.put("homeNetVo", homeNetVo);
        map.put("homeServerVo", homeServerVo);
        map.put("homeAlarmRecordVo", homeAlarmRecordVo);
        map.put("homeDbVo", homeDbVo);
        map.put("homeTcpIpVo", homeTcpIpVo);
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
        return this.monitorAlarmRecordService.getLast5AlarmRecord();
    }

}
