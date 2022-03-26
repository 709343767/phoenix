package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorTcpDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TCP信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:25
 */
@Service
public class TcpServiceImpl extends ServiceImpl<IMonitorTcpDao, MonitorTcp> implements ITcpService {
}
