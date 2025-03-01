package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.SensorsDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerSensorsDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerSensors;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerSensorsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务器传感器信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 15:06
 */
@Service
public class ServerSensorsServiceImpl extends ServiceImpl<IMonitorServerSensorsDao, MonitorServerSensors> implements IServerSensorsService {

    /**
     * <p>
     * 把服务器传感器信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/15 20:43
     */
    @Retryable
    @Override
    public void operateServerSensors(ServerPackage serverPackage) {
        SensorsDomain sensorsDomain = serverPackage.getServer().getSensorsDomain();
        if (sensorsDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            // 查询数据库中是否有此IP的传感器
            LambdaQueryWrapper<MonitorServerSensors> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerSensors::getIp, ip);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerSensors monitorServerSensors = new MonitorServerSensors();
            monitorServerSensors.setIp(ip);
            monitorServerSensors.setCpuTemperature(sensorsDomain.getCpuTemperature());
            monitorServerSensors.setCpuVoltage(sensorsDomain.getCpuVoltage());
            List<SensorsDomain.FanSpeedDomain> fanSpeedDomainList = sensorsDomain.getFanSpeedDomainList();
            if (CollectionUtils.isNotEmpty(fanSpeedDomainList)) {
                monitorServerSensors.setFanSpeed(fanSpeedDomainList.stream().map(SensorsDomain.FanSpeedDomain::getFanSpeed).collect(Collectors.joining(";")));
            }
            // 没有
            if (selectCountDb == 0) {
                monitorServerSensors.setInsertTime(currentTime);
                this.save(monitorServerSensors);
            }
            // 有
            else {
                monitorServerSensors.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorServerSensors> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerSensors::getIp, ip);
                this.update(monitorServerSensors, lambdaUpdateWrapper);
            }
        }
    }

}
