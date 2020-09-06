package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorJvmMemory;
import com.imby.server.business.web.dao.IMonitorJvmMemoryDao;
import com.imby.server.business.web.service.IMonitorJvmMemoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机内存信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmMemoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryDao, MonitorJvmMemory> implements IMonitorJvmMemoryService {

}
