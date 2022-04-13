package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorHttpDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * HTTP信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:05
 */
@Service
public class HttpServiceImpl extends ServiceImpl<IMonitorHttpDao, MonitorHttp> implements IHttpService {
}
