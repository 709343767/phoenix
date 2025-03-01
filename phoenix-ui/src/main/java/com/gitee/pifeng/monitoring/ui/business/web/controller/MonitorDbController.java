package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.constant.DbEnums;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorDbService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorDbVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据库
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
@Controller
@Tag(name = "数据库")
@RequestMapping("/monitor-db")
public class MonitorDbController {

    /**
     * 数据库表服务类
     */
    @Autowired
    private IMonitorDbService monitorDbService;

    /**
     * 监控环境服务类
     */
    @Autowired
    private IMonitorEnvService monitorEnvService;

    /**
     * 监控分组服务类
     */
    @Autowired
    private IMonitorGroupService monitorGroupService;

    /**
     * <p>
     * 访问数据库列表页面
     * </p>
     *
     * @return {@link ModelAndView} 数据库列表页面
     * @author 皮锋
     * @custom.date 2020/12/19 17:31
     */
    @Operation(summary = "访问数据库列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("db/db");
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 获取数据库列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param connName     数据库连接名
     * @param url          数据库URL
     * @param dbType       数据库类型
     * @param isOnline     数据库状态
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/12/19 17:36
     */
    @Operation(summary = "获取数据库列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "connName", description = "数据库连接名", in = ParameterIn.QUERY),
            @Parameter(name = "url", description = "数据库URL", in = ParameterIn.QUERY),
            @Parameter(name = "dbType", description = "数据库类型", in = ParameterIn.QUERY),
            @Parameter(name = "isOnline", description = "数据库状态", in = ParameterIn.QUERY),
            @Parameter(name = "monitorEnv", description = "监控环境", in = ParameterIn.QUERY),
            @Parameter(name = "monitorGroup", description = "监控分组", in = ParameterIn.QUERY)})
    @GetMapping("get-monitor-db-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.QUERY, operDesc = "获取数据库列表")
    public LayUiAdminResultVo getMonitorDbList(@RequestParam(value = "current") Long current,
                                               @RequestParam(value = "size") Long size,
                                               @RequestParam(value = "connName", required = false) String connName,
                                               @RequestParam(value = "url", required = false) String url,
                                               @RequestParam(value = "dbType", required = false) String dbType,
                                               @RequestParam(value = "isOnline", required = false) String isOnline,
                                               @RequestParam(value = "monitorEnv", required = false) String monitorEnv,
                                               @RequestParam(value = "monitorGroup", required = false) String monitorGroup) {
        Page<MonitorDbVo> page = this.monitorDbService.getMonitorDbList(current, size, connName, url, dbType, isOnline, monitorEnv, monitorGroup);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 访问编辑数据库信息表单页面
     * </p>
     *
     * @param id 数据库ID
     * @return {@link ModelAndView} 编辑数据库信息表单页面
     * @author 皮锋
     * @custom.date 2020/12/19 18:51
     */
    @Operation(summary = "访问编辑数据库信息表单页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "数据库ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/edit-monitor-db-form")
    public ModelAndView editMonitorDbForm(@RequestParam(name = "id") Long id) {
        MonitorDb monitorDb = this.monitorDbService.getById(id);
        MonitorDbVo monitorDbVo = MonitorDbVo.builder().build().convertFor(monitorDb);
        // 解密密码
        monitorDbVo.setPassword(new String(Base64.getDecoder().decode(monitorDbVo.getPassword()), StandardCharsets.UTF_8));
        ModelAndView mv = new ModelAndView("db/edit-db");
        mv.addObject(monitorDbVo);
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        mv.addObject("env", monitorDbVo.getMonitorEnv());
        mv.addObject("group", monitorDbVo.getMonitorGroup());
        return mv;
    }

    /**
     * <p>
     * 编辑数据库信息
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/19 19:56
     */
    @Operation(summary = "编辑数据库信息")
    @PutMapping("/edit-monitor-db")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑数据库信息")
    public LayUiAdminResultVo editMonitorDb(MonitorDbVo monitorDbVo) throws SigarException, IOException {
        LayUiAdminResultVo layUiAdminResultVo = this.monitorDbService.editMonitorDb(monitorDbVo);
        // 测试数据库连通性
        if (StringUtils.isNotBlank(monitorDbVo.getPassword())) {
            this.monitorDbService.testMonitorDb(monitorDbVo);
        }
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Operation(summary = "设置是否开启监控（0：不开启监控；1：开启监控）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableMonitor", description = "是否开启监控（0：不开启监控；1：开启监控）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-monitor")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启监控（0：不开启监控；1：开启监控）")
    public LayUiAdminResultVo setIsEnableMonitor(@RequestParam(value = "id") Long id,
                                                 @RequestParam(value = "isEnableMonitor") String isEnableMonitor) {
        return this.monitorDbService.setIsEnableMonitor(id, isEnableMonitor);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Operation(summary = "设置是否开启告警（0：不开启告警；1：开启告警）")
    @Parameters(value = {
            @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isEnableAlarm", description = "是否开启告警（0：不开启告警；1：开启告警）", in = ParameterIn.QUERY)})
    @PutMapping("/set-is-enable-alarm")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.UPDATE, operDesc = "设置是否开启告警（0：不开启告警；1：开启告警）")
    public LayUiAdminResultVo setIsEnableAlarm(@RequestParam(value = "id") Long id,
                                               @RequestParam(value = "isEnableAlarm") String isEnableAlarm) {
        return this.monitorDbService.setIsEnableAlarm(id, isEnableAlarm);
    }

    /**
     * <p>
     * 访问新增数据库信息表单页面
     * </p>
     *
     * @return {@link ModelAndView} 新增数据库信息表单页面
     * @author 皮锋
     * @custom.date 2020/12/19 19:54
     */
    @Operation(summary = "访问新增数据库信息表单页面")
    @GetMapping("/add-monitor-db-form")
    public ModelAndView addMonitorDbForm() {
        ModelAndView mv = new ModelAndView("db/add-db");
        // 监控环境列表
        List<String> monitorEnvs = this.monitorEnvService.list().stream().map(MonitorEnv::getEnvName).collect(Collectors.toList());
        // 监控分组列表
        List<String> monitorGroups = this.monitorGroupService.list().stream().map(MonitorGroup::getGroupName).collect(Collectors.toList());
        mv.addObject("monitorEnvs", monitorEnvs);
        mv.addObject("monitorGroups", monitorGroups);
        return mv;
    }

    /**
     * <p>
     * 添加数据库信息
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/19 20:05
     */
    @Operation(summary = "添加数据库信息")
    @PostMapping("/add-monitor-db")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.ADD, operDesc = "添加数据库信息")
    public LayUiAdminResultVo addMonitorDb(MonitorDbVo monitorDbVo) throws SigarException, IOException {
        LayUiAdminResultVo layUiAdminResultVo = this.monitorDbService.addMonitorDb(monitorDbVo);
        // 测试数据库连通性
        this.monitorDbService.testMonitorDb(monitorDbVo);
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 删除数据库信息
     * </p>
     *
     * @param ids 数据库主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:59
     */
    @Operation(summary = "删除数据库信息")
    @DeleteMapping("/delete-monitor-db")
    @PreAuthorize("hasAuthority('超级管理员')")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.DELETE, operDesc = "删除数据库信息")
    public LayUiAdminResultVo deleteMonitorDb(@RequestBody List<Long> ids) {
        return this.monitorDbService.deleteMonitorDb(ids);
    }

    /**
     * <p>
     * 访问数据库详情页面
     * </p>
     *
     * @param id     数据库主键ID
     * @param dbType 数据库类型
     * @return {@link ModelAndView} 数据库详情页面
     * @author 皮锋
     * @custom.date 2020/12/22 14:47
     */
    @Operation(summary = "访问数据库详情页面")
    @Parameters(value = {
            @Parameter(name = "id", description = "数据库ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "dbType", description = "数据库类型", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/db-detail")
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.PAGE, operDesc = "访问数据库详情页面")
    public ModelAndView dbDetail(Long id, String dbType) {
        ModelAndView mv = new ModelAndView();
        DbEnums dbEnums = DbEnums.str2Enum(dbType);
        switch (dbEnums) {
            case MySQL:
                mv.setViewName("db/db-mysql-detail");
                break;
            case Oracle:
                mv.setViewName("db/db-oracle-detail");
                break;
            case Mongo:
                mv.setViewName("db/db-mongo-detail");
                break;
            case Redis:
                mv.setViewName("db/db-redis-detail");
                break;
            default:
                break;
        }
        MonitorDb monitorDb = this.monitorDbService.getById(id);
        MonitorDbVo monitorDbVo = MonitorDbVo.builder().build().convertFor(monitorDb);
        // 屏蔽密码
        monitorDbVo.setPassword(null);
        mv.addObject(monitorDbVo);
        return mv;
    }

    /**
     * <p>
     * 测试数据库连通性
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：网络连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    @Operation(summary = "测试数据库连通性")
    @PostMapping("/test-monitor-db")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.TEST, operDesc = "测试数据库连通性")
    public LayUiAdminResultVo testMonitorDb(MonitorDbVo monitorDbVo) throws SigarException, IOException {
        return this.monitorDbService.testMonitorDb(monitorDbVo);
    }

}