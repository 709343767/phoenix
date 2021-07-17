package com.gitee.pifeng.monitoring.ui.util;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.pifeng.monitoring.ui.business.web.realm.MonitorUserRealm;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.Collection;
import java.util.List;

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

    /**
     * <p>
     * 使某些用户的session过期，让用户强制下线
     * </p>
     *
     * @param sessionRegistry {@link SessionRegistry} session注册信息
     * @param userIds         用户ID数组
     * @author 皮锋
     * @custom.date 2021/6/24 17:57
     */
    public static void letUserSessionExpireNow(SessionRegistry sessionRegistry, Long... userIds) {
        List<Object> users = sessionRegistry.getAllPrincipals();
        // 获取session中所有的用户信息
        for (Object principal : users) {
            if (principal instanceof MonitorUserRealm) {
                final MonitorUserRealm loggedUser = (MonitorUserRealm) principal;
                if (ArrayUtil.contains(userIds, loggedUser.getId())) {
                    // false代表不包含过期session
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (CollectionUtils.isNotEmpty(sessionsInfo)) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            // 让session过期
                            sessionInformation.expireNow();
                        }
                    }
                }
            }
        }
    }

}
