package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.gitee.pifeng.monitoring.common.constant.sql.Oracle;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbSession4OracleService;
import com.gitee.pifeng.monitoring.server.util.db.DbUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Oracle数据库会话服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:39
 */
@Slf4j
@Service
public class DbSession4OracleServiceImpl implements IDbSession4OracleService {

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
     * @custom.date 2020/12/30 12:46
     */
    @Override
    public List<Entity> getSessionList(String url, String username, String password) throws SQLException {
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        return SqlExecutor.query(connection, Oracle.SESSION_LIST, new EntityListHandler());
    }

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @param sids     sids
     * @param serials  serials
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/30 15:22
     */
    @Override
    public void destroySession(String url, String username, String password, List<Long> sids, List<Long> serials) throws SQLException {
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        for (int i = 0; i < sids.size(); i++) {
            try {
                String sql = Oracle.KILL_SESSION.replace("SID", String.valueOf(sids.get(i))).replace("SERIAL#", String.valueOf(serials.get(i)));
                SqlExecutor.execute(connection, sql);
            } catch (SQLException e) {
                log.error("结束会话异常！", e);
            }
        }
    }

}
