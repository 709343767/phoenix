package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.DiskDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerDiskHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerDiskHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerDiskHistoryService;
import com.google.common.collect.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器磁盘历史记录服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:31
 */
@Service
public class ServerDiskHistoryServiceImpl extends ServiceImpl<IMonitorServerDiskHistoryDao, MonitorServerDiskHistory> implements IServerDiskHistoryService {

    /**
     * <p>
     * 把服务器磁盘历史记录添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/12 10:29
     */
    @Retryable
    @Override
    public void operateServerDiskHistory(ServerPackage serverPackage) {
        // 磁盘信息
        DiskDomain diskDomain = serverPackage.getServer().getDiskDomain();
        if (diskDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
            // 要添加的磁盘信息
            List<MonitorServerDiskHistory> saveMonitorServerDiskHistory = Lists.newArrayList();
            for (int i = 0; i < diskInfoDomains.size(); i++) {
                DiskDomain.DiskInfoDomain diskInfoDomain = diskInfoDomains.get(i);
                // 封装对象
                MonitorServerDiskHistory monitorServerDiskHistory = new MonitorServerDiskHistory();
                monitorServerDiskHistory.setIp(ip);
                monitorServerDiskHistory.setDiskNo(i + 1);
                monitorServerDiskHistory.setDevName(diskInfoDomain.getDevName());
                monitorServerDiskHistory.setDirName(diskInfoDomain.getDirName());
                monitorServerDiskHistory.setTypeName(diskInfoDomain.getTypeName());
                monitorServerDiskHistory.setSysTypeName(diskInfoDomain.getSysTypeName());
                monitorServerDiskHistory.setAvail(diskInfoDomain.getAvail());
                monitorServerDiskHistory.setFree(diskInfoDomain.getFree());
                monitorServerDiskHistory.setTotal(diskInfoDomain.getTotal());
                monitorServerDiskHistory.setUsed(diskInfoDomain.getUsed());
                monitorServerDiskHistory.setUsePercent(diskInfoDomain.getUsePercent());
                monitorServerDiskHistory.setInsertTime(currentTime);
                monitorServerDiskHistory.setUpdateTime(currentTime);
                saveMonitorServerDiskHistory.add(monitorServerDiskHistory);
            }
            ((IServerDiskHistoryService) AopContext.currentProxy()).saveBatch(saveMonitorServerDiskHistory);
        }
    }

}
