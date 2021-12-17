package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerMemoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerMemory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerMemoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器内存信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 14:14
 */
@Service
public class ServerMemoryServiceImpl extends ServiceImpl<IMonitorServerMemoryDao, MonitorServerMemory> implements IServerMemoryService {
}
