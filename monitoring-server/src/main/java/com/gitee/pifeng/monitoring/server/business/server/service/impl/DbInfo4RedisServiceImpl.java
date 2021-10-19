package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.gitee.pifeng.monitoring.common.exception.DbException;
import com.gitee.pifeng.monitoring.common.web.util.db.RedisUtils;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbInfo4RedisService;
import lombok.Cleanup;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * <p>
 * Redis数据库信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 20:15
 */
@Service
public class DbInfo4RedisServiceImpl implements IDbInfo4RedisService {

    /**
     * <p>
     * 获取Redis信息
     * </p>
     *
     * @param host     主机
     * @param port     端口
     * @param password 密码
     * @return Redis信息
     * @author 皮锋
     * @custom.date 2021/10/16 20:22
     */
    @Override
    public String getRedisInfo(String host, int port, String password) {
        // 获取 Jedis
        @Cleanup
        Jedis jedis = RedisUtils.getJedis(host, port, password);
        if (jedis != null) {
            return jedis.info();
        } else {
            throw new DbException("获取Redis信息异常！");
        }
    }

}
