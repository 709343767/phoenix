package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmThreadDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmThread;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmThreadService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机线程信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:12
 */
@Service
public class JvmThreadServiceImpl extends ServiceImpl<IMonitorJvmThreadDao, MonitorJvmThread> implements IJvmThreadService {
}
