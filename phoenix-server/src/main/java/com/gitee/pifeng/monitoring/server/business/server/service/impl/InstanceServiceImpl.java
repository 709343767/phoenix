package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorInstanceDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.server.business.server.service.IInstanceService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 应用实例服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/29 15:21
 */
@Service
public class InstanceServiceImpl extends ServiceImpl<IMonitorInstanceDao, MonitorInstance> implements IInstanceService {

    /**
     * <p>
     * 把应用实例添加或者更新到数据库
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2025/5/3 14:31
     */
    @Retryable
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void operateMonitorInstance(HeartbeatPackage heartbeatPackage) {
        String instanceId = heartbeatPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // 查询数据库中有没有当前应用实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, instanceId);
        int selectCountDb = this.count(lambdaQueryWrapper);
        // 封装对象
        MonitorInstance entity = new MonitorInstance();
        entity.setInstanceId(instanceId);
        entity.setInstanceName(heartbeatPackage.getInstanceName());
        entity.setInstanceDesc(heartbeatPackage.getInstanceDesc());
        entity.setEndpoint(heartbeatPackage.getInstanceEndpoint());
        entity.setIp(heartbeatPackage.getIp());
        entity.setConnFrequency((int) heartbeatPackage.getRate());
        entity.setLanguage(heartbeatPackage.getInstanceLanguage());
        entity.setAppServerType(heartbeatPackage.getAppServerType().getName());
        entity.setIsOfflineNotice(ZeroOrOneConstants.ZERO);
        // 插入记录
        if (selectCountDb == 0) {
            entity.setInsertTime(currentTime);
            entity.setOfflineCount(0);
            // 默认开启监控和告警
            entity.setIsEnableMonitor(ZeroOrOneConstants.ONE);
            entity.setIsEnableAlarm(ZeroOrOneConstants.ONE);
            this.save(entity);
        }
        // 更新记录
        else {
            entity.setUpdateTime(currentTime);
            LambdaUpdateWrapper<MonitorInstance> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorInstance::getInstanceId, instanceId);
            this.update(entity, lambdaUpdateWrapper);
        }
    }

}
