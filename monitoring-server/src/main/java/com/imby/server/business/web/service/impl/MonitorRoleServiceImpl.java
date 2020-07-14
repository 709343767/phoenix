package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorRoleDao;
import com.imby.server.business.web.entity.MonitorRole;
import com.imby.server.business.web.service.IMonitorRoleService;
import org.springframework.stereotype.Service;

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
}
