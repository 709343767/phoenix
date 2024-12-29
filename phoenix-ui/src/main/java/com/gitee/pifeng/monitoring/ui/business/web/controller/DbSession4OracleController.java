package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbSession4OracleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Oracle数据库会话
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:37
 */
@Controller
@Tag(name = "数据库会话.Oracle")
@RequestMapping("/db-session4oracle")
public class DbSession4OracleController {

    /**
     * Oracle数据库会话服务类
     */
    @Autowired
    private IDbSession4OracleService dbSession4OracleService;

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return layUiAdmin响应对象
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @Operation(summary = "获取会话列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "id", description = "数据库ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/get-session-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#会话", operType = OperateTypeConstants.QUERY, operDesc = "获取会话列表")
    public LayUiAdminResultVo getSessionList(@RequestParam(value = "current") Long current,
                                             @RequestParam(value = "size") Long size,
                                             @RequestParam(value = "id") Long id) throws NetException, IOException, SigarException {
        Page<DbSession4OracleVo> page = this.dbSession4OracleService.getSessionList(current, size, id);
        return LayUiAdminResultVo.ok(page);
    }

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param dbSession4OracleVos Oracle数据库会话
     * @param id                  数据库ID
     * @return layUiAdmin响应对象
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:03
     */
    @Operation(summary = "结束会话")
    @DeleteMapping("/destroy-session")
    @PreAuthorize("hasAuthority('超级管理员')")
    @Parameters(value = {
            @Parameter(name = "id", description = "数据库ID", required = true, in = ParameterIn.QUERY)})
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#会话", operType = OperateTypeConstants.CONTROL, operDesc = "结束会话")
    public LayUiAdminResultVo destroySession(@RequestBody List<DbSession4OracleVo> dbSession4OracleVos,
                                             @RequestParam(value = "id") Long id) throws NetException, IOException, SigarException {
        return this.dbSession4OracleService.destroySession(dbSession4OracleVos, id);
    }

}
