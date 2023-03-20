package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmClassLoadingDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmClassLoading;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmClassLoadingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机类加载信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/12 19:34
 */
@Service
public class JvmClassLoadingServiceImpl extends ServiceImpl<IMonitorJvmClassLoadingDao, MonitorJvmClassLoading> implements IJvmClassLoadingService {
}
