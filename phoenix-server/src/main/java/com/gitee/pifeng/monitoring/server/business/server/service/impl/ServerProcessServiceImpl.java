package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerProcessDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerProcess;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerProcessService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器进程信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:11
 */
@Service
public class ServerProcessServiceImpl extends ServiceImpl<IMonitorServerProcessDao, MonitorServerProcess> implements IServerProcessService {
}
