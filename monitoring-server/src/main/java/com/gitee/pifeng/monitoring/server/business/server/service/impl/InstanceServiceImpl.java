package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorInstanceDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.server.business.server.service.IInstanceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应用实例服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/29 15:21
 */
@Service
public class InstanceServiceImpl extends ServiceImpl<IMonitorInstanceDao, MonitorInstance> implements IInstanceService {
}
