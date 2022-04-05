package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerDiskHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerDiskHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerDiskHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器磁盘历史记录服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:31
 */
@Service
public class ServerDiskHistoryServiceImpl extends ServiceImpl<IMonitorServerDiskHistoryDao, MonitorServerDiskHistory> implements IServerDiskHistoryService {
}
