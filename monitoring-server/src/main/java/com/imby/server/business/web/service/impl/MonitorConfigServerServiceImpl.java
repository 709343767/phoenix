package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorConfigServer;
import com.imby.server.business.web.dao.IMonitorConfigServerDao;
import com.imby.server.business.web.service.IMonitorConfigServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 监控服务器信息配置服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-17
 */
@Service
public class MonitorConfigServerServiceImpl extends ServiceImpl<IMonitorConfigServerDao, MonitorConfigServer> implements IMonitorConfigServerService {

}
