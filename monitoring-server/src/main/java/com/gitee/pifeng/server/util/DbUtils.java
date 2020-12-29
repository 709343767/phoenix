package com.gitee.pifeng.server.util;

import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.Cleanup;

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
     * @param password 密码
     * @return 数据库连接
     * @author 皮锋
     * @custom.date 2020/12/29 21:17
     */
    public static Connection getConnection(String url, String username, String password) throws SQLException {
        String pwd = new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
        // 数据源
        @Cleanup
        SimpleDataSource ds = new SimpleDataSource(url, username, pwd);
        return ds.getConnection();
    }

}
