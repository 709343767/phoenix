package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmRuntimeDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmRuntime;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmRuntimeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机运行时信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/12 19:27
 */
@Service
public class JvmRuntimeServiceImpl extends ServiceImpl<IMonitorJvmRuntimeDao, MonitorJvmRuntime> implements IJvmRuntimeService {
}
