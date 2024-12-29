package com.gitee.pifeng.monitoring.common.constant.monitortype;

/**
 * <p>
 * 监控子类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/12/19 12:26
 */
public enum MonitorSubTypeEnums {

    /**
     * 服务状态
     */
    SERVICE_STATUS,

    /**
     * 空（无）
     */
    EMPTY,

    /* *********************************************** 服务器监控子类型 *********************************************** */

    /**
     * 服务器监控子类型————CPU
     */
    SERVER__CPU,

    /**
     * 服务器监控子类型————磁盘
     */
    SERVER__DISK,

    /**
     * 服务器监控子类型————平均负载
     */
    SERVER__LOAD_AVERAGE,

    /**
     * 服务器监控子类型————内存
     */
    SERVER__MEMORY,

    /* *********************************************** 数据库监控子类型 *********************************************** */

    /**
     * 数据库监控子类型————表空间
     */
    DATABASE__TABLE_SPACE

}
