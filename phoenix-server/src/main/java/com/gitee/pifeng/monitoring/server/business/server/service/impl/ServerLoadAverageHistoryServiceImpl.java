package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.SystemLoadAverageDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerLoadAverageHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverageHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerLoadAverageHistoryService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服服务器平均负载历史记录服务层接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 15:22
 */
@Service
public class ServerLoadAverageHistoryServiceImpl extends ServiceImpl<IMonitorServerLoadAverageHistoryDao, MonitorServerLoadAverageHistory> implements IServerLoadAverageHistoryService {

    /**
     * <p>
     * 把服务器平均负载历史记录添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2022/6/17 14:50
     */
    @Retryable
    @Override
    public void operateServerLoadAverageHistory(ServerPackage serverPackage) {
        SystemLoadAverageDomain systemLoadAverageDomain = serverPackage.getServer().getSystemLoadAverageDomain();
        if (systemLoadAverageDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            // 封装对象
            MonitorServerLoadAverageHistory monitorServerLoadAverageHistory = new MonitorServerLoadAverageHistory();
            monitorServerLoadAverageHistory.setIp(ip);
            monitorServerLoadAverageHistory.setLogicalProcessorCount(systemLoadAverageDomain.getLogicalProcessorCount());
            monitorServerLoadAverageHistory.setOne(systemLoadAverageDomain.getOneLoadAverage());
            monitorServerLoadAverageHistory.setFive(systemLoadAverageDomain.getFiveLoadAverage());
            monitorServerLoadAverageHistory.setFifteen(systemLoadAverageDomain.getFifteenLoadAverage());
            monitorServerLoadAverageHistory.setInsertTime(currentTime);
            monitorServerLoadAverageHistory.setUpdateTime(currentTime);
            this.save(monitorServerLoadAverageHistory);
        }
    }

}
