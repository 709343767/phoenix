package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerSensorsDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerSensors;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerSensorsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器传感器信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:06
 */
@Service
public class ServerSensorsServiceImpl extends ServiceImpl<IMonitorServerSensorsDao, MonitorServerSensors> implements IServerSensorsService {
}
