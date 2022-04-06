package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmMemoryHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemoryHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmMemoryHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机内存历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/6 9:57
 */
@Service
public class JvmMemoryHistoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryHistoryDao, MonitorJvmMemoryHistory> implements IJvmMemoryHistoryService {
}
