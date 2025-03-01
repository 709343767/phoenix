package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.DiskDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerDiskDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerDisk;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerDiskService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器磁盘信息服务层接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 14:56
 */
@Service
public class ServerDiskServiceImpl extends ServiceImpl<IMonitorServerDiskDao, MonitorServerDisk> implements IServerDiskService {

    /**
     * <p>
     * 把服务器磁盘信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:56
     */
    @Retryable
    @Override
    public void operateServerDisk(ServerPackage serverPackage) {
        // 磁盘信息
        DiskDomain diskDomain = serverPackage.getServer().getDiskDomain();
        if (diskDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<DiskDomain.DiskInfoDomain> diskInfoDomains = diskDomain.getDiskInfoList();
            // 要添加的磁盘信息
            List<MonitorServerDisk> saveMonitorServerDisk = Lists.newArrayList();
            for (int i = 0; i < diskInfoDomains.size(); i++) {
                DiskDomain.DiskInfoDomain diskInfoDomain = diskInfoDomains.get(i);
                // 查询数据库中有没有此IP和磁盘的磁盘信息
                LambdaQueryWrapper<MonitorServerDisk> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorServerDisk::getIp, ip);
                lambdaQueryWrapper.eq(MonitorServerDisk::getDiskNo, i + 1);
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorServerDisk monitorServerDisk = new MonitorServerDisk();
                monitorServerDisk.setIp(ip);
                monitorServerDisk.setDiskNo(i + 1);
                monitorServerDisk.setDevName(diskInfoDomain.getDevName());
                monitorServerDisk.setDirName(diskInfoDomain.getDirName());
                monitorServerDisk.setTypeName(diskInfoDomain.getTypeName());
                monitorServerDisk.setSysTypeName(diskInfoDomain.getSysTypeName());
                monitorServerDisk.setAvail(diskInfoDomain.getAvail());
                monitorServerDisk.setFree(diskInfoDomain.getFree());
                monitorServerDisk.setTotal(diskInfoDomain.getTotal());
                monitorServerDisk.setUsed(diskInfoDomain.getUsed());
                monitorServerDisk.setUsePercent(diskInfoDomain.getUsePercent());
                // 没有
                if (selectCountDb == 0) {
                    monitorServerDisk.setInsertTime(currentTime);
                    saveMonitorServerDisk.add(monitorServerDisk);
                }
                // 有
                else {
                    monitorServerDisk.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorServerDisk> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorServerDisk::getIp, ip);
                    lambdaUpdateWrapper.eq(MonitorServerDisk::getDiskNo, i + 1);
                    this.update(monitorServerDisk, lambdaUpdateWrapper);
                }
            }
            // 有要添加的磁盘信息
            if (CollectionUtils.isNotEmpty(saveMonitorServerDisk)) {
                ((IServerDiskService) AopContext.currentProxy()).saveBatch(saveMonitorServerDisk);
            }
        }
    }

}
