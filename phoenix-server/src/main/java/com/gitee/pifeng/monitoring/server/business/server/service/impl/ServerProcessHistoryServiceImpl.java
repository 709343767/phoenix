package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerProcessHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerProcessHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerProcessHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器进程历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:36
 */
@Service
public class ServerProcessHistoryServiceImpl extends ServiceImpl<IMonitorServerProcessHistoryDao, MonitorServerProcessHistory> implements IServerProcessHistoryService {
}
