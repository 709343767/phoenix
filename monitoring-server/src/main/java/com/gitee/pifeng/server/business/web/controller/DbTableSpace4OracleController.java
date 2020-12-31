package com.gitee.pifeng.server.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.service.IDbTableSpace4OracleService;
import com.gitee.pifeng.server.business.web.vo.DbTableSpace4OracleVo;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * <p>
 * Oracle数据库表空间
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:08
 */
@Controller
@Api(tags = "数据库表空间.Oracle")
@RequestMapping("/db-tablespace4oracle")
public class DbTableSpace4OracleController {

    /**
     * Oracle数据库表空间服务类
     */
    @Autowired
    private IDbTableSpace4OracleService dbTableSpace4OracleService;

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return layUiAdmin响应对象
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @ApiOperation(value = "获取表空间列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/get-tablespace-list")
    @ResponseBody
    public LayUiAdminResultVo getTableSpaceList(Long current, Long size, Long id) throws SQLException {
        Page<DbTableSpace4OracleVo> page = this.dbTableSpace4OracleService.getTableSpaceList(current, size, id);
        return LayUiAdminResultVo.ok(page);
    }

}
