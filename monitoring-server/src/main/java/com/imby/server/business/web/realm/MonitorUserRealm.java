package com.imby.server.business.web.realm;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * <p>
 * 监控用户域
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/6 19:18
 */
@Getter
public class MonitorUserRealm extends User {

    /**
     * 用户名
     */
    private String usrname;

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @param usrname  用户名
     * @param account  账号
     * @param password 密码
     * @author 皮锋
     * @custom.date 2020/7/6 19:27
     */
    public MonitorUserRealm(String usrname, String account, String password, Collection<? extends GrantedAuthority> authorities) {
        super(account, password, authorities);
        this.usrname = usrname;
    }
}
