package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.MemoryDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerMemoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerMemory;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerMemoryService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务器内存信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/2/4 14:14
 */
@Service
public class ServerMemoryServiceImpl extends ServiceImpl<IMonitorServerMemoryDao, MonitorServerMemory> implements IServerMemoryService {

    /**
     * <p>
     * 把服务器内存信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/25 9:38
     */
    @Retryable
    @Override
    public void operateServerMemory(ServerPackage serverPackage) {
        // 内存信息
        MemoryDomain memoryDomain = serverPackage.getServer().getMemoryDomain();
        if (memoryDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            MemoryDomain.MenDomain menDomain = memoryDomain.getMenDomain();
            MemoryDomain.SwapDomain swapDomain = memoryDomain.getSwapDomain();
            // 判断数据库中是否有此IP的服务器内存
            LambdaQueryWrapper<MonitorServerMemory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerMemory::getIp, ip);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerMemory monitorServerMemory = new MonitorServerMemory();
            monitorServerMemory.setIp(ip);
            monitorServerMemory.setMenTotal(menDomain.getMemTotal());
            monitorServerMemory.setMenUsed(menDomain.getMemUsed());
            monitorServerMemory.setMenFree(menDomain.getMemFree());
            monitorServerMemory.setMenUsedPercent(menDomain.getMenUsedPercent());
            monitorServerMemory.setSwapTotal(swapDomain.getSwapTotal());
            monitorServerMemory.setSwapUsed(swapDomain.getSwapUsed());
            monitorServerMemory.setSwapFree(swapDomain.getSwapFree());
            monitorServerMemory.setSwapUsedPercent(swapDomain.getSwapUsedPercent());
            // 没有
            if (selectCountDb == 0) {
                monitorServerMemory.setInsertTime(currentTime);
                this.save(monitorServerMemory);
            }
            // 有
            else {
                monitorServerMemory.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorServerMemory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerMemory::getIp, ip);
                this.update(monitorServerMemory, lambdaUpdateWrapper);
            }
        }
    }

}
