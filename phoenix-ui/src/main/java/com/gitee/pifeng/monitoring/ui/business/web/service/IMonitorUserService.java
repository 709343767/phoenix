package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.exception.NotFoundUserException;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorUser;
import com.gitee.pifeng.monitoring.ui.business.web.realm.MonitorUserRealm;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorUserVo;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 * 监控用户服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 17:39
 */
public interface IMonitorUserService extends IService<MonitorUser>, AuthenticationUserDetailsService<CasAssertionAuthenticationToken>, UserDetailsService {

    /**
     * <p>
     * 获取用户权限信息
     * </p>
     *
     * @param monitorUser 监控用户
     * @return 用户权限信息
     * @author 皮锋
     * @custom.date 2021/10/9 9:25
     */
    List<GrantedAuthority> getGrantedAuthorities(MonitorUser monitorUser);

    /**
     * <p>
     * 根据条件获取监控用户域
     * </p>
     *
     * @param ids 用户主键ID
     * @return {@link MonitorUserRealm}
     * @throws NotFoundUserException 找不到用户异常
     * @author 皮锋
     * @custom.date 2021/10/8 17:36
     */
    List<MonitorUserRealm> getMonitorUserRealms(List<Long> ids) throws NotFoundUserException;

    /**
     * <p>
     * 修改密码
     * </p>
     *
     * @param oldPassword 原始密码
     * @param password    新密码
     * @return LayUiAdmin响应对象：如果原始密码校验失败，LayUiAdminResultVo.data="verifyFail"；
     * 如果修改密码成功，LayUiAdminResultVo.data="success"；
     * 否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/7/11 15:27
     */
    LayUiAdminResultVo updatePassword(String oldPassword, String password);

    /**
     * <p>
     * 修改当前用户信息
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return LayUiAdmin响应对象：如果修改用户信息成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/7/13 18:58
     */
    LayUiAdminResultVo updateUser(MonitorUserVo monitorUserVo);

    /**
     * <p>
     * 获取监控用户列表
     * </p>
     *
     * @param current  当前页
     * @param size     每页显示条数
     * @param account  账号
     * @param username 用户名
     * @param email    电子邮箱
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/7/23 16:37
     */
    Page<MonitorUserVo> getMonitorUserList(Long current, Long size, String account, String username, String email);

    /**
     * <p>
     * 添加用户
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return layUiAdmin响应对象：如果数据库中已经有此账号，LayUiAdminResultVo.data="exist"；
     * 如果添加用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/1 21:33
     */
    LayUiAdminResultVo saveUser(MonitorUserVo monitorUserVo);

    /**
     * <p>
     * 编辑用户
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return layUiAdmin响应对象：如果编辑用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/2 20:44
     */
    LayUiAdminResultVo editUser(MonitorUserVo monitorUserVo);

    /**
     * <p>
     * 删除用户
     * </p>
     *
     * @param ids 用户ID集合
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NotFoundUserException 找不到用户异常
     * @author 皮锋
     * @custom.date 2020/8/2 17:35
     */
    LayUiAdminResultVo deleteUser(List<Long> ids) throws NotFoundUserException;
}
