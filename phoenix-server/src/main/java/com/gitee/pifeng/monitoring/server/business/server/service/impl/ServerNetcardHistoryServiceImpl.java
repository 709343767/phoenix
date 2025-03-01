package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerNetcardHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerNetcardHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerNetcardHistoryService;
import com.google.common.collect.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器网卡历史记录信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:22
 */
@Service
public class ServerNetcardHistoryServiceImpl extends ServiceImpl<IMonitorServerNetcardHistoryDao, MonitorServerNetcardHistory> implements IServerNetcardHistoryService {

    /**
     * <p>
     * 把服务器网卡历史记录添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 23:39
     */
    @Retryable
    @Override
    public void operateServerNetcardHistory(ServerPackage serverPackage) {
        // 网卡信息
        NetDomain netDomain = serverPackage.getServer().getNetDomain();
        if (netDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            // 设置网卡信息
            List<NetDomain.NetInterfaceDomain> netInterfaceDomains = netDomain.getNetList();
            // 要添加的网卡信息
            List<MonitorServerNetcardHistory> saveMonitorServerNetcardHistory = Lists.newArrayList();
            for (int i = 0; i < netInterfaceDomains.size(); i++) {
                NetDomain.NetInterfaceDomain netInterfaceDomain = netInterfaceDomains.get(i);
                // 封装对象
                MonitorServerNetcardHistory monitorServerNetcardHistory = new MonitorServerNetcardHistory();
                monitorServerNetcardHistory.setIp(ip);
                monitorServerNetcardHistory.setNetNo(i + 1);
                // 网卡配置信息
                monitorServerNetcardHistory.setAddress(netInterfaceDomain.getAddress());
                monitorServerNetcardHistory.setBroadcast(netInterfaceDomain.getBroadcast());
                monitorServerNetcardHistory.setMask(netInterfaceDomain.getMask());
                monitorServerNetcardHistory.setName(netInterfaceDomain.getName());
                monitorServerNetcardHistory.setType(netInterfaceDomain.getType());
                monitorServerNetcardHistory.setHwAddr(netInterfaceDomain.getHwAddr());
                monitorServerNetcardHistory.setDescription(netInterfaceDomain.getDescription());
                // 网卡状态信息
                monitorServerNetcardHistory.setRxBytes(netInterfaceDomain.getRxBytes());
                monitorServerNetcardHistory.setRxDropped(netInterfaceDomain.getRxDropped());
                monitorServerNetcardHistory.setRxErrors(netInterfaceDomain.getRxErrors());
                monitorServerNetcardHistory.setRxPackets(netInterfaceDomain.getRxPackets());
                monitorServerNetcardHistory.setTxBytes(netInterfaceDomain.getTxBytes());
                monitorServerNetcardHistory.setTxDropped(netInterfaceDomain.getTxDropped());
                monitorServerNetcardHistory.setTxErrors(netInterfaceDomain.getTxErrors());
                monitorServerNetcardHistory.setTxPackets(netInterfaceDomain.getTxPackets());
                // 网速
                monitorServerNetcardHistory.setDownloadBps(netInterfaceDomain.getDownloadBps());
                monitorServerNetcardHistory.setUploadBps(netInterfaceDomain.getUploadBps());
                // 时间
                monitorServerNetcardHistory.setInsertTime(currentTime);
                monitorServerNetcardHistory.setUpdateTime(currentTime);
                saveMonitorServerNetcardHistory.add(monitorServerNetcardHistory);
            }
            ((IServerNetcardHistoryService) AopContext.currentProxy()).saveBatch(saveMonitorServerNetcardHistory);
        }
    }

}
