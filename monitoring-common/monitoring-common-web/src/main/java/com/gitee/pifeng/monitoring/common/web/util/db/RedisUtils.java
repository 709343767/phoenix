package com.gitee.pifeng.monitoring.common.web.util.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * <p>
 * Redis工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 14:08
 */
@Slf4j
public class RedisUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/10/16 14:09
     */
    private RedisUtils() {
    }

    /**
     * <p>
     * 获取 Jedis
     * </p>
     *
     * @param host     主机
     * @param port     端口
     * @param password 密码
     * @return {@link Jedis}
     * @author 皮锋
     * @custom.date 2021/10/16 14:15
     */
    public static Jedis getJedis(String host, int port, String password) {
        try {
            Jedis jedis = new Jedis(host, port, 5000);
            if (StringUtils.isNotBlank(password)) {
                jedis.auth(password);
            }
            return jedis;
        } catch (Exception e) {
            log.error("与Redis数据库建立连接异常！");
            return null;
        }
    }

    /**
     * <p>
     * Redis数据库是否可连接
     * </p>
     *
     * @param jedis {@link Jedis}
     * @return 是 或者 否
     * @author 皮锋
     * @custom.date 2021/10/16 14:23
     */
    public static boolean isConnect(Jedis jedis) {
        if (jedis == null) {
            return false;
        }
        String ping = jedis.ping();
        return "PONG".equalsIgnoreCase(ping);
    }

    /**
     * <p>
     * 关闭 Jedis
     * </p>
     *
     * @param jedis {@link Jedis}
     * @author 皮锋
     * @custom.date 2021/10/16 14:17
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
