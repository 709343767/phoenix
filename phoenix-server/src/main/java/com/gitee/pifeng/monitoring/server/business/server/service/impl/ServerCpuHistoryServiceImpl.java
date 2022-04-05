package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerCpuHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpuHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerCpuHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器CPU历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:16
 */
@Service
public class ServerCpuHistoryServiceImpl extends ServiceImpl<IMonitorServerCpuHistoryDao, MonitorServerCpuHistory> implements IServerCpuHistoryService {
}
