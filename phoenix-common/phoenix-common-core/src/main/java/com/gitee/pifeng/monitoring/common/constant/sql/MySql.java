package com.gitee.pifeng.monitoring.common.constant.sql;

/**
 * <p>
 * MySQL数据库sql语句
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/20 15:44
 */
public final class MySql {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/12/20 15:45
     */
    private MySql() {
    }

    /**
     * 检查连接
     */
    public static final String CHECK_CONN = "SELECT 1";

    /**
     * 会话列表
     */
    public static final String SESSION_LIST = "show full processlist";

    /**
     * 结束会话
     */
    public static final String KILL_SESSION = "kill ?";

}
