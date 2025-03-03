package com.gitee.pifeng.monitoring.server.util.db;

import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

/**
 * <p>
 * 数据库工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/29 21:16
 */
@Slf4j
public class DbUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/12/29 21:17
     */
    private DbUtils() {
    }

    /**
     * <p>
     * 获取数据库连接
     * </p>
     *
     * @param url      url
     * @param username 用户名
     * @param password base64编码后的密码
     * @return 数据库连接
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/29 21:17
     */
    public static Connection getConnection(String url, String username, String password) throws SQLException {
        String pwd = new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
        // 数据源
        try (SimpleDataSource ds = new SimpleDataSource(url, username, pwd)) {
            return ds.getConnection();
        } catch (SQLException e) {
            log.error("与数据库建立连接异常！", e);
            throw e;
        }
    }

}
