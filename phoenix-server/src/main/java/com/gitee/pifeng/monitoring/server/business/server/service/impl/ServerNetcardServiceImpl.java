package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.NetDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerNetcardDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerNetcard;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerNetcardService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    /**
     * <p>
     * 把服务器网卡信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/26 11:21
     */
    @Retryable
    @Override
    public void operateServerNetcard(ServerPackage serverPackage) {
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
            List<MonitorServerNetcard> saveMonitorServerNetcard = Lists.newArrayList();
            for (int i = 0; i < netInterfaceDomains.size(); i++) {
                NetDomain.NetInterfaceDomain netInterfaceDomain = netInterfaceDomains.get(i);
                // 查询数据库中有没有此IP和网卡的网卡信息
                LambdaQueryWrapper<MonitorServerNetcard> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorServerNetcard::getIp, ip);
                lambdaQueryWrapper.eq(MonitorServerNetcard::getNetNo, i + 1);
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorServerNetcard monitorServerNetcard = new MonitorServerNetcard();
                monitorServerNetcard.setIp(ip);
                monitorServerNetcard.setNetNo(i + 1);
                // 网卡配置信息
                monitorServerNetcard.setAddress(netInterfaceDomain.getAddress());
                monitorServerNetcard.setBroadcast(netInterfaceDomain.getBroadcast());
                monitorServerNetcard.setMask(netInterfaceDomain.getMask());
                monitorServerNetcard.setName(netInterfaceDomain.getName());
                monitorServerNetcard.setType(netInterfaceDomain.getType());
                monitorServerNetcard.setHwAddr(netInterfaceDomain.getHwAddr());
                monitorServerNetcard.setDescription(netInterfaceDomain.getDescription());
                // 网卡状态信息
                monitorServerNetcard.setRxBytes(netInterfaceDomain.getRxBytes());
                monitorServerNetcard.setRxDropped(netInterfaceDomain.getRxDropped());
                monitorServerNetcard.setRxErrors(netInterfaceDomain.getRxErrors());
                monitorServerNetcard.setRxPackets(netInterfaceDomain.getRxPackets());
                monitorServerNetcard.setTxBytes(netInterfaceDomain.getTxBytes());
                monitorServerNetcard.setTxDropped(netInterfaceDomain.getTxDropped());
                monitorServerNetcard.setTxErrors(netInterfaceDomain.getTxErrors());
                monitorServerNetcard.setTxPackets(netInterfaceDomain.getTxPackets());
                // 网速
                monitorServerNetcard.setDownloadBps(netInterfaceDomain.getDownloadBps());
                monitorServerNetcard.setUploadBps(netInterfaceDomain.getUploadBps());
                // 没有
                if (selectCountDb == 0) {
                    monitorServerNetcard.setInsertTime(currentTime);
                    saveMonitorServerNetcard.add(monitorServerNetcard);
                }
                // 有
                else {
                    monitorServerNetcard.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorServerNetcard> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorServerNetcard::getIp, ip);
                    lambdaUpdateWrapper.eq(MonitorServerNetcard::getNetNo, i + 1);
                    this.update(monitorServerNetcard, lambdaUpdateWrapper);
                }
            }
            // 有要添加的网卡信息
            if (CollectionUtils.isNotEmpty(saveMonitorServerNetcard)) {
                ((IServerNetcardService) AopContext.currentProxy()).saveBatch(saveMonitorServerNetcard);
            }
        }
    }

}
