package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbTableSpace4OracleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Oracle数据库表空间控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:08
 */
@RestController
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
     * 获取表空间列表(按文件)
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @ApiOperation(value = "获取表空间列表(按文件)")
    @PostMapping("/get-tablespace-list-file")
    public BaseResponsePackage getTableSpaceListFile(@RequestBody BaseRequestPackage baseRequestPackage) throws SQLException {
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String url = extraMsg.getString("url");
        String username = extraMsg.getString("username");
        String password = extraMsg.getString("password");
        List<Entity> entities = this.dbTableSpace4OracleService.getTableSpaceListFile(url, username, password);
        String jsonString = JSON.toJSONString(entities);
        return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(jsonString).build());
    }

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @ApiOperation(value = "获取表空间列表")
    @PostMapping("/get-tablespace-list-all")
    public BaseResponsePackage getTableSpaceListAll(@RequestBody BaseRequestPackage baseRequestPackage) throws SQLException {
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String url = extraMsg.getString("url");
        String username = extraMsg.getString("username");
        String password = extraMsg.getString("password");
        List<Entity> entities = this.dbTableSpace4OracleService.getTableSpaceListAll(url, username, password);
        String jsonString = JSON.toJSONString(entities);
        return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(jsonString).build());
    }

}
