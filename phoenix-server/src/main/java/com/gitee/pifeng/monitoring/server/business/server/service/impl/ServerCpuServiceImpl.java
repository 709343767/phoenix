package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerCpuDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpu;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerCpuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器CPU信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 9:16
 */
@Service
public class ServerCpuServiceImpl extends ServiceImpl<IMonitorServerCpuDao, MonitorServerCpu> implements IServerCpuService {
}
