package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorDb;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据库表服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 15:25
 */
@Service
public class DbServiceImpl extends ServiceImpl<IMonitorDbDao, MonitorDb> implements IDbService {
}
