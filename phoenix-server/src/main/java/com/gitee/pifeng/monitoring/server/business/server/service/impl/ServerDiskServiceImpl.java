package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerDiskDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerDisk;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerDiskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器磁盘信息服务层接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 14:56
 */
@Service
public class ServerDiskServiceImpl extends ServiceImpl<IMonitorServerDiskDao, MonitorServerDisk> implements IServerDiskService {
}
