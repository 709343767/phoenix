package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorTcpIpDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpIp;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpIpService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TCP/IP信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:25
 */
@Service
public class TcpIpServiceImpl extends ServiceImpl<IMonitorTcpIpDao, MonitorTcpIp> implements ITcpIpService {
}
