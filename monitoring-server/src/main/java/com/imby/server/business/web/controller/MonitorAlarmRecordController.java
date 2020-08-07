package com.imby.server.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imby.server.business.web.service.IMonitorAlarmRecordService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorAlarmRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 告警记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/7 15:53
 */
@Controller
@Api(tags = "告警记录")
@RequestMapping("/monitor-alarm-record")
public class MonitorAlarmRecordController {

    /**
     * 告警记录服务类
     */
    @Autowired
    private IMonitorAlarmRecordService monitorAlarmRecordService;

    /**
     * <p>
     * 访问告警记录列表页面
     * </p>
     *
     * @return {@link ModelAndView} 告警记录列表页面
     * @author 皮锋
     * @custom.date 2020/8/7 15:56
     */
    @ApiOperation(value = "访问告警记录列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("alarm/alarm-record");
    }

    /**
     * <p>
     * 获取告警记录列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param type    告警类型
     * @param level   告警级别
     * @param title   告警标题
     * @param content 告警内容
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/8/7 16:12
     */
    @ApiOperation(value = "获取告警记录列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "type", value = "告警类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "告警级别", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "告警标题", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "告警内容", paramType = "query", dataType = "string")})
    @GetMapping("/get-monitor-alarm-record-list")
    @ResponseBody
    public LayUiAdminResultVo getMonitorAlarmRecordList(Long current, Long size, String type, String level, String title, String content) {
        Page<MonitorAlarmRecordVo> page = this.monitorAlarmRecordService.getMonitorAlarmRecordList(current, size, type, level, title, content);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除告警记录
     * </p>
     *
     * @param monitorAlarmRecordVos 告警记录
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 16:58
     */
    @ApiOperation(value = "删除告警记录")
    @PostMapping("/delete-monitor-alarm-record")
    @ResponseBody
    public LayUiAdminResultVo deleteMonitorAlarmRecord(@RequestBody List<MonitorAlarmRecordVo> monitorAlarmRecordVos) {
        return this.monitorAlarmRecordService.deleteMonitorAlarmRecord(monitorAlarmRecordVos);
    }
}
