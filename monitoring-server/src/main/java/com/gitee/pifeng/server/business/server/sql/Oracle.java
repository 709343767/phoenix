package com.gitee.pifeng.server.business.server.sql;

/**
 * <p>
 * Oracle数据库sql语句
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/20 16:00
 */
public class Oracle {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/12/20 16:00
     */
    private Oracle() {
    }

    /**
     * 检查连接
     */
    public static final String CHECK_CONN = "SELECT 1 FROM DUAL";
    
}
