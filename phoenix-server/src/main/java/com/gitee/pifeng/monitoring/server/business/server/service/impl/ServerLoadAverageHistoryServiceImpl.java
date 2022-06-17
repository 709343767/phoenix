package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerLoadAverageHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverageHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerLoadAverageHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服服务器平均负载历史记录服务层接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 15:22
 */
@Service
public class ServerLoadAverageHistoryServiceImpl extends ServiceImpl<IMonitorServerLoadAverageHistoryDao, MonitorServerLoadAverageHistory> implements IServerLoadAverageHistoryService {
}
