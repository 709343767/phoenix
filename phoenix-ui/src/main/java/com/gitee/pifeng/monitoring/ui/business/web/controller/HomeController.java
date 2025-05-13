package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.*;
import com.gitee.pifeng.monitoring.ui.business.web.vo.*;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "主页")
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
     * 告警记录详情服务类
     */
    @Autowired
    private IMonitorAlarmRecordDetailService monitorAlarmRecordDetailService;

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
     * TCP信息服务类
     */
    @Autowired
    private IMonitorTcpService monitorTcpService;

    /**
     * HTTP信息服务类
     */
    @Autowired
    private IMonitorHttpService monitorHttpService;

    /**
     * <p>
     * 访问主页
     * </p>
     *
     * @return {@link ModelAndView} 主页
     * @author 皮锋
     * @custom.date 2020/8/3 15:14
     */
    @Operation(summary = "访问主页")
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
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordDetailService.getHomeAlarmRecordInfo();
        mv.addObject("homeAlarmRecordVo", homeAlarmRecordVo);
        HomeDbVo homeDbVo = this.monitorDbService.getHomeDbInfo();
        mv.addObject("homeDbVo", homeDbVo);
        HomeTcpVo homeTcpVo = this.monitorTcpService.getHomeTcpInfo();
        mv.addObject("homeTcpVo", homeTcpVo);
        HomeHttpVo homeHttpVo = this.monitorHttpService.getHomeHttpInfo();
        mv.addObject("homeHttpVo", homeHttpVo);
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
    @Operation(summary = "获取主页的摘要信息")
    @ResponseBody
    @PostMapping("/home/get-summary-info")
    public LayUiAdminResultVo getSummaryInfo() {
        HomeInstanceVo homeInstanceVo = this.monitorInstanceService.getHomeInstanceInfo();
        HomeNetVo homeNetVo = this.monitorNetService.getHomeNetInfo();
        HomeServerVo homeServerVo = this.monitorServerService.getHomeServerInfo();
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordDetailService.getHomeAlarmRecordInfo();
        HomeDbVo homeDbVo = this.monitorDbService.getHomeDbInfo();
        HomeTcpVo homeTcpVo = this.monitorTcpService.getHomeTcpInfo();
        HomeHttpVo homeHttpVo = this.monitorHttpService.getHomeHttpInfo();
        Map<String, Object> map = new HashMap<>(16);
        map.put("homeInstanceVo", homeInstanceVo);
        map.put("homeNetVo", homeNetVo);
        map.put("homeServerVo", homeServerVo);
        map.put("homeAlarmRecordVo", homeAlarmRecordVo);
        map.put("homeDbVo", homeDbVo);
        map.put("homeTcpVo", homeTcpVo);
        map.put("homeHttpVo", homeHttpVo);
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
    @Operation(summary = "获取最近7天的告警统计信息")
    @ResponseBody
    @PostMapping("/home/get-last-7-days-alarm-record-statistics")
    public LayUiAdminResultVo getLast7DaysAlarmRecordStatistics() {
        return this.monitorAlarmRecordDetailService.getLast7DaysAlarmRecordStatistics();
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
    @Operation(summary = "获取告警类型统计信息")
    @ResponseBody
    @PostMapping("/home/get-alarm-record-type-statistics")
    public LayUiAdminResultVo getAlarmRecordTypeStatistics() {
        return this.monitorAlarmRecordService.getAlarmRecordTypeStatistics();
    }

    /**
     * <p>
     * 获取告警结果统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/4/7 10:46
     */
    @Operation(summary = "获取告警结果统计信息")
    @ResponseBody
    @PostMapping("/home/get-alarm-record-result-statistics")
    public LayUiAdminResultVo getAlarmRecordResultStatistics() {
        HomeAlarmRecordVo homeAlarmRecordVo = this.monitorAlarmRecordDetailService.getHomeAlarmRecordInfo();
        return LayUiAdminResultVo.ok(homeAlarmRecordVo);
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
    @Operation(summary = "获取最新的5条告警记录")
    @ResponseBody
    @PostMapping("/home/get-last-5-alarm-record")
    public LayUiAdminResultVo getLast5AlarmRecord() {
        return this.monitorAlarmRecordService.getLast5AlarmRecord();
    }

}
