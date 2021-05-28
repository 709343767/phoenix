package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorNetDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNet;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网络信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 15:28
 */
@Service
public class NetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements INetService {
}