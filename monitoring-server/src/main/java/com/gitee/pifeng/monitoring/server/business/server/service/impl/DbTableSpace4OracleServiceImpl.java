package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.gitee.pifeng.monitoring.common.constant.sql.Oracle;
import com.gitee.pifeng.monitoring.common.util.db.DbUtils;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbTableSpace4OracleService;
import lombok.Cleanup;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Oracle数据库表空间服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:11
 */
@Service
public class DbTableSpace4OracleServiceImpl implements IDbTableSpace4OracleService {

    /**
     * <p>
     * 获取表空间列表(按文件)
     * </p>
     *
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @return 表空间列表(按文件)
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    @Override
    public List<Entity> getTableSpaceListFile(String url, String username, String password) throws SQLException {
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        return SqlExecutor.query(connection, Oracle.TABLE_SPACE_SELECT_FILE, new EntityListHandler());
    }

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param url      url
     * @param username 用户名
     * @param password 密码
     * @return 表空间列表
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    @Override
    public List<Entity> getTableSpaceListAll(String url, String username, String password) throws SQLException {
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        return SqlExecutor.query(connection, Oracle.TABLE_SPACE_SELECT_ALL, new EntityListHandler());
    }

}
