package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorTcpIpHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpIpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpIpHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TCP/IP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Service
public class TcpIpHistoryServiceImpl extends ServiceImpl<IMonitorTcpIpHistoryDao, MonitorTcpIpHistory> implements ITcpIpHistoryService {

}
