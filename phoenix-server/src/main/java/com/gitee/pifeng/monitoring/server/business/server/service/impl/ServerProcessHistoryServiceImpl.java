package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.ProcessDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerProcessHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerProcessHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerProcessHistoryService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务器进程历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:36
 */
@Service
public class ServerProcessHistoryServiceImpl extends ServiceImpl<IMonitorServerProcessHistoryDao, MonitorServerProcessHistory> implements IServerProcessHistoryService {

    /**
     * <p>
     * 把服务器进程历史记录添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/9/15 15:04
     */
    @Retryable
    @Override
    public void operateServerProcessHistory(ServerPackage serverPackage) {
        ProcessDomain processDomain = serverPackage.getServer().getProcessDomain();
        if (processDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            int processNum = processDomain.getProcessNum();
            // 添加记录到数据库
            MonitorServerProcessHistory monitorServerProcessHistory = MonitorServerProcessHistory.builder().build();
            monitorServerProcessHistory.setIp(ip);
            monitorServerProcessHistory.setProcessNum(processNum);
            monitorServerProcessHistory.setInsertTime(currentTime);
            monitorServerProcessHistory.setUpdateTime(currentTime);
            this.save(monitorServerProcessHistory);
        }
    }

}
