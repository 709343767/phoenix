package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorRoleDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRole;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRoleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorRoleVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 监控用户角色服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/14 9:18
 */
@Service
public class MonitorRoleServiceImpl extends ServiceImpl<IMonitorRoleDao, MonitorRole> implements IMonitorRoleService {

    /**
     * 监控用户角色数据访问对象
     */
    @Autowired
    private IMonitorRoleDao monitorRoleDao;

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
    @Override
    public Page<MonitorRoleVo> getMonitorRoleList(Long current, Long size, Long roleId) {
        // 查询数据库
        IPage<MonitorRole> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (roleId != null) {
            lambdaQueryWrapper.eq(MonitorRole::getId, roleId);
        }
        IPage<MonitorRole> monitorRolePage = this.monitorRoleDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorRole> monitorRoles = monitorRolePage.getRecords();
        // 转换成监控角色表现层对象
        List<MonitorRoleVo> monitorRoleVos = Lists.newLinkedList();
        for (MonitorRole monitorRole : monitorRoles) {
            MonitorRoleVo monitorRoleVo = MonitorRoleVo.builder().build().convertFor(monitorRole);
            monitorRoleVos.add(monitorRoleVo);
        }
        // 设置返回对象
        Page<MonitorRoleVo> monitorRoleVoPage = new Page<>();
        monitorRoleVoPage.setRecords(monitorRoleVos);
        monitorRoleVoPage.setTotal(monitorRolePage.getTotal());
        return monitorRoleVoPage;
    }
}
