package com.gitee.pifeng.server.constant.sql;

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
    public static final String SESSION_LIST = "SELECT s.SID AS SID,s.SERIAL# AS SERIAL# ,s.USERNAME AS USERNAME,s.SCHEMANAME AS SCHEMANAME,s.TYPE AS TYPE," +
            "s.STATE AS STATE,s.LOGON_TIME AS LOGONTIME,s.MACHINE AS MACHINE,s.OSUSER AS OSUSER,s.PROGRAM AS PROGRAM," +
            "s.EVENT AS EVENT,s.SECONDS_IN_WAIT AS WAITTIME, to_char(substr(sq.SQL_FULLTEXT,0,2000)) AS SQL " +
            "FROM GV$SESSION s, gv$sql sq " +
            "WHERE s.sql_address = sq.address(+) " +
            "AND s.sql_hash_value = sq.hash_value(+)  AND s.sql_child_number = sq.child_number AND s.TYPE = 'USER' ORDER BY s.SID ASC";

    /**
     * 结束会话
     */
    public static final String KILL_SESSION = "alter system kill session 'sid, serial#' immediate";

    /**
     * 查询表空间
     */
    public static final String TABLE_SPACE_SELECT = "SELECT " +
            "b.file_id fileId, " +
            "b.file_name fileName, " +
            "b.tablespace_name tablespaceName, " +
            "b.bytes total, " +
            "(b.bytes-sum(nvl(a.bytes, 0))) used, " +
            "sum(nvl(a.bytes, 0)) free, " +
            "sum(nvl(a.bytes, 0))/(b.bytes)* 100 freePer " +
            "FROM " +
            "dba_free_space a, " +
            "dba_data_files b " +
            "WHERE " +
            "a.file_id = b.file_id " +
            "GROUP BY " +
            "b.tablespace_name, " +
            "b.file_name, " +
            "b.file_id, " +
            "b.bytes " +
            "ORDER BY " +
            "b.tablespace_name";

}
