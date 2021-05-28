package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorInstanceDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.server.business.server.service.IHeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 心跳服务实现。
 * </p>
 * 把应用实例添加或者更新到数据库。
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
@Transactional(rollbackFor = Throwable.class)
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

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
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Result dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 把应用实例添加或者更新到数据库
        this.operateMonitorInstance(heartbeatPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
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
    private void operateMonitorInstance(HeartbeatPackage heartbeatPackage) {
        String instanceId = heartbeatPackage.getInstanceId();
        // 查询数据库中有没有当前应用实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, instanceId);
        Integer selectCountDb = this.monitorInstanceDao.selectCount(lambdaQueryWrapper);
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
        // 插入记录
        if (selectCountDb == null || selectCountDb == 0) {
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
