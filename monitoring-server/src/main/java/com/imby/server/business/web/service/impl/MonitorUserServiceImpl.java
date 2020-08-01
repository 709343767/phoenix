package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorRoleDao;
import com.imby.server.business.web.dao.IMonitorUserDao;
import com.imby.server.business.web.entity.MonitorRole;
import com.imby.server.business.web.entity.MonitorUser;
import com.imby.server.business.web.realm.MonitorUserRealm;
import com.imby.server.business.web.service.IMonitorUserService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorUserVo;
import com.imby.server.util.SpringSecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 监控用户服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 17:39
 */
@Service
public class MonitorUserServiceImpl extends ServiceImpl<IMonitorUserDao, MonitorUser> implements IMonitorUserService, UserDetailsService {

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
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        // 根据username在数据库中查询用户
        LambdaQueryWrapper<MonitorUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(MonitorUser::getAccount, account);
        MonitorUser monitorUser = this.monitorUserDao.selectOne(userQueryWrapper);
        // 用户为空
        if (monitorUser == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
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
        return new MonitorUserRealm(monitorUser.getId(), monitorUser.getUsername(), account, monitorUser.getPassword(),
                monitorUser.getRoleId(), monitorUser.getRegisterTime(), monitorUser.getUpdateTime(), monitorUser.getEmail(),
                monitorUser.getRemarks(), grantedAuthorityList);
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
        // 一.校验密码是否正确
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
            return LayUiAdminResultVo.ok("verifyFail");
        }
        // 二.修改密码
        // 加密密码
        String enPassword = bc.encode(password);
        // 更新数据库
        LambdaUpdateWrapper<MonitorUser> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorUser::getId, userId).set(MonitorUser::getPassword, enPassword);
        int result = this.monitorUserDao.update(null, lambdaUpdateWrapper);
        if (result == 1) {
            return LayUiAdminResultVo.ok("success");
        }
        return LayUiAdminResultVo.ok("fail");
    }

    /**
     * <p>
     * 修改用户信息
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return LayUiAdmin响应对象：如果修改用户信息成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/7/13 18:58
     */
    @Override
    public LayUiAdminResultVo updateUser(MonitorUserVo monitorUserVo) {
        MonitorUser monitorUser = monitorUserVo.convertToMonitorUser();
        // 设置更新时间
        monitorUser.setUpdateTime(new Date());
        int result = this.monitorUserDao.updateById(monitorUser);
        if (result == 1) {
            return LayUiAdminResultVo.ok("success");
        }
        return LayUiAdminResultVo.ok("fail");
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
    public Page<MonitorUserVo> getMonitorUserList(long current, long size, String account, String username, String email) {
        // 查询数据库
        IPage<MonitorUser> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(account)) {
            lambdaQueryWrapper.eq(MonitorUser::getAccount, account);
        }
        if (StringUtils.isNotBlank(account)) {
            lambdaQueryWrapper.like(MonitorUser::getUsername, username);
        }
        if (StringUtils.isNotBlank(account)) {
            lambdaQueryWrapper.like(MonitorUser::getEmail, email);
        }
        IPage<MonitorUser> monitorUserPage = this.monitorUserDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorRole> monitorRoles = this.monitorRoleDao.selectList(new QueryWrapper<>());
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
}
