package com.gitee.pifeng.monitoring.common.constant;

/**
 * <p>
 * 监控类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/1 20:04
 */
public enum MonitorTypeEnums {

    /**
     * 数据库
     */
    DATABASE,

    /**
     * 服务器
     */
    SERVER,

    /**
     * 网络
     */
    NET,

    /**
     * 应用实例
     */
    INSTANCE,

    /**
     * 自定义，如果是自己的业务监控，设置此类型
     */
    CUSTOM

}
