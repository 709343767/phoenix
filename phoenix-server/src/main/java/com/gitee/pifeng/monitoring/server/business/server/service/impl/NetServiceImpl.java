package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorNetDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNet;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网络信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 15:28
 */
@Service
public class NetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements INetService {

    /**
     * <p>
     * 获取被监控网络源IP地址
     * </p>
     *
     * @return 被监控网络源IP地址
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/10/6 22:05
     */
    @Override
    public String getSourceIp() throws NetException {
        return NetUtils.getLocalIp();
    }

}