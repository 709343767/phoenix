package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorRole;
import com.imby.server.business.web.vo.MonitorRoleVo;

/**
 * <p>
 * 监控用户角色服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/14 9:17
 */
public interface IMonitorRoleService extends IService<MonitorRole> {

    /**
     * <p>
     * 获取监控角色列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param roleId  角色ID
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    Page<MonitorRoleVo> getMonitorRoleList(Long current, Long size, Long roleId);
}
