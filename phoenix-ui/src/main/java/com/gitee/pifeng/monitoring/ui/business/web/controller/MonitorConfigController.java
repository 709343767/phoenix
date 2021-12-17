package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorConfigService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorConfigPageFormVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        MonitorConfigPageFormVo monitorConfigPageForm = this.monitorConfigService.getMonitorConfigPageFormInfo();
        mv.addObject("monitorConfigPageForm", monitorConfigPageForm);
        return mv;
    }

    /**
     * <p>
     * 更新监控配置
     * </p>
     *
     * @param monitorConfigPageFormVo 监控配置页面表单对象
     * @return layUiAdmin响应对象：如果更新数据库成功，LayUiAdminResultVo.data="success"；<br>
     * 如果更新数据库成功，但是更新监控服务端配置失败，LayUiAdminResultVo.data="refreshFail"；<br>
     * 否则，LayUiAdminResultVo.data="fail"。
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/11/9 19:56
     */
    @ApiOperation(value = "更新监控配置")
    @ResponseBody
    @PutMapping("/update-monitor-config")
    @OperateLog(operModule = UiModuleConstants.SET + "#监控设置", operType = OperateTypeConstants.UPDATE, operDesc = "更新监控配置")
    public LayUiAdminResultVo updateMonitorConfig(MonitorConfigPageFormVo monitorConfigPageFormVo) throws NetException, SigarException {
        return this.monitorConfigService.update(monitorConfigPageFormVo);
    }

}
