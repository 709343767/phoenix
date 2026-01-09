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
            "S.SID AS SID, " +
            "S.SERIAL# AS SERIAL# , " +
            "S.USERNAME AS USERNAME, " +
            "S.SCHEMANAME AS SCHEMANAME, " +
            "S.TYPE AS TYPE, " +
            "S.STATE AS STATE, " +
            "S.LOGON_TIME AS LOGONTIME, " +
            "S.MACHINE AS MACHINE, " +
            "S.OSUSER AS OSUSER, " +
            "S.PROGRAM AS PROGRAM, " +
            "S.EVENT AS EVENT, " +
            "S.SECONDS_IN_WAIT AS WAITTIME, " +
            "S.LAST_CALL_ET AS LAST_CALL_ET, " +
            "TO_CHAR(SUBSTR(SQ.SQL_FULLTEXT, 0, 2000)) AS SQL " +
            "FROM " +
            "GV$SESSION S, " +
            "GV$SQL SQ " +
            "WHERE " +
            "S.SQL_ADDRESS = SQ.ADDRESS(+) " +
            "AND S.SQL_HASH_VALUE = SQ.HASH_VALUE(+) " +
            "AND S.SQL_CHILD_NUMBER = SQ.CHILD_NUMBER(+) " +
            "AND S.TYPE = 'USER' " +
            "ORDER BY " +
            "S.SID ASC";

    /**
     * 结束会话
     */
    public static final String KILL_SESSION = "ALTER SYSTEM KILL SESSION 'SID, SERIAL#' IMMEDIATE";

    /**
     * 查询表空间（按文件）
     */
    public static final String TABLE_SPACE_SELECT_FILE = "SELECT " +
            "A.FILE_ID FILEID, " +
            "A.FILE_NAME FILENAME, " +
            "A.TABLESPACE_NAME TABLESPACENAME, " +
            "A.BYTES TOTAL, " +
            "(A.BYTES-SUM(NVL(B.BYTES, 0))) USED, " +
            "SUM(NVL(B.BYTES, 0)) FREE, " +
            "SUM(NVL(B.BYTES, 0))/(A.BYTES)* 100 FREEPER " +
            "FROM " +
            "DBA_DATA_FILES A, " +
            "DBA_FREE_SPACE B " +
            "WHERE " +
            "A.FILE_ID = B.FILE_ID(+) " +
            "GROUP BY " +
            "A.TABLESPACE_NAME, " +
            "A.FILE_NAME, " +
            "A.FILE_ID, " +
            "A.BYTES " +
            "ORDER BY " +
            "A.TABLESPACE_NAME, " +
            "A.FILE_ID";

    /**
     * 查询表空间（汇总）
     */
    public static final String TABLE_SPACE_SELECT_ALL = "SELECT " +
            "A.TABLESPACE_NAME TABLESPACENAME, " +
            "A.BYTES TOTAL, " +
            "B.BYTES USED, " +
            "C.BYTES FREE, " +
            "(B.BYTES * 100) / A.BYTES USEDRATE, " +
            "(C.BYTES * 100) / A.BYTES FREERATE " +
            "FROM " +
            "SYS.SM$TS_AVAIL A, " +
            "SYS.SM$TS_USED B, " +
            "SYS.SM$TS_FREE C " +
            "WHERE " +
            "A.TABLESPACE_NAME = B.TABLESPACE_NAME " +
            "AND A.TABLESPACE_NAME = C.TABLESPACE_NAME";

}
