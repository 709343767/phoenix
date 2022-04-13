package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorHttpHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * HTTP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:07
 */
@Service
public class HttpHistoryServiceImpl extends ServiceImpl<IMonitorHttpHistoryDao, MonitorHttpHistory> implements IHttpHistoryService {
}
