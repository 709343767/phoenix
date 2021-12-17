package com.gitee.pifeng.monitoring.common.constant.sql;

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

    /**
     * 会话列表
     */
    public static final String SESSION_LIST = "SELECT " +
            "s.SID AS SID, " +
            "s.SERIAL# AS SERIAL# , " +
            "s.USERNAME AS USERNAME, " +
            "s.SCHEMANAME AS SCHEMANAME, " +
            "s.TYPE AS TYPE, " +
            "s.STATE AS STATE, " +
            "s.LOGON_TIME AS LOGONTIME, " +
            "s.MACHINE AS MACHINE, " +
            "s.OSUSER AS OSUSER, " +
            "s.PROGRAM AS PROGRAM, " +
            "s.EVENT AS EVENT, " +
            "s.SECONDS_IN_WAIT AS WAITTIME, " +
            "to_char(substr(sq.SQL_FULLTEXT, 0, 2000)) AS SQL " +
            "FROM " +
            "GV$SESSION s, " +
            "GV$sql sq " +
            "WHERE " +
            "s.sql_address = sq.address(+) " +
            "AND s.sql_hash_value = sq.hash_value(+) " +
            "AND s.sql_child_number = sq.child_number(+) " +
            "AND s.TYPE = 'USER' " +
            "ORDER BY " +
            "s.SID ASC";

    /**
     * 结束会话
     */
    public static final String KILL_SESSION = "alter system kill session 'sid, serial#' immediate";

    /**
     * 查询表空间（按文件）
     */
    public static final String TABLE_SPACE_SELECT_FILE = "SELECT " +
            "a.file_id fileId, " +
            "a.file_name fileName, " +
            "a.tablespace_name tablespaceName, " +
            "a.bytes total, " +
            "(a.bytes-sum(nvl(b.bytes, 0))) used, " +
            "sum(nvl(b.bytes, 0)) free, " +
            "sum(nvl(b.bytes, 0))/(a.bytes)* 100 freePer " +
            "FROM " +
            "dba_data_files a, " +
            "dba_free_space b " +
            "WHERE " +
            "a.file_id = b.file_id(+) " +
            "GROUP BY " +
            "a.tablespace_name, " +
            "a.file_name, " +
            "a.file_id, " +
            "a.bytes " +
            "ORDER BY " +
            "a.tablespace_name";

    /**
     * 查询表空间（汇总）
     */
    public static final String TABLE_SPACE_SELECT_ALL = "SELECT " +
            "a.tablespace_name tablespaceName, " +
            "a.bytes total, " +
            "b.bytes used, " +
            "c.bytes free, " +
            "(b.bytes * 100) / a.bytes usedRate, " +
            "(c.bytes * 100) / a.bytes freeRate " +
            "FROM " +
            "sys.sm$ts_avail a, " +
            "sys.sm$ts_used b, " +
            "sys.sm$ts_free c " +
            "WHERE " +
            "a.tablespace_name = b.tablespace_name " +
            "AND a.tablespace_name = c.tablespace_name";

}
