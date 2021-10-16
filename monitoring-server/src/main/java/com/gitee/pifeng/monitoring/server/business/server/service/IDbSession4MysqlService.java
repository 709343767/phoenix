package com.gitee.pifeng.monitoring.server.business.server.service;

import cn.hutool.db.Entity;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * MySQL数据库会话服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/24 16:49
 */
public interface IDbSession4MysqlService {

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @return 会话列表
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:55
     */
    List<Entity> getSessionList(String url, String username, String password) throws SQLException;

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param url        url
     * @param username   用户名
     * @param password   密码
     * @param sessionIds session列表
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:05
     */
    void destroySession(String url, String username, String password, List<Long> sessionIds) throws SQLException;

}
