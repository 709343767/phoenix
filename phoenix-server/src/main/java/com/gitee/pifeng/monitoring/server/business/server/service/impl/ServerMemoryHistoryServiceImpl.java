package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.MemoryDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerMemoryHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerMemoryHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerMemoryHistoryService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务器内存历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:10
 */
@Service
public class ServerMemoryHistoryServiceImpl extends ServiceImpl<IMonitorServerMemoryHistoryDao, MonitorServerMemoryHistory> implements IServerMemoryHistoryService {

    /**
     * <p>
     * 把服务器内存历史记录添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:43
     */
    @Retryable
    @Override
    public void operateServerMemoryHistory(ServerPackage serverPackage) {
        // 内存信息
        MemoryDomain memoryDomain = serverPackage.getServer().getMemoryDomain();
        if (memoryDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            MemoryDomain.MenDomain menDomain = memoryDomain.getMenDomain();
            MemoryDomain.SwapDomain swapDomain = memoryDomain.getSwapDomain();
            // 封装对象
            MonitorServerMemoryHistory monitorServerMemoryHistory = new MonitorServerMemoryHistory();
            monitorServerMemoryHistory.setIp(ip);
            monitorServerMemoryHistory.setMenTotal(menDomain.getMemTotal());
            monitorServerMemoryHistory.setMenUsed(menDomain.getMemUsed());
            monitorServerMemoryHistory.setMenFree(menDomain.getMemFree());
            monitorServerMemoryHistory.setMenUsedPercent(menDomain.getMenUsedPercent());
            monitorServerMemoryHistory.setSwapTotal(swapDomain.getSwapTotal());
            monitorServerMemoryHistory.setSwapUsed(swapDomain.getSwapUsed());
            monitorServerMemoryHistory.setSwapFree(swapDomain.getSwapFree());
            monitorServerMemoryHistory.setSwapUsedPercent(swapDomain.getSwapUsedPercent());
            monitorServerMemoryHistory.setInsertTime(currentTime);
            monitorServerMemoryHistory.setUpdateTime(currentTime);
            this.save(monitorServerMemoryHistory);
        }
    }

}
