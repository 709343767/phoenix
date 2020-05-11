package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Result;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.server.business.server.core.InstancePool;
import com.imby.server.business.server.dao.IMonitorInstanceDao;
import com.imby.server.business.server.domain.Instance;
import com.imby.server.business.server.entity.MonitorInstance;
import com.imby.server.business.server.service.IHeartbeatService;
import com.imby.server.property.MonitoringServerWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 心跳服务实现。
 * </p>
 * 1.把应用实例添加或者更新到应用实例池；<br>
 * 2.把应用实例添加或者更新到数据库。
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties config;

    /**
     * 应用实例数据访问对象
     */
    @Autowired
    private IMonitorInstanceDao monitorInstanceDao;

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    @Override
    public Result dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 把应用实例添加或者更新到应用实例池
        this.operateInstancePool(heartbeatPackage);
        // 把应用实例添加或者更新到数据库
        this.operateDb(heartbeatPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 把应用实例添加或者更新到应用实例池
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2020/5/10 23:26
     */
    private void operateInstancePool(HeartbeatPackage heartbeatPackage) {
        // 把应用实例交给应用实例池管理
        String key = heartbeatPackage.getInstanceId();
        Instance instance = new Instance();
        // 实例本身信息
        instance.setEndpoint(heartbeatPackage.getEndpoint());
        instance.setInstanceId(heartbeatPackage.getInstanceId());
        instance.setInstanceName(heartbeatPackage.getInstanceName());
        instance.setIp(heartbeatPackage.getIp());
        instance.setComputerName(heartbeatPackage.getComputerName());
        // 实例状态信息
        instance.setOnline(true);
        instance.setOnConnect(true);
        instance.setDateTime(heartbeatPackage.getDateTime());
        instance.setLineAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isLineAlarm());
        instance.setConnectAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isConnectAlarm());
        instance.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
        // 更新应用实例池
        this.instancePool.updateInstancePool(key, instance);
    }

    /**
     * <p>
     * 把应用实例添加或者更新到数据库
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2020/5/10 23:25
     */
    private void operateDb(HeartbeatPackage heartbeatPackage) {
        String instanceId = heartbeatPackage.getInstanceId();
        MonitorInstance entity = new MonitorInstance();
        entity.setInstanceId(instanceId);
        entity.setInstanceName(heartbeatPackage.getInstanceName());
        entity.setEndpoint(heartbeatPackage.getEndpoint());
        entity.setIp(heartbeatPackage.getIp());
        entity.setIsOnLine(ZeroOrOneConstants.ONE);
        entity.setIsOnConnect(ZeroOrOneConstants.ONE);
        // 查询数据库中有没有当前应用实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, instanceId);
        MonitorInstance monitorInstance = this.monitorInstanceDao.selectOne(lambdaQueryWrapper);
        // 插入记录
        if (monitorInstance == null) {
            entity.setInsertTime(heartbeatPackage.getDateTime());
            this.monitorInstanceDao.insert(entity);
        }
        // 更新记录
        else {
            entity.setUpdateTime(heartbeatPackage.getDateTime());
            LambdaUpdateWrapper<MonitorInstance> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorInstance::getInstanceId, instanceId);
            this.monitorInstanceDao.update(entity, lambdaUpdateWrapper);
        }
    }

}
