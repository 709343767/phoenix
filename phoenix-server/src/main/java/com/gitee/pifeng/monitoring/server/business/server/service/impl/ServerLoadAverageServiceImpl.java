package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.SystemLoadAverageDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerLoadAverageDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerLoadAverage;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerLoadAverageService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服服务器平均负载服务层接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/6/17 14:54
 */
@Service
public class ServerLoadAverageServiceImpl extends ServiceImpl<IMonitorServerLoadAverageDao, MonitorServerLoadAverage> implements IServerLoadAverageService {

    /**
     * <p>
     * 把服务器平均负载信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2022/6/17 14:50
     */
    @Retryable
    @Override
    public void operateServerLoadAverage(ServerPackage serverPackage) {
        SystemLoadAverageDomain systemLoadAverageDomain = serverPackage.getServer().getSystemLoadAverageDomain();
        if (systemLoadAverageDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            // 查询数据库中是否有此IP的服务器平均负载信息
            LambdaQueryWrapper<MonitorServerLoadAverage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerLoadAverage::getIp, ip);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerLoadAverage monitorServerLoadAverage = new MonitorServerLoadAverage();
            monitorServerLoadAverage.setIp(ip);
            monitorServerLoadAverage.setLogicalProcessorCount(systemLoadAverageDomain.getLogicalProcessorCount());
            monitorServerLoadAverage.setOne(systemLoadAverageDomain.getOneLoadAverage());
            monitorServerLoadAverage.setFive(systemLoadAverageDomain.getFiveLoadAverage());
            monitorServerLoadAverage.setFifteen(systemLoadAverageDomain.getFifteenLoadAverage());
            // 没有
            if (selectCountDb == 0) {
                monitorServerLoadAverage.setInsertTime(currentTime);
                this.save(monitorServerLoadAverage);
            }
            // 有
            else {
                monitorServerLoadAverage.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorServerLoadAverage> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerLoadAverage::getIp, ip);
                this.update(monitorServerLoadAverage, lambdaUpdateWrapper);
            }
        }
    }

}
