package com.gitee.pifeng.server.business.web.realm;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

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
     * serialVersionUID
     */
    private static final long serialVersionUID = 1405360851679996927L;

    /**
     * 用户ID
     */
    private final Long id;

    /**
     * 用户名
     */
    private final String usrname;

    /**
     * 角色ID
     */
    private final Long roleId;

    /**
     * 注册时间
     */
    private final Date registerTime;

    /**
     * 更新时间
     */
    private final Date updateTime;

    /**
     * 电子邮箱
     */
    private final String email;

    /**
     * 备注
     */
    private final String remarks;

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @param id           用户ID
     * @param usrname      用户名
     * @param account      账号
     * @param password     密码
     * @param roleId       角色ID
     * @param registerTime 注册时间
     * @param updateTime   更新时间
     * @param email        电子邮箱
     * @param remarks      备注
     * @param authorities  权限
     * @author 皮锋
     * @custom.date 2020/7/6 19:27
     */
    public MonitorUserRealm(Long id, String usrname, String account, String password, Long roleId, Date registerTime,
                            Date updateTime, String email, String remarks, Collection<? extends GrantedAuthority> authorities) {
        super(account, password, authorities);
        this.id = id;
        this.usrname = usrname;
        this.roleId = roleId;
        this.registerTime = registerTime;
        this.updateTime = updateTime;
        this.email = email;
        this.remarks = remarks;
    }
}
