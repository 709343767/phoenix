package com.gitee.pifeng.monitoring.common.constant;

import com.gitee.pifeng.monitoring.common.exception.MonitoringUniversalException;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 数据库类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/15 20:15
 */
public enum DbEnums {

    /**
     * Oracle
     */
    Oracle,

    /**
     * MySQL
     */
    MySQL,

    /**
     * Redis
     */
    Redis,

    /**
     * Mongo
     */
    Mongo;

    /**
     * <p>
     * 数据库类型字符串转数据库类型枚举
     * </p>
     *
     * @param dbTypeStr 数据库类型字符串
     * @return 数据库类型枚举
     * @author 皮锋
     * @custom.date 2025/1/8 17:31
     */
    public static DbEnums str2Enum(String dbTypeStr) {
        // Oracle
        if (StringUtils.equalsIgnoreCase(DbEnums.Oracle.name(), dbTypeStr)) {
            return DbEnums.Oracle;
        }
        // MySQL 协议
        if (StringUtils.equalsIgnoreCase(DbEnums.MySQL.name(), dbTypeStr)) {
            return DbEnums.MySQL;
        }
        // Redis 协议
        if (StringUtils.equalsIgnoreCase(DbEnums.Redis.name(), dbTypeStr)) {
            return DbEnums.Redis;
        }
        // Mongo 协议
        if (StringUtils.equalsIgnoreCase(DbEnums.Mongo.name(), dbTypeStr)) {
            return DbEnums.Mongo;
        }
        throw new MonitoringUniversalException("未知的数据库类型！");
    }

}
