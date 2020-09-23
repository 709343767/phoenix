package com.imby.common.constant;

/**
 * <p>
 * 告警类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/1 20:04
 */
public enum AlarmTypeEnums {

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
     * 自定义，如果是自己的业务告警，设置此类型
     */
    CUSTOM

}
