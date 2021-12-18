package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbSession4OracleService;
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
 * Oracle数据库会话控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:37
 */
@Slf4j
@RestController
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
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @ApiOperation(value = "获取会话列表")
    @PostMapping("/get-session-list")
    public BaseResponsePackage getSessionList(@RequestBody BaseRequestPackage baseRequestPackage) throws SQLException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String url = extraMsg.getString("url");
        String username = extraMsg.getString("username");
        String password = extraMsg.getString("password");
        List<Entity> entities = this.dbSession4OracleService.getSessionList(url, username, password);
        String jsonString = JSON.toJSONString(entities);
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(jsonString).build());
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        log.info("获取会话列表耗时：{}", betweenDay);
        return baseResponsePackage;
    }

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:03
     */
    @ApiOperation(value = "结束会话")
    @PostMapping("/destroy-session")
    public BaseResponsePackage destroySession(@RequestBody BaseRequestPackage baseRequestPackage) throws SQLException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String url = extraMsg.getString("url");
        String username = extraMsg.getString("username");
        String password = extraMsg.getString("password");
        List<Long> sids = extraMsg.getObject("sids", new TypeReference<List<Long>>() {
        });
        List<Long> serials = extraMsg.getObject("serials", new TypeReference<List<Long>>() {
        });
        this.dbSession4OracleService.destroySession(url, username, password, sids, serials);
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build());
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        log.info("结束会话耗时：{}", betweenDay);
        return baseResponsePackage;
    }

}
