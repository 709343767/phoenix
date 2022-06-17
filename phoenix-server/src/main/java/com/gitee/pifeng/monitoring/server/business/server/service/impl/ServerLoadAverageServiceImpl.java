package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerLoadAverageDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverage;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerLoadAverageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服服务器平均负载服务层接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 14:54
 */
@Service
public class ServerLoadAverageServiceImpl extends ServiceImpl<IMonitorServerLoadAverageDao, MonitorServerLoadAverage> implements IServerLoadAverageService {
}
