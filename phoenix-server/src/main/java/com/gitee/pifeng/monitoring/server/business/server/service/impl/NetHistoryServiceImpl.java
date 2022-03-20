package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorNetHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.INetHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网络信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Service
public class NetHistoryServiceImpl extends ServiceImpl<IMonitorNetHistoryDao, MonitorNetHistory> implements INetHistoryService {

}
