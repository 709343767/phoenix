package com.gitee.pifeng.monitoring.server.business.server.service;

/**
 * <p>
 * Redis数据库信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 20:13
 */
public interface IDbInfo4RedisService {

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
    String getRedisInfo(String host, int port, String password);
}
