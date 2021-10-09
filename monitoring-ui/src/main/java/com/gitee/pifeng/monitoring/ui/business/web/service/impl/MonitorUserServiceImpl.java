package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.exception.NotFoundUserException;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorRoleDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorUserDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRole;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorUser;
import com.gitee.pifeng.monitoring.ui.business.web.realm.MonitorUserRealm;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorUserVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * 监控用户服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 17:39
 */
@Service
public class MonitorUserServiceImpl extends ServiceImpl<IMonitorUserDao, MonitorUser> implements IMonitorUserService {

    /**
     * 监控用户数据访问对象
     */
    @Autowired
    private IMonitorUserDao monitorUserDao;

    /**
     * 监控用户角色数据访问对象
     */
    @Autowired
    private IMonitorRoleDao monitorRoleDao;

    /**
     * session注册信息
     */
    @Autowired
    private SessionRegistry sessionRegistry;

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
    @Override
    public List<GrantedAuthority> getGrantedAuthorities(MonitorUser monitorUser) {
        // 根据角色ID在数据库中查询角色
        LambdaQueryWrapper<MonitorRole> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(MonitorRole::getId, monitorUser.getRoleId());
        List<MonitorRole> monitorRoles = this.monitorRoleDao.selectList(roleQueryWrapper);
        // 设置授权信息
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(monitorRoles)) {
            for (MonitorRole monitorRole : monitorRoles) {
                grantedAuthorityList.add(new SimpleGrantedAuthority(monitorRole.getRoleName()));
            }
        }
        return grantedAuthorityList;
    }

    /**
     * <p>
     * 根据账号获取用户
     * </p>
     *
     * @param account 账号
     * @return {@link UserDetails}
     * @author 皮锋
     * @custom.date 2020/7/5 14:05
     */
    @Override
    public UserDetails loadUserByUsername(String account) {
        // 根据username在数据库中查询用户
        LambdaQueryWrapper<MonitorUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(MonitorUser::getAccount, account);
        MonitorUser monitorUser = this.monitorUserDao.selectOne(userQueryWrapper);
        // 用户为空
        if (monitorUser == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        // 获取用户权限信息
        List<GrantedAuthority> grantedAuthorityList = this.getGrantedAuthorities(monitorUser);
        return new MonitorUserRealm(monitorUser.getId(), monitorUser.getUsername(), account, monitorUser.getPassword(),
                monitorUser.getRoleId(), monitorUser.getRegisterTime(), monitorUser.getUpdateTime(), monitorUser.getEmail(),
                monitorUser.getRemarks(), grantedAuthorityList);
    }

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
    @Override
    public List<MonitorUserRealm> getMonitorUserRealms(List<Long> ids) throws NotFoundUserException {
        LambdaQueryWrapper<MonitorUser> userQueryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            userQueryWrapper.in(MonitorUser::getId, ids);
        }
        List<MonitorUser> monitorUsers = this.monitorUserDao.selectList(userQueryWrapper);
        // 用户为空
        if (CollectionUtils.isEmpty(monitorUsers)) {
            throw new NotFoundUserException("用户不存在！");
        }
        List<MonitorUserRealm> monitorUserRealms = Lists.newArrayList();
        for (MonitorUser monitorUser : monitorUsers) {
            // 获取用户权限信息
            List<GrantedAuthority> grantedAuthorityList = this.getGrantedAuthorities(monitorUser);
            MonitorUserRealm monitorUserRealm = new MonitorUserRealm(monitorUser.getId(), monitorUser.getUsername(),
                    monitorUser.getAccount(), monitorUser.getPassword(), monitorUser.getRoleId(),
                    monitorUser.getRegisterTime(), monitorUser.getUpdateTime(), monitorUser.getEmail(),
                    monitorUser.getRemarks(), grantedAuthorityList);
            // 添加到list
            monitorUserRealms.add(monitorUserRealm);
        }
        return monitorUserRealms;
    }

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
    @Override
    public LayUiAdminResultVo updatePassword(String oldPassword, String password) {
        // 一.解密密码
        oldPassword = new String(Base64.getDecoder().decode(oldPassword), StandardCharsets.UTF_8);
        password = new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
        // 二.校验密码是否正确
        MonitorUserRealm monitorUserRealm = SpringSecurityUtils.getCurrentMonitorUserRealm();
        Long userId = monitorUserRealm.getId();
        // 查询数据库
        MonitorUser monitorUser = this.monitorUserDao.selectById(userId);
        String dbPassword = monitorUser.getPassword();
        // 判断输入的原密码和加密后的密码是否一致
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        boolean verify = bc.matches(oldPassword, dbPassword);
        // 密码不正确
        if (!verify) {
            return LayUiAdminResultVo.ok(WebResponseConstants.VERIFY_FAIL);
        }
        // 三.修改密码
        // 加密密码
        String enPassword = bc.encode(password);
        // 更新数据库
        LambdaUpdateWrapper<MonitorUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorUser::getId, userId).set(MonitorUser::getPassword, enPassword);
        int result = this.monitorUserDao.update(null, lambdaUpdateWrapper);
        if (result == 1) {
            MonitorUserRealm realm = (MonitorUserRealm) this.loadUserByUsername(monitorUser.getAccount());
            // 更新springsecurity中当前用户
            SpringSecurityUtils.updateCurrentMonitorUserRealm(realm);
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

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
    @Override
    public LayUiAdminResultVo updateUser(MonitorUserVo monitorUserVo) {
        MonitorUser monitorUser = monitorUserVo.convertTo();
        // 设置更新时间
        monitorUser.setUpdateTime(new Date());
        int result = this.monitorUserDao.updateById(monitorUser);
        if (result == 1) {
            // 更新springsecurity中当前用户
            MonitorUserRealm realm = (MonitorUserRealm) this.loadUserByUsername(monitorUser.getAccount());
            SpringSecurityUtils.updateCurrentMonitorUserRealm(realm);
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

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
    @Override
    public Page<MonitorUserVo> getMonitorUserList(Long current, Long size, String account, String username, String email) {
        // 查询数据库
        IPage<MonitorUser> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(account)) {
            lambdaQueryWrapper.eq(MonitorUser::getAccount, account);
        }
        if (StringUtils.isNotBlank(username)) {
            lambdaQueryWrapper.like(MonitorUser::getUsername, username);
        }
        if (StringUtils.isNotBlank(email)) {
            lambdaQueryWrapper.like(MonitorUser::getEmail, email);
        }
        IPage<MonitorUser> monitorUserPage = this.monitorUserDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorRole> monitorRoles = this.monitorRoleDao.selectList(new LambdaQueryWrapper<>());
        List<MonitorUser> monitorUsers = monitorUserPage.getRecords();
        // 转换成监控用户表现层对象
        List<MonitorUserVo> monitorUserVos = new LinkedList<>();
        for (MonitorUser monitorUser : monitorUsers) {
            long roleId = monitorUser.getRoleId();
            // 赋予角色名
            for (MonitorRole monitorRole : monitorRoles) {
                long id = monitorRole.getId();
                String roleName = monitorRole.getRoleName();
                if (roleId == id) {
                    MonitorUserVo monitorUserVo = MonitorUserVo.builder().roleName(roleName).build().convertFor(monitorUser);
                    // 屏蔽密码
                    monitorUserVo.setPassword(null);
                    monitorUserVos.add(monitorUserVo);
                }
            }
        }
        // 设置返回对象
        Page<MonitorUserVo> monitorUserVoPage = new Page<>();
        monitorUserVoPage.setRecords(monitorUserVos);
        monitorUserVoPage.setTotal(monitorUserPage.getTotal());
        return monitorUserVoPage;
    }

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
    @Override
    public LayUiAdminResultVo saveUser(MonitorUserVo monitorUserVo) {
        // 判断账号是否已经存在
        LambdaQueryWrapper<MonitorUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorUser::getAccount, monitorUserVo.getAccount());
        MonitorUser dbUser = this.monitorUserDao.selectOne(lambdaQueryWrapper);
        // 数据库中已经有此账号
        if (dbUser != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorUser monitorUser = monitorUserVo.convertTo();
        monitorUser.setRegisterTime(new Date());
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        // 加密密码
        String enPassword = bc.encode(new String(Base64.getDecoder().decode(monitorUser.getPassword()), StandardCharsets.UTF_8));
        monitorUser.setPassword(enPassword);
        int result = this.monitorUserDao.insert(monitorUser);
        // 成功
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

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
    @Override
    public LayUiAdminResultVo editUser(MonitorUserVo monitorUserVo) {
        MonitorUser monitorUser = monitorUserVo.convertTo();
        // 密码
        String password = new String(Base64.getDecoder().decode(monitorUser.getPassword()), StandardCharsets.UTF_8);
        if (StringUtils.isBlank(password)) {
            // mybatis-plus不会更新值为null字段
            monitorUser.setPassword(null);
        } else {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            monitorUser.setPassword(bc.encode(password));
        }
        monitorUser.setUpdateTime(new Date());
        int result = this.monitorUserDao.updateById(monitorUser);
        if (result == 1) {
            Long currentMonitorUserRealmId = SpringSecurityUtils.getCurrentMonitorUserRealm().getId();
            MonitorUserRealm realm = (MonitorUserRealm) this.loadUserByUsername(monitorUser.getAccount());
            // 如果修改的是当前用户
            if (Objects.equals(currentMonitorUserRealmId, monitorUser.getId())) {
                // 更新springsecurity中当前用户
                SpringSecurityUtils.updateCurrentMonitorUserRealm(realm);
            }
            // 如果修改的不是当前用户
            else {
                // 使这个用户的session过期，让用户强制下线
                SpringSecurityUtils.letUserSessionExpireNow(this.sessionRegistry, realm);
            }
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 删除用户
     * </p>
     *
     * @param monitorUserVos 用户信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NotFoundUserException 找不到用户异常
     * @author 皮锋
     * @custom.date 2020/8/2 17:35
     */
    @Override
    @Transactional
    public LayUiAdminResultVo deleteUser(List<MonitorUserVo> monitorUserVos) throws NotFoundUserException {
        int size = monitorUserVos.size();
        List<Long> ids = Lists.newArrayList();
        for (MonitorUserVo monitorUserVo : monitorUserVos) {
            Long id = monitorUserVo.getId();
            ids.add(id);
        }
        List<MonitorUserRealm> monitorUserRealms = this.getMonitorUserRealms(ids);
        int result = this.monitorUserDao.deleteBatchIds(ids);
        if (size == result) {
            // 使这些用户的session过期，让用户强制下线
            SpringSecurityUtils.letUserSessionExpireNow(this.sessionRegistry, monitorUserRealms.toArray());
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

}
