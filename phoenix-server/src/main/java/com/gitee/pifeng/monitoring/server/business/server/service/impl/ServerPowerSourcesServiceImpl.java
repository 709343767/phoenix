package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerPowerSourcesDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerPowerSources;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerPowerSourcesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器电池信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:00
 */
@Service
public class ServerPowerSourcesServiceImpl extends ServiceImpl<IMonitorServerPowerSourcesDao, MonitorServerPowerSources> implements IServerPowerSourcesService {
}
