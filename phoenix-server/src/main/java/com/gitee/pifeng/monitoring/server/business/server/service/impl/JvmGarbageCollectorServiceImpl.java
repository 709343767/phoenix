package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmGarbageCollectorDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmGarbageCollector;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmGarbageCollectorService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机GC信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:17
 */
@Service
public class JvmGarbageCollectorServiceImpl extends ServiceImpl<IMonitorJvmGarbageCollectorDao, MonitorJvmGarbageCollector> implements IJvmGarbageCollectorService {
}
