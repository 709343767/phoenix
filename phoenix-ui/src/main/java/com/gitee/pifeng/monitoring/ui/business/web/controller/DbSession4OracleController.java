package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbSession4OracleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "数据库会话.Oracle")
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
    @ApiOperation(value = "获取会话列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
    @GetMapping("/get-session-list")
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#会话", operType = OperateTypeConstants.QUERY, operDesc = "获取会话列表")
    public LayUiAdminResultVo getSessionList(Long current, Long size, Long id) throws NetException, IOException, SigarException {
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
    @ApiOperation(value = "结束会话")
    @DeleteMapping("/destroy-session")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long", dataTypeClass = Long.class)})
    @ResponseBody
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#会话", operType = OperateTypeConstants.DELETE, operDesc = "结束会话")
    public LayUiAdminResultVo destroySession(@RequestBody List<DbSession4OracleVo> dbSession4OracleVos, Long id) throws NetException, IOException, SigarException {
        return this.dbSession4OracleService.destroySession(dbSession4OracleVos, id);
    }

}
