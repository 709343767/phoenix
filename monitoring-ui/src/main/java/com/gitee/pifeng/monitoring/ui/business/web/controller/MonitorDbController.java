package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorDbService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorDbVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 * 数据库
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
@Controller
@Api(tags = "数据库")
@RequestMapping("/monitor-db")
public class MonitorDbController {

    /**
     * 数据库表服务类
     */
    @Autowired
    private IMonitorDbService monitorDbService;

    /**
     * <p>
     * 访问数据库列表页面
     * </p>
     *
     * @return {@link ModelAndView} 数据库列表页面
     * @author 皮锋
     * @custom.date 2020/12/19 17:31
     */
    @ApiOperation(value = "访问数据库列表页面")
    @GetMapping("/list")
    public ModelAndView list() {
        return new ModelAndView("db/db");
    }

    /**
     * <p>
     * 获取数据库列表
     * </p>
     *
     * @param current  当前页
     * @param size     每页显示条数
     * @param connName 数据库连接名
     * @param url      数据库URL
     * @param dbType   数据库类型
     * @param isOnline 数据库状态
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/12/19 17:36
     */
    @ApiOperation(value = "获取数据库列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "connName", value = "数据库连接名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "url", value = "数据库URL", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "dbType", value = "数据库类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "isOnline", value = "数据库状态", paramType = "query", dataType = "string")})
    @GetMapping("get-monitor-db-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.QUERY, operDesc = "获取数据库列表")
    public LayUiAdminResultVo getMonitorDbList(Long current, Long size, String connName, String url, String dbType, String isOnline) {
        Page<MonitorDbVo> page = this.monitorDbService.getMonitorDbList(current, size, connName, url, dbType, isOnline);
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
    @ApiOperation(value = "访问编辑数据库信息表单页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/edit-monitor-db-form")
    public ModelAndView editMonitorDbForm(@RequestParam(name = "id") Long id) {
        MonitorDb monitorDb = this.monitorDbService.getById(id);
        MonitorDbVo monitorDbVo = MonitorDbVo.builder().build().convertFor(monitorDb);
        // 解密密码
        monitorDbVo.setPassword(new String(Base64.getDecoder().decode(monitorDbVo.getPassword()), StandardCharsets.UTF_8));
        ModelAndView mv = new ModelAndView("db/edit-db");
        mv.addObject(monitorDbVo);
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
     * @author 皮锋
     * @custom.date 2020/12/19 19:56
     */
    @ApiOperation(value = "编辑数据库信息")
    @PutMapping("/edit-monitor-db")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.UPDATE, operDesc = "编辑数据库信息")
    public LayUiAdminResultVo editMonitorDb(MonitorDbVo monitorDbVo) {
        return this.monitorDbService.editMonitorDb(monitorDbVo);
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
    @ApiOperation(value = "访问新增数据库信息表单页面")
    @GetMapping("/add-monitor-db-form")
    public ModelAndView addMonitorDbForm() {
        return new ModelAndView("db/add-db");
    }

    /**
     * <p>
     * 添加数据库信息
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:05
     */
    @ApiOperation(value = "添加数据库信息")
    @PostMapping("/add-monitor-db")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.ADD, operDesc = "添加数据库信息")
    public LayUiAdminResultVo addMonitorDb(MonitorDbVo monitorDbVo) {
        return this.monitorDbService.addMonitorDb(monitorDbVo);
    }

    /**
     * <p>
     * 删除数据库信息
     * </p>
     *
     * @param monitorDbVos 删除数据库信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:59
     */
    @ApiOperation(value = "删除数据库信息")
    @DeleteMapping("/delete-monitor-db")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE, operType = OperateTypeConstants.DELETE, operDesc = "删除数据库信息")
    public LayUiAdminResultVo deleteMonitorDb(@RequestBody List<MonitorDbVo> monitorDbVos) {
        return this.monitorDbService.deleteMonitorDb(monitorDbVos);
    }

    /**
     * <p>
     * 访问数据库详情页面
     * </p>
     *
     * @param id 数据库主键ID
     * @return {@link ModelAndView} 数据库详情页面
     * @author 皮锋
     * @custom.date 2020/12/22 14:47
     */
    @ApiOperation(value = "访问数据库详情页面")
    @GetMapping("/db-detail")
    public ModelAndView dbDetail(String id) {
        ModelAndView mv = new ModelAndView("db/db-detail");
        MonitorDb monitorDb = this.monitorDbService.getById(id);
        MonitorDbVo monitorDbVo = MonitorDbVo.builder().build().convertFor(monitorDb);
        mv.addObject(monitorDbVo);
        return mv;
    }

}

