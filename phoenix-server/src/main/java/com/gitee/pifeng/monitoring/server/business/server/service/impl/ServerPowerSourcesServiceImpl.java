package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.server.PowerSourcesDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerPowerSourcesDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerPowerSources;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerPowerSourcesService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器电池信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:00
 */
@Service
public class ServerPowerSourcesServiceImpl extends ServiceImpl<IMonitorServerPowerSourcesDao, MonitorServerPowerSources> implements IServerPowerSourcesService {

    /**
     * <p>
     * 把服务器电池信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/15 20:42
     */
    @Retryable
    @Override
    public void operateServerPowerSources(ServerPackage serverPackage) {
        // 电池信息
        PowerSourcesDomain powerSourcesDomain = serverPackage.getServer().getPowerSourcesDomain();
        if (powerSourcesDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<PowerSourcesDomain.PowerSourceDomain> powerSourceDomains = powerSourcesDomain.getPowerSourceDomainList();
            // 要添加的电池信息
            List<MonitorServerPowerSources> saveMonitorServerPowerSources = Lists.newArrayList();
            for (int i = 0; i < powerSourceDomains.size(); i++) {
                PowerSourcesDomain.PowerSourceDomain powerSourceDomain = powerSourceDomains.get(i);
                // 查询数据库中是否有此IP的电池信息
                LambdaQueryWrapper<MonitorServerPowerSources> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorServerPowerSources::getIp, ip);
                lambdaQueryWrapper.eq(MonitorServerPowerSources::getPowerSourcesNo, i + 1);
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorServerPowerSources monitorServerPowerSources = new MonitorServerPowerSources();
                monitorServerPowerSources.setIp(ip);
                monitorServerPowerSources.setPowerSourcesNo(i + 1);
                monitorServerPowerSources.setName(powerSourceDomain.getName());
                monitorServerPowerSources.setDeviceName(powerSourceDomain.getDeviceName());
                monitorServerPowerSources.setRemainingCapacityPercent(powerSourceDomain.getRemainingCapacityPercent());
                monitorServerPowerSources.setTimeRemainingEstimated(powerSourceDomain.getTimeRemainingEstimated());
                monitorServerPowerSources.setTimeRemainingInstant(powerSourceDomain.getTimeRemainingInstant());
                monitorServerPowerSources.setPowerUsageRate(powerSourceDomain.getPowerUsageRate());
                monitorServerPowerSources.setVoltage(powerSourceDomain.getVoltage());
                monitorServerPowerSources.setAmperage(powerSourceDomain.getAmperage());
                monitorServerPowerSources.setIsPowerOnLine(powerSourceDomain.isPowerOnLine() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
                monitorServerPowerSources.setIsCharging(powerSourceDomain.isCharging() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
                monitorServerPowerSources.setIsDischarging(powerSourceDomain.isDischarging() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
                monitorServerPowerSources.setCurrentCapacity(powerSourceDomain.getCurrentCapacity());
                monitorServerPowerSources.setMaxCapacity(powerSourceDomain.getMaxCapacity());
                monitorServerPowerSources.setDesignCapacity(powerSourceDomain.getDesignCapacity());
                monitorServerPowerSources.setCycleCount(powerSourceDomain.getCycleCount());
                monitorServerPowerSources.setChemistry(powerSourceDomain.getChemistry());
                monitorServerPowerSources.setManufactureDate(powerSourceDomain.getManufactureDate());
                monitorServerPowerSources.setManufacturer(powerSourceDomain.getManufacturer());
                monitorServerPowerSources.setSerialNumber(powerSourceDomain.getSerialNumber());
                monitorServerPowerSources.setTemperature(powerSourceDomain.getTemperature());
                // 没有
                if (selectCountDb == 0) {
                    monitorServerPowerSources.setInsertTime(currentTime);
                    saveMonitorServerPowerSources.add(monitorServerPowerSources);
                }
                // 有
                else {
                    monitorServerPowerSources.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorServerPowerSources> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorServerPowerSources::getIp, ip);
                    lambdaUpdateWrapper.eq(MonitorServerPowerSources::getPowerSourcesNo, i + 1);
                    this.update(monitorServerPowerSources, lambdaUpdateWrapper);
                }
            }
            // 有要添加的电池信息
            if (CollectionUtils.isNotEmpty(saveMonitorServerPowerSources)) {
                ((IServerPowerSourcesService) AopContext.currentProxy()).saveBatch(saveMonitorServerPowerSources);
            }
        }
    }

}
