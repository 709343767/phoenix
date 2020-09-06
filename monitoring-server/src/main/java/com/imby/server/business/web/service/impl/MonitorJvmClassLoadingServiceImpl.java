package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorJvmClassLoading;
import com.imby.server.business.web.dao.IMonitorJvmClassLoadingDao;
import com.imby.server.business.web.service.IMonitorJvmClassLoadingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机类加载信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmClassLoadingServiceImpl extends ServiceImpl<IMonitorJvmClassLoadingDao, MonitorJvmClassLoading> implements IMonitorJvmClassLoadingService {

}
