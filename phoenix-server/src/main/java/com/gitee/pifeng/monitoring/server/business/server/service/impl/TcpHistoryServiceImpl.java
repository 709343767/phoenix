package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorTcpHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TCP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Service
public class TcpHistoryServiceImpl extends ServiceImpl<IMonitorTcpHistoryDao, MonitorTcpHistory> implements ITcpHistoryService {

}
