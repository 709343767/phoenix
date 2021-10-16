package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.gitee.pifeng.monitoring.common.constant.sql.MySql;
import com.gitee.pifeng.monitoring.common.web.util.db.DbUtils;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbSession4MysqlService;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * MySQL数据库会话服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/24 16:49
 */
@Slf4j
@Service
public class DbSession4MysqlServiceImpl implements IDbSession4MysqlService {

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
    @Override
    public List<Entity> getSessionList(String url, String username, String password) throws SQLException {
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        return SqlExecutor.query(connection, MySql.SESSION_LIST, new EntityListHandler());
    }

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
    @Override
    public void destroySession(String url, String username, String password, List<Long> sessionIds) throws SQLException {
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        // 循环每一个session
        sessionIds.forEach(sessionId -> {
            try {
                SqlExecutor.execute(connection, MySql.KILL_SESSION, sessionId);
            } catch (SQLException e) {
                log.error("结束会话异常！", e);
            }
        });
    }

}
