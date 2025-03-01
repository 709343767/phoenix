package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.CpuDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerCpuDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerCpu;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerCpuService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器CPU信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 9:16
 */
@Service
public class ServerCpuServiceImpl extends ServiceImpl<IMonitorServerCpuDao, MonitorServerCpu> implements IServerCpuService {

    /**
     * <p>
     * 把服务器CPU信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:48
     */
    @Retryable
    @Override
    public void operateServerCpu(ServerPackage serverPackage) {
        // Cpu信息
        CpuDomain cpuDomain = serverPackage.getServer().getCpuDomain();
        if (cpuDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<CpuDomain.CpuInfoDomain> cpuInfoDomains = cpuDomain.getCpuList();
            // 要添加的Cpu信息集合
            List<MonitorServerCpu> saveMonitorServerCpus = Lists.newArrayList();
            for (int i = 0; i < cpuInfoDomains.size(); i++) {
                CpuDomain.CpuInfoDomain cpuInfoDomain = cpuInfoDomains.get(i);
                // 查询数据库中有没有此IP和此CPU的CPU信息
                LambdaQueryWrapper<MonitorServerCpu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorServerCpu::getIp, ip);
                lambdaQueryWrapper.eq(MonitorServerCpu::getCpuNo, i + 1);
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorServerCpu monitorServerCpu = new MonitorServerCpu();
                monitorServerCpu.setIp(ip);
                monitorServerCpu.setCpuNo(i + 1);
                monitorServerCpu.setCpuMhz(cpuInfoDomain.getCpuMhz());
                monitorServerCpu.setCpuVendor(cpuInfoDomain.getCpuVendor());
                monitorServerCpu.setCpuModel(cpuInfoDomain.getCpuModel());
                monitorServerCpu.setCpuUser(cpuInfoDomain.getCpuUser());
                monitorServerCpu.setCpuSys(cpuInfoDomain.getCpuSys());
                monitorServerCpu.setCpuNice(cpuInfoDomain.getCpuNice());
                monitorServerCpu.setCpuWait(cpuInfoDomain.getCpuWait());
                monitorServerCpu.setCpuCombined(cpuInfoDomain.getCpuCombined());
                monitorServerCpu.setCpuIdle(cpuInfoDomain.getCpuIdle());
                // 没有
                if (selectCountDb == 0) {
                    monitorServerCpu.setInsertTime(currentTime);
                    saveMonitorServerCpus.add(monitorServerCpu);
                }
                // 有
                else {
                    monitorServerCpu.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorServerCpu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorServerCpu::getIp, ip);
                    lambdaUpdateWrapper.eq(MonitorServerCpu::getCpuNo, i + 1);
                    this.update(monitorServerCpu, lambdaUpdateWrapper);
                }
            }
            // 有要新增的Cpu
            if (CollectionUtils.isNotEmpty(saveMonitorServerCpus)) {
                ((IServerCpuService) AopContext.currentProxy()).saveBatch(saveMonitorServerCpus);
            }
        }
    }

}
