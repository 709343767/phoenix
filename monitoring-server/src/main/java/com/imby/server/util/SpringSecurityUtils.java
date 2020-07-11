package com.imby.server.util;

import com.imby.server.business.web.realm.MonitorUserRealm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * springSecurity工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/7 19:11
 */
public class SpringSecurityUtils {

    /**
     * <p>
     * 获取当前用户的认证信息
     * </p>
     *
     * @return {@link Authentication}
     * @author 皮锋
     * @custom.date 2020/7/7 19:15
     */
    public static Authentication getCurrentUserAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * <p>
     * 获取当前用户
     * </p>
     *
     * @return {@link MonitorUserRealm}
     * @author 皮锋
     * @custom.date 2020/7/7 19:16
     */
    public static MonitorUserRealm getCurrentMonitorUserRealm() {
        return (MonitorUserRealm) getCurrentUserAuthentication().getPrincipal();
    }
}
