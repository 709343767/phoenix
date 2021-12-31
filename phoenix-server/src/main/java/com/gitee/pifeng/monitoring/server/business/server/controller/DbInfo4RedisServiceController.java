package com.gitee.pifeng.monitoring.server.business.server.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.DbException;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbInfo4RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Redis数据库信息控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 20:17
 */
@Slf4j
@RestController
@Api(tags = "数据库信息.Redis")
@RequestMapping("/db-info4redis")
public class DbInfo4RedisServiceController {

    /**
     * Redis数据库信息服务接口
     */
    @Autowired
    private IDbInfo4RedisService dbInfo4RedisService;

    /**
     * <p>
     * 获取Redis信息
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @throws DbException 自定义数据库异常
     * @author 皮锋
     * @custom.date 2021/10/16 20:22
     */
    @ApiOperation(value = "获取Redis信息")
    @PostMapping("/get-redis-info")
    public BaseResponsePackage getRedisInfo(@RequestBody BaseRequestPackage baseRequestPackage) throws DbException {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        JSONObject extraMsg = baseRequestPackage.getExtraMsg();
        String host = extraMsg.getString("host");
        int port = extraMsg.getIntValue("port");
        String password = extraMsg.getString("password");
        String info = this.dbInfo4RedisService.getRedisInfo(host, port, password);
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(info).build());
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        if (timer.intervalSecond() > 1) {
            log.warn("获取Redis信息耗时：{}", betweenDay);
        }
        return baseResponsePackage;
    }

}
