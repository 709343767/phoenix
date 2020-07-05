package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorRoleDao;
import com.imby.server.business.web.dao.IMonitorUserDao;
import com.imby.server.business.web.entity.MonitorRole;
import com.imby.server.business.web.entity.MonitorUser;
import com.imby.server.business.web.service.IMonitorUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 根据用户名获取用户
     * </p>
     *
     * @param username 用户名
     * @return {@link UserDetails}
     * @author 皮锋
     * @custom.date 2020/7/5 14:05
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username在数据库中查询用户
        LambdaQueryWrapper<MonitorUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(MonitorUser::getUserName, username);
        MonitorUser monitorUser = this.monitorUserDao.selectOne(userQueryWrapper);

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
        return new User(username, monitorUser.getPassword(), grantedAuthorityList);
    }
}
