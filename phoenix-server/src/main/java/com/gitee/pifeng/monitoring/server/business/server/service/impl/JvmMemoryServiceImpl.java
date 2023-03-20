package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmMemoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemory;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmMemoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机内存信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:06
 */
@Service
public class JvmMemoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryDao, MonitorJvmMemory> implements IJvmMemoryService {
}
