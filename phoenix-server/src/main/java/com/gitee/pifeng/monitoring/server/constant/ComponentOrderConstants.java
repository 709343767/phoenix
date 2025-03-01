package com.gitee.pifeng.monitoring.server.constant;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * Spring 组件的执行顺序{@link Order}
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025年1月15日 下午20:44:30
 */
public class ComponentOrderConstants {

    /**
     * 应用程序
     */
    public static final int INSTANCE = 100;

    /**
     * 服务器
     */
    public static final int SERVER = 200;

    /**
     * 网络信息
     */
    public static final int NET = 300;

    /**
     * 数据库
     */
    public static final int DB = 400;

    /**
     * TCP服务
     */
    public static final int TCP = 500;

    /**
     * HTTP服务
     */
    public static final int HTTP = 600;

    /**
     * 其它
     */
    public static final int OTHER = Ordered.LOWEST_PRECEDENCE - 1000;

}
