package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.CpuDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerCpuHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpuHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerCpuHistoryService;
import com.google.common.collect.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器CPU历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:16
 */
@Service
public class ServerCpuHistoryServiceImpl extends ServiceImpl<IMonitorServerCpuHistoryDao, MonitorServerCpuHistory> implements IServerCpuHistoryService {

    /**
     * <p>
     * 把服务器CPU历史记录添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 17:19
     */
    @Retryable
    @Override
    public void operateServerCpuHistory(ServerPackage serverPackage) {
        // Cpu信息
        CpuDomain cpuDomain = serverPackage.getServer().getCpuDomain();
        if (cpuDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<CpuDomain.CpuInfoDomain> cpuInfoDomains = cpuDomain.getCpuList();
            // 要添加的Cpu信息
            List<MonitorServerCpuHistory> saveMonitorServerCpuHistory = Lists.newArrayList();
            for (int i = 0; i < cpuInfoDomains.size(); i++) {
                CpuDomain.CpuInfoDomain cpuInfoDomain = cpuInfoDomains.get(i);
                // 封装对象
                MonitorServerCpuHistory monitorServerCpuHistory = new MonitorServerCpuHistory();
                monitorServerCpuHistory.setIp(ip);
                monitorServerCpuHistory.setCpuNo(i + 1);
                monitorServerCpuHistory.setCpuMhz(cpuInfoDomain.getCpuMhz());
                monitorServerCpuHistory.setCpuVendor(cpuInfoDomain.getCpuVendor());
                monitorServerCpuHistory.setCpuModel(cpuInfoDomain.getCpuModel());
                monitorServerCpuHistory.setCpuUser(cpuInfoDomain.getCpuUser());
                monitorServerCpuHistory.setCpuSys(cpuInfoDomain.getCpuSys());
                monitorServerCpuHistory.setCpuNice(cpuInfoDomain.getCpuNice());
                monitorServerCpuHistory.setCpuWait(cpuInfoDomain.getCpuWait());
                monitorServerCpuHistory.setCpuCombined(cpuInfoDomain.getCpuCombined());
                monitorServerCpuHistory.setCpuIdle(cpuInfoDomain.getCpuIdle());
                monitorServerCpuHistory.setInsertTime(currentTime);
                monitorServerCpuHistory.setUpdateTime(currentTime);
                saveMonitorServerCpuHistory.add(monitorServerCpuHistory);
            }
            ((IServerCpuHistoryService) AopContext.currentProxy()).saveBatch(saveMonitorServerCpuHistory);
        }
    }

}
