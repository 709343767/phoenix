package com.gitee.pifeng.server.business.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.server.business.web.entity.*;
import com.gitee.pifeng.server.business.web.service.*;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.business.web.vo.MonitorConfigPageFormVo;
import com.gitee.pifeng.server.constant.WebResponseConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
@Api(tags = "配置.监控配置")
public class MonitorConfigController {

    /**
     * 监控配置服务类
     */
    @Autowired
    private IMonitorConfigService monitorConfigService;

    /**
     * 监控网络配置服务类
     */
    @Autowired
    private IMonitorConfigNetService monitorConfigNetService;

    /**
     * 监控告警配置服务类
     */
    @Autowired
    private IMonitorConfigAlarmService monitorConfigAlarmService;

    /**
     * 监控邮件告警配置服务类
     */
    @Autowired
    private IMonitorConfigAlarmMailService monitorConfigAlarmMailService;

    /**
     * 监控短信告警配置服务类
     */
    @Autowired
    private IMonitorConfigAlarmSmsService monitorConfigAlarmSmsService;

    /**
     * 监控服务器信息配置服务类
     */
    @Autowired
    private IMonitorConfigServerService monitorConfigServerService;

    /**
     * 监控服务器CPU信息配置服务类
     */
    @Autowired
    private IMonitorConfigServerCpuService monitorConfigServerCpuService;

    /**
     * 监控服务器磁盘信息配置服务类
     */
    @Autowired
    private IMonitorConfigServerDiskService monitorConfigServerDiskService;

    /**
     * 监控服务器内存信息配置服务类
     */
    @Autowired
    private IMonitorConfigServerMemoryService monitorConfigServerMemoryService;

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
        ModelAndView mv = new ModelAndView("set/config/config");
        MonitorConfig monitorConfig = this.monitorConfigService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigNet monitorConfigNet = this.monitorConfigNetService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarm monitorConfigAlarm = this.monitorConfigAlarmService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarmMail monitorConfigAlarmMail = this.monitorConfigAlarmMailService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarmSms monitorConfigAlarmSms = this.monitorConfigAlarmSmsService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigServer monitorConfigServer = this.monitorConfigServerService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigServerCpu monitorConfigServerCpu = this.monitorConfigServerCpuService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigServerDisk monitorConfigServerDisk = this.monitorConfigServerDiskService.getOne(new LambdaQueryWrapper<>());
        MonitorConfigServerMemory monitorConfigServerMemory = this.monitorConfigServerMemoryService.getOne(new LambdaQueryWrapper<>());
        mv.addObject(monitorConfig)
                .addObject(monitorConfigNet)
                .addObject(monitorConfigAlarm)
                .addObject(monitorConfigAlarmMail)
                .addObject(monitorConfigAlarmSms)
                .addObject(monitorConfigServer)
                .addObject(monitorConfigServerCpu)
                .addObject(monitorConfigServerDisk)
                .addObject(monitorConfigServerMemory);
        return mv;
    }

    /**
     * <p>
     * 更新监控配置
     * </p>
     *
     * @param monitorConfigPageFormVo 监控配置页面表单对象
     * @return layUiAdmin响应对象：如果更新监控配置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/11/9 19:56
     */
    @ApiOperation(value = "更新监控配置")
    @ResponseBody
    @PostMapping("/update-monitor-config")
    public LayUiAdminResultVo updateMonitorConfig(MonitorConfigPageFormVo monitorConfigPageFormVo) {
        boolean b = this.monitorConfigService.update(monitorConfigPageFormVo);
        if (b) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

}
