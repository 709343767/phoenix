package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorConfigDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorConfig;
import com.gitee.pifeng.monitoring.server.business.server.service.IConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 监控配置服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/4 11:06
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<IMonitorConfigDao, MonitorConfig> implements IConfigService {
}
