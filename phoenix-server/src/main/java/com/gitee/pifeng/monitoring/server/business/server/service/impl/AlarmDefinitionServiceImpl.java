package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmDefinitionDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmDefinition;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmDefinitionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 告警定义服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/5/7 09:13
 */
@Service
public class AlarmDefinitionServiceImpl extends ServiceImpl<IMonitorAlarmDefinitionDao, MonitorAlarmDefinition> implements IAlarmDefinitionService {
}