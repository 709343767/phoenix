package com.gitee.pifeng.monitoring.ui.business.web.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.web.util.ContextUtils;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmRecordService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import com.gitee.pifeng.monitoring.ui.core.CustomExcelExportStyler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param status  告警状态
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
            @ApiImplicitParam(name = "status", value = "告警状态", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "告警标题", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "告警内容", paramType = "query", dataType = "string")})
    @GetMapping("/get-monitor-alarm-record-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.ALARM + "#告警记录", operType = OperateTypeConstants.QUERY, operDesc = "获取告警记录列表")
    public LayUiAdminResultVo getMonitorAlarmRecordList(Long current, Long size, String type, String level, String status, String title, String content) {
        Page<MonitorAlarmRecordVo> page = this.monitorAlarmRecordService.getMonitorAlarmRecordList(current, size, type, level, status, title, content);
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
    @DeleteMapping("/delete-monitor-alarm-record")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.ALARM + "#告警记录", operType = OperateTypeConstants.DELETE, operDesc = "删除告警记录")
    public LayUiAdminResultVo deleteMonitorAlarmRecord(@RequestBody List<MonitorAlarmRecordVo> monitorAlarmRecordVos) {
        return this.monitorAlarmRecordService.deleteMonitorAlarmRecord(monitorAlarmRecordVos);
    }

    /**
     * <p>
     * 导出告警记录列表
     * </p>
     *
     * @param type    告警类型
     * @param level   告警级别
     * @param status  告警状态
     * @param title   告警标题
     * @param content 告警内容
     * @author 皮锋
     * @custom.date 2021/5/18 22:12
     */
    @ApiOperation(value = "导出告警记录列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "type", value = "告警类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "level", value = "告警级别", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "告警状态", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "title", value = "告警标题", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "content", value = "告警内容", paramType = "query", dataType = "string")})
    @GetMapping("/export-monitor-alarm-record-list")
    @OperateLog(operModule = UiModuleConstants.ALARM + "#告警记录", operType = OperateTypeConstants.EXPORT, operDesc = "导出告警记录列表")
    public void exportMonitorAlarmRecordList(String type, String level, String status, String title, String content) {
        String name = "告警记录";
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = this.monitorAlarmRecordService.getMonitorAlarmRecordList(type, level, status, title, content);
        for (MonitorAlarmRecordVo monitorAlarmRecordVo : monitorAlarmRecordVos) {
            monitorAlarmRecordVo.setContent(StringUtils.replace(monitorAlarmRecordVo.getContent(), "<br>", "\n"));
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

}
