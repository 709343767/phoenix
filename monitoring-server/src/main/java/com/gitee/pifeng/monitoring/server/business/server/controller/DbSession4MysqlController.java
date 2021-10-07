package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbSession4MysqlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * MySQL数据库会话控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/22 14:36
 */
@RestController
@Api(tags = "数据库会话.MySQL")
@RequestMapping("/db-session4mysql")
@Slf4j
public class DbSession4MysqlController {

    /**
     * MySQL数据库会话服务类
     */
    @Autowired
    private IDbSession4MysqlService dbSession4MysqlService;

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @ApiOperation(value = "获取会话列表")
    @PostMapping("/get-session-list")
    public BaseResponsePackage getSessionList(@RequestBody BaseRequestPackage baseRequestPackage) {
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String url = extraMsg.getString("url");
        String username = extraMsg.getString("username");
        String password = extraMsg.getString("password");
        try {
            List<Entity> entities = this.dbSession4MysqlService.getSessionList(url, username, password);
            String jsonString = JSON.toJSONString(entities);
            return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(jsonString).build());
        } catch (SQLException e) {
            log.error("获取会话列表失败！", e);
            return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(false).msg(e.getMessage()).build());
        }
    }

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/12/25 17:03
     */
    @ApiOperation(value = "结束会话")
    @PostMapping("/destroy-session")
    public BaseResponsePackage destroySession(@RequestBody BaseRequestPackage baseRequestPackage) {
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String url = extraMsg.getString("url");
        String username = extraMsg.getString("username");
        String password = extraMsg.getString("password");
        List<Long> sessionIds = extraMsg.getObject("sessionIds", new TypeReference<List<Long>>() {
        });
        try {
            this.dbSession4MysqlService.destroySession(url, username, password, sessionIds);
            return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build());
        } catch (SQLException e) {
            log.error("获取会话列表失败！", e);
            return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(false).msg(e.getMessage()).build());
        }
    }

}
