package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Result;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.exception.NetException;
import com.imby.common.util.NetUtils;
import com.imby.server.business.server.dao.IMonitorInstanceDao;
import com.imby.server.business.server.dao.IMonitorNetDao;
import com.imby.server.business.server.entity.MonitorInstance;
import com.imby.server.business.server.entity.MonitorNet;
import com.imby.server.business.server.service.IHeartbeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 心跳服务实现。
 * </p>
 * 把应用实例添加或者更新到数据库。
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
@Transactional
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * 应用实例数据访问对象
     */
    @Autowired
    private IMonitorInstanceDao monitorInstanceDao;

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return {@link Result}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    @Transactional
    @Override
    public Result dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) throws NetException {
        // 把应用实例添加或者更新到数据库
        this.operateMonitorInstance(heartbeatPackage);
        // 把网络信息添加或者更新到数据库
        this.operateMonitorNet(heartbeatPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 把网络信息添加或者更新到数据库
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/8/31 16:46
     */
    private void operateMonitorNet(HeartbeatPackage heartbeatPackage) throws NetException {
        // IP地址（来源）
        String ipSource = heartbeatPackage.getIp();
        // IP地址（目的地）
        String ipTarget = NetUtils.getLocalIp();
        // 先查询数据库中是否有此IP
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorNet::getIpSource, ipSource);
        lambdaQueryWrapper.eq(MonitorNet::getIpTarget, ipTarget);
        MonitorNet monitorNetDb = this.monitorNetDao.selectOne(lambdaQueryWrapper);
        // 创建对象
        MonitorNet monitorNet = new MonitorNet();
        monitorNet.setIpSource(ipSource);
        monitorNet.setIpTarget(ipTarget);
        monitorNet.setStatus(ZeroOrOneConstants.ONE);
        // 插入记录
        if (monitorNetDb == null) {
            monitorNet.setInsertTime(new Date());
            this.monitorNetDao.insert(monitorNet);
        }
        // 更新记录
        else {
            monitorNet.setUpdateTime(new Date());
            LambdaUpdateWrapper<MonitorNet> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorNet::getIpSource, ipSource);
            lambdaUpdateWrapper.eq(MonitorNet::getIpTarget, ipTarget);
            this.monitorNetDao.update(monitorNet, lambdaUpdateWrapper);
        }
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
        MonitorInstance entity = new MonitorInstance();
        entity.setInstanceId(instanceId);
        entity.setInstanceName(heartbeatPackage.getInstanceName());
        entity.setInstanceDesc(heartbeatPackage.getInstanceDesc());
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
