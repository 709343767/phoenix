package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerNetcardHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerNetcardHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerNetcardHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器网卡历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:22
 */
@Service
public class ServerNetcardHistoryServiceImpl extends ServiceImpl<IMonitorServerNetcardHistoryDao, MonitorServerNetcardHistory> implements IServerNetcardHistoryService {
}
