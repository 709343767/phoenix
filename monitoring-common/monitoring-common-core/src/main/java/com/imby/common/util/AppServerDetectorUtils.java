package com.imby.common.util;

import com.imby.common.constant.AppServerTypeEnums;
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
        if (ServerDetector.isJBoss()) {
            return AppServerTypeEnums.JBOSS;
        }
        if (ServerDetector.isWebSphere()) {
            return AppServerTypeEnums.WEBSPHERE;
        }
        return AppServerTypeEnums.UNKNOWN;
    }

}
