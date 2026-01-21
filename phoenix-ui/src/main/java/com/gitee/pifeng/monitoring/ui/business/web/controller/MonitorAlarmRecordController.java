package com.gitee.pifeng.monitoring.ui.business.web.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.web.util.ContextUtils;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmRecordDetailService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmRecordService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordDetailVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import com.gitee.pifeng.monitoring.ui.core.CustomExcelExportStyler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 告警记录
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/7 15:53
 */
@Controller
@Tag(name = "告警记录")
@RequestMapping("/monitor-alarm-record")
public class MonitorAlarmRecordController {

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
     * <p>
     * 访问告警记录列表页面
     * </p>
     *
     * @return {@link ModelAndView} 告警记录列表页面
     * @author 皮锋
     * @custom.date 2020/8/7 15:56
     */
    @Operation(summary = "访问告警记录列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("alarm/alarm-record");
    }

    /**
     * <p>
     * 获取告警记录列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param type       告警类型
     * @param level      告警级别
     * @param way        告警方式
     * @param status     告警状态（0：失败；1：成功）
     * @param title      告警标题
     * @param content    告警内容
     * @param insertDate 记录日期
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/8/7 16:12
     */
    @Operation(summary = "获取告警记录列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "type", description = "告警类型", in = ParameterIn.QUERY),
            @Parameter(name = "level", description = "告警级别", in = ParameterIn.QUERY),
            @Parameter(name = "way", description = "告警方式", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "告警状态（0：失败；1：成功）", in = ParameterIn.QUERY),
            @Parameter(name = "title", description = "告警标题", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "告警内容", in = ParameterIn.QUERY),
            @Parameter(name = "insertDate", description = "记录日期", in = ParameterIn.QUERY)})
    @GetMapping("/get-monitor-alarm-record-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.ALARM, operType = OperateTypeConstants.QUERY, operDesc = "获取告警记录列表")
    public LayUiAdminResultVo getMonitorAlarmRecordList(@RequestParam(value = "current") Long current,
                                                        @RequestParam(value = "size") Long size,
                                                        @RequestParam(value = "type", required = false) String type,
                                                        @RequestParam(value = "level", required = false) String level,
                                                        @RequestParam(value = "way", required = false) String way,
                                                        @RequestParam(value = "status", required = false) String status,
                                                        @RequestParam(value = "title", required = false) String title,
                                                        @RequestParam(value = "content", required = false) String content,
                                                        @RequestParam(value = "insertDate", required = false) String insertDate) {
        Page<MonitorAlarmRecordVo> page = this.monitorAlarmRecordService.getMonitorAlarmRecordList(current, size, type, level, way, status, title, content, insertDate);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 删除告警记录
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 16:58
     */
    @Operation(summary = "删除告警记录")
    @DeleteMapping("/delete-monitor-alarm-record")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.ALARM, operType = OperateTypeConstants.DELETE, operDesc = "删除告警记录")
    public LayUiAdminResultVo deleteMonitorAlarmRecord(@RequestBody List<Long> ids) {
        return this.monitorAlarmRecordService.deleteMonitorAlarmRecord(ids);
    }

    /**
     * <p>
     * 清空告警记录
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 10:08
     */
    @Operation(summary = "清空告警记录")
    @DeleteMapping("/cleanup-monitor-alarm-record")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.ALARM, operType = OperateTypeConstants.DELETE, operDesc = "清空告警记录")
    public LayUiAdminResultVo cleanupMonitorAlarmRecord() {
        return this.monitorAlarmRecordService.cleanupMonitorAlarmRecord();
    }

    /**
     * <p>
     * 导出告警记录列表
     * </p>
     *
     * @param type       告警类型
     * @param level      告警级别
     * @param way        告警方式
     * @param status     告警状态（0：失败；1：成功）
     * @param title      告警标题
     * @param content    告警内容
     * @param insertDate 记录日期
     * @author 皮锋
     * @custom.date 2021/5/18 22:12
     */
    @Operation(summary = "导出告警记录列表")
    @Parameters(value = {
            @Parameter(name = "type", description = "告警类型", in = ParameterIn.QUERY),
            @Parameter(name = "level", description = "告警级别", in = ParameterIn.QUERY),
            @Parameter(name = "way", description = "告警方式", in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "告警状态（0：失败；1：成功）", in = ParameterIn.QUERY),
            @Parameter(name = "title", description = "告警标题", in = ParameterIn.QUERY),
            @Parameter(name = "content", description = "告警内容", in = ParameterIn.QUERY),
            @Parameter(name = "insertDate", description = "记录日期", in = ParameterIn.QUERY)})
    @GetMapping("/export-monitor-alarm-record-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.ALARM, operType = OperateTypeConstants.EXPORT, operDesc = "导出告警记录列表")
    public void exportMonitorAlarmRecordList(@RequestParam(value = "type", required = false) String type,
                                             @RequestParam(value = "level", required = false) String level,
                                             @RequestParam(value = "way", required = false) String way,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "title", required = false) String title,
                                             @RequestParam(value = "content", required = false) String content,
                                             @RequestParam(value = "insertDate", required = false) String insertDate) {
        String name = "告警记录";
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = this.monitorAlarmRecordService.getMonitorAlarmRecordList(type, level, way, status, title, content, insertDate);
        if (CollectionUtils.isEmpty(monitorAlarmRecordVos)) {
            // 倒序
            Collections.reverse(monitorAlarmRecordVos);
        }
        for (MonitorAlarmRecordVo monitorAlarmRecordVo : monitorAlarmRecordVos) {
            // 单独处理下告警内容
            String alarmRecordVoContent = monitorAlarmRecordVo.getContent() != null ? monitorAlarmRecordVo.getContent() : "";
            // 替换
            monitorAlarmRecordVo.setContent(StringUtils.replace(alarmRecordVoContent, "<br>", "\n"));
            alarmRecordVoContent = monitorAlarmRecordVo.getContent();
            // 截取
            monitorAlarmRecordVo.setContent(alarmRecordVoContent.length() >= 500 ? StringUtils.substring(alarmRecordVoContent, 0, 500) + "......" : alarmRecordVoContent);
            // 告警方式，多种方式用逗号分隔
            String alarmWay = monitorAlarmRecordVo.getWay();
            monitorAlarmRecordVo.setWay(StringUtils.isBlank(alarmWay) ? alarmWay : alarmWay.replace(",", "、")
                    .replace(AlarmWayEnums.SMS.name(), "短信")
                    .replace(AlarmWayEnums.MAIL.name(), "邮件"));
        }
        ExportParams params = new ExportParams(name, name, ExcelType.XSSF);
        // 不设置列高
        params.setHeight((short) -1);
        // 自定义导出样式
        params.setStyle(CustomExcelExportStyler.class);
        // 列宽自适应
        params.setAutoSize(true);
        Map<String, Object> map = new HashMap<>(16);
        map.put(NormalExcelConstants.DATA_LIST, monitorAlarmRecordVos);
        map.put(NormalExcelConstants.CLASS, MonitorAlarmRecordVo.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, name + "（" + DateTimeUtils.dateToString(new Date(), DateTimeStylesEnums.YYYYMMDDHHMMSS) + "）");
        ServletRequestAttributes servletRequestAttributes = ContextUtils.getServletRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * <p>
     * 访问告警记录详情页面
     * </p>
     *
     * @param id 告警记录ID
     * @return {@link ModelAndView} 告警记录详情页面
     * @author 皮锋
     * @custom.date 2021/6/18 17:20
     */
    @Operation(summary = "访问告警记录详情页面")
    @GetMapping("/monitor-alarm-record-detail")
    @Parameters(value = {
            @Parameter(name = "id", description = "告警记录ID", required = true, in = ParameterIn.QUERY)})
    @OperateLog(operModule = UiModuleConstants.ALARM, operType = OperateTypeConstants.PAGE, operDesc = "访问告警记录详情页面")
    public ModelAndView monitorAlarmRecordDetail(Long id) {
        ModelAndView mv = new ModelAndView("alarm/alarm-record-detail");
        MonitorAlarmRecordVo monitorAlarmRecordVo = this.monitorAlarmRecordService.getMonitorAlarmRecord(id);
        mv.addObject("monitorAlarmRecordVo", monitorAlarmRecordVo);
        List<MonitorAlarmRecordDetailVo> monitorAlarmRecordDetailVos = this.monitorAlarmRecordDetailService.getMonitorAlarmRecordDetails(id);
        mv.addObject("monitorAlarmRecordDetailVos", monitorAlarmRecordDetailVos);
        return mv;
    }

}
