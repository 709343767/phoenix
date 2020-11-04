package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorConfig;
import com.imby.server.business.web.dao.IMonitorConfigDao;
import com.imby.server.business.web.service.IMonitorConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 监控配置服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-04
 */
@Service
public class MonitorConfigServiceImpl extends ServiceImpl<IMonitorConfigDao, MonitorConfig> implements IMonitorConfigService {

}
