package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorJvmGarbageCollector;
import com.imby.server.business.web.dao.IMonitorJvmGarbageCollectorDao;
import com.imby.server.business.web.service.IMonitorJvmGarbageCollectorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机GC信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmGarbageCollectorServiceImpl extends ServiceImpl<IMonitorJvmGarbageCollectorDao, MonitorJvmGarbageCollector> implements IMonitorJvmGarbageCollectorService {

}
