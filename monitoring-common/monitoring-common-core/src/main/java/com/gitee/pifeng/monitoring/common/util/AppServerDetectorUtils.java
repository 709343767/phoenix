package com.gitee.pifeng.monitoring.common.util;

import com.gitee.pifeng.monitoring.common.constant.AppServerTypeEnums;
import com.liferay.portal.kernel.util.ServerDetector;

/**
 * <p>
 * 应用服务器工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/5 21:12
 */
public class AppServerDetectorUtils {

    /**
     * <p>
     * 构造方法私有化
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/12/5 22:25
     */
    private AppServerDetectorUtils() {
    }

    public static final String JETTY_CLASS = "/org/mortbay/jetty/Server.class";

    public static final String UNDERTOW_CLASS = "/io/undertow/Undertow.class";

    /**
     * <p>
     * 获取应用服务器枚举类型
     * </p>
     *
     * @return {@link AppServerTypeEnums}
     * @author 皮锋
     * @custom.date 2020/12/5 21:36
     */
    public static AppServerTypeEnums getAppServerTypeEnum() {
        if (ServerDetector.isTomcat()) {
            return AppServerTypeEnums.TOMCAT;
        }
        if (ServerDetector.isWebLogic()) {
            return AppServerTypeEnums.WEBLOGIC;
        }
        if (isUndertow()) {
            return AppServerTypeEnums.UNDERTOW;
        }
        if (isJetty()) {
            return AppServerTypeEnums.JETTY;
        }
        return AppServerTypeEnums.UNKNOWN;
    }

    /**
     * <p>
     * 是不是Jetty服务器
     * </p>
     *
     * @return 是 或者 否
     * @author 皮锋
     * @custom.date 2020/12/6 11:34
     */
    private static boolean isJetty() {
        Class<?> c = AppServerDetectorUtils.class;
        return c.getResource(JETTY_CLASS) != null;
    }

    /**
     * <p>
     * 是不是Undertow服务器
     * </p>
     *
     * @return 是 或者 否
     * @author 皮锋
     * @custom.date 2020/12/6 11:35
     */
    private static boolean isUndertow() {
        Class<?> c = AppServerDetectorUtils.class;
        return c.getResource(UNDERTOW_CLASS) != null;
    }

}
