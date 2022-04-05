package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerNetcardDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerNetcard;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerNetcardService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器网卡信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:47
 */
@Service
public class ServerNetcardServiceImpl extends ServiceImpl<IMonitorServerNetcardDao, MonitorServerNetcard> implements IServerNetcardService {
}
