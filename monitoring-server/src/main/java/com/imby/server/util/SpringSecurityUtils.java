package com.imby.server.util;

import com.imby.server.business.web.realm.MonitorUserRealm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

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
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private SpringSecurityUtils() {
    }

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

    /**
     * <p>
     * 更新当前用户
     * </p>
     *
     * @param monitorUserRealm {@link MonitorUserRealm}
     * @author 皮锋
     * @custom.date 2020/8/4 9:58
     */
    public static void updateCurrentMonitorUserRealm(MonitorUserRealm monitorUserRealm) {
        // 证书
        Object credentials = monitorUserRealm.getPassword();
        // 权限
        Collection<GrantedAuthority> authorities = monitorUserRealm.getAuthorities();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(monitorUserRealm, credentials, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
