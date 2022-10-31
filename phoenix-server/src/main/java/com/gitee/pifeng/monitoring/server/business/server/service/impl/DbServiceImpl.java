package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.db.DbUtil;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DbEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.sql.MySql;
import com.gitee.pifeng.monitoring.common.constant.sql.Oracle;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorDb;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbService;
import com.gitee.pifeng.monitoring.server.util.db.DbUtils;
import com.gitee.pifeng.monitoring.server.util.db.MongoUtils;
import com.gitee.pifeng.monitoring.server.util.db.RedisUtils;
import com.mongodb.MongoClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.util.Date;

/**
 * <p>
 * 数据库表服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 15:25
 */
@Service
public class DbServiceImpl extends ServiceImpl<IMonitorDbDao, MonitorDb> implements IDbService {

    /**
     * <p>
     * 测试数据库连通性
     * </p>
     *
     * @param url      数据库URL
     * @param dbType   数据库类型
     * @param username 用户名
     * @param password 密码
     * @return true 或者 false
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    @Override
    public Boolean testMonitorDb(String url, String dbType, String username, String password) {
        // 是否可连接
        boolean isConnected = false;
        // 关系型数据库
        if (StringUtils.equalsIgnoreCase(DbEnums.MySQL.name(), dbType)
                || StringUtils.equalsIgnoreCase(DbEnums.Oracle.name(), dbType)) {
            Connection connection = null;
            try {
                connection = DbUtils.getConnection(url, username, password);
                // 数据库连接不为空
                if (connection != null) {
                    // mysql
                    if (StringUtils.equalsIgnoreCase(dbType, DbType.MYSQL.getDb())) {
                        SqlExecutor.query(connection, MySql.CHECK_CONN, new NumberHandler());
                    }
                    // oracle
                    if (StringUtils.equalsIgnoreCase(dbType, DbType.ORACLE.getDb())) {
                        SqlExecutor.query(connection, Oracle.CHECK_CONN, new NumberHandler());
                    }
                    isConnected = true;
                }
            } catch (Exception ignored) {
            } finally {
                DbUtil.close(connection);
            }
        }
        // Redis数据库
        else if (StringUtils.equalsIgnoreCase(DbEnums.Redis.name(), dbType)) {
            Jedis jedis = null;
            try {
                String[] address = StringUtils.split(url, ":");
                // 主机
                String host = address[0];
                // 端口
                int port = Integer.parseInt(address[1]);
                // 获取 Jedis
                jedis = RedisUtils.getJedis(host, port, password);
                // Redis数据库是否可连接
                isConnected = RedisUtils.isConnect(jedis);
            } catch (Exception ignored) {
            } finally {
                RedisUtils.close(jedis);
            }
        }
        // mongo数据库
        else if (StringUtils.equalsIgnoreCase(DbEnums.Mongo.name(), dbType)) {
            MongoClient mongoClient = null;
            try {
                // 获取 MongoClient
                mongoClient = MongoUtils.getClient(url);
                // Mongo数据库是否可连接
                isConnected = MongoUtils.isConnect(mongoClient);
            } catch (Exception ignored) {
            } finally {
                MongoUtils.close(mongoClient);
            }
        }
        // 查询数据库
        LambdaQueryWrapper<MonitorDb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorDb::getUrl, url);
        lambdaQueryWrapper.eq(MonitorDb::getDbType, dbType);
        MonitorDb monitorDb = this.getOne(lambdaQueryWrapper);
        // 如果有数据库信息，则更新
        if (monitorDb != null) {
            monitorDb.setIsOnline(isConnected ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorDb.setUpdateTime(new Date());
            this.updateById(monitorDb);
        }
        return isConnected;
    }

}
