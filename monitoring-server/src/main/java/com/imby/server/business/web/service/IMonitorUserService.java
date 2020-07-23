package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorUser;
import com.imby.server.business.web.vo.MonitorUserVo;

/**
 * <p>
 * 监控用户服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 17:39
 */
public interface IMonitorUserService extends IService<MonitorUser> {

    /**
     * <p>
     * 校验密码是否正确
     * </p>
     *
     * @param password 密码
     * @return 密码是否校验成功
     * @author 皮锋
     * @custom.date 2020/7/8 16:59
     */
    boolean verifyPassword(String password);

    /**
     * <p>
     * 修改密码
     * </p>
     *
     * @param password 密码
     * @return 密码是否修改成功
     * @author 皮锋
     * @custom.date 2020/7/11 15:27
     */
    boolean updatePassword(String password);

    /**
     * <p>
     * 修改用户信息
     * </p>
     *
     * @param monitorUserVo 用户信息
     * @return 用户信息是否修改成功
     * @author 皮锋
     * @custom.date 2020/7/13 18:58
     */
    boolean updateUser(MonitorUserVo monitorUserVo);

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
    Page<MonitorUserVo> getMonitorUserList(long current, long size, String account, String username, String email);
}
