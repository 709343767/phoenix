package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerOsDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerOs;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerOsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器操作系统信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:23
 */
@Service
public class ServerOsServiceImpl extends ServiceImpl<IMonitorServerOsDao, MonitorServerOs> implements IServerOsService {
}
