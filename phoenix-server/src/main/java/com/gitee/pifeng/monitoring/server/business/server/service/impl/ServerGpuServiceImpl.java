package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.GpuDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerGpuDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerGpu;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerGpuService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器GPU信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-06-09
 */
@Service
public class ServerGpuServiceImpl extends ServiceImpl<IMonitorServerGpuDao, MonitorServerGpu> implements IServerGpuService {

    /**
     * <p>
     * 把服务器GPU信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2025/6/9 10:27
     */
    @Retryable
    @Override
    public void operateServerGpu(ServerPackage serverPackage) {
        // GPU信息
        GpuDomain gpuDomain = serverPackage.getServer().getGpuDomain();
        if (gpuDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<GpuDomain.GpuInfoDomain> gpuInfoDomains = gpuDomain.getGpuList();
            // 要添加的Gpu信息集合
            List<MonitorServerGpu> saveMonitorServerGpus = Lists.newArrayList();
            for (int i = 0; i < gpuInfoDomains.size(); i++) {
                GpuDomain.GpuInfoDomain gpuInfoDomain = gpuInfoDomains.get(i);
                // 查询数据库中有没有此IP和此GPU的GPU信息
                LambdaQueryWrapper<MonitorServerGpu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorServerGpu::getIp, ip);
                lambdaQueryWrapper.eq(MonitorServerGpu::getGpuNo, i + 1);
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorServerGpu monitorServerGpu = new MonitorServerGpu();
                monitorServerGpu.setIp(ip);
                monitorServerGpu.setGpuNo(i + 1);
                monitorServerGpu.setGpuName(gpuInfoDomain.getGpuName());
                monitorServerGpu.setGpuDeviceId(gpuInfoDomain.getGpuDeviceId());
                monitorServerGpu.setGpuVendor(gpuInfoDomain.getGpuVendor());
                monitorServerGpu.setGpuVersionInfo(gpuInfoDomain.getGpuVersionInfo());
                monitorServerGpu.setGpuVramTotal(gpuInfoDomain.getGpuVRamTotal());
                // 没有
                if (selectCountDb == 0) {
                    monitorServerGpu.setInsertTime(currentTime);
                    saveMonitorServerGpus.add(monitorServerGpu);
                }
                // 有
                else {
                    monitorServerGpu.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorServerGpu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorServerGpu::getIp, ip);
                    lambdaUpdateWrapper.eq(MonitorServerGpu::getGpuNo, i + 1);
                    this.update(monitorServerGpu, lambdaUpdateWrapper);
                }
            }
            // 有要新增的Gpu
            if (CollectionUtils.isNotEmpty(saveMonitorServerGpus)) {
                ((IServerGpuService) AopContext.currentProxy()).saveBatch(saveMonitorServerGpus);
            }
        }
    }

}