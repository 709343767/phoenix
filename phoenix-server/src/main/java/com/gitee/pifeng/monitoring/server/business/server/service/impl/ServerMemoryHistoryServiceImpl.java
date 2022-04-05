package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerMemoryHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerMemoryHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerMemoryHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器内存历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:10
 */
@Service
public class ServerMemoryHistoryServiceImpl extends ServiceImpl<IMonitorServerMemoryHistoryDao, MonitorServerMemoryHistory> implements IServerMemoryHistoryService {
}
