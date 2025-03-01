package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.jvm.GarbageCollectorDomain;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmGarbageCollectorDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmGarbageCollector;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmGarbageCollectorService;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * java虚拟机GC信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:17
 */
@Service
public class JvmGarbageCollectorServiceImpl extends ServiceImpl<IMonitorJvmGarbageCollectorDao, MonitorJvmGarbageCollector> implements IJvmGarbageCollectorService {

    /**
     * <p>
     * 把java虚拟机GC信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 10:38
     */
    @Retryable
    @Override
    public void operateMonitorJvmGarbageCollector(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // GC信息
        GarbageCollectorDomain garbageCollectorDomain = jvmPackage.getJvm().getGarbageCollectorDomain();
        if (garbageCollectorDomain != null) {
            // 详细GC信息列表
            List<GarbageCollectorDomain.GarbageCollectorInfoDomain> garbageCollectorInfoDomains = garbageCollectorDomain.getGarbageCollectorInfoDomains();
            // 要添加的java虚拟机GC信息
            List<MonitorJvmGarbageCollector> saveMonitorJvmGarbageCollectors = Lists.newArrayList();
            for (int i = 0; i < garbageCollectorInfoDomains.size(); i++) {
                // 详细GC信息
                GarbageCollectorDomain.GarbageCollectorInfoDomain garbageCollectorInfoDomain = garbageCollectorInfoDomains.get(i);
                // 查询数据库，看有没有当前应用的GC信息
                LambdaQueryWrapper<MonitorJvmGarbageCollector> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorJvmGarbageCollector::getInstanceId, instanceId);
                lambdaQueryWrapper.eq(MonitorJvmGarbageCollector::getGarbageCollectorName, garbageCollectorInfoDomain.getName());
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorJvmGarbageCollector monitorJvmGarbageCollector = new MonitorJvmGarbageCollector();
                monitorJvmGarbageCollector.setInstanceId(instanceId);
                monitorJvmGarbageCollector.setGarbageCollectorNo(i + 1);
                monitorJvmGarbageCollector.setGarbageCollectorName(garbageCollectorInfoDomain.getName());
                monitorJvmGarbageCollector.setCollectionCount(garbageCollectorInfoDomain.getCollectionCount());
                monitorJvmGarbageCollector.setCollectionTime(garbageCollectorInfoDomain.getCollectionTime());
                // 新增java虚拟机GC信息
                if (selectCountDb == 0) {
                    monitorJvmGarbageCollector.setInsertTime(currentTime);
                    saveMonitorJvmGarbageCollectors.add(monitorJvmGarbageCollector);
                }
                // 更新java虚拟机GC信息
                else {
                    monitorJvmGarbageCollector.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorJvmGarbageCollector> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorJvmGarbageCollector::getInstanceId, instanceId);
                    lambdaUpdateWrapper.eq(MonitorJvmGarbageCollector::getGarbageCollectorName, monitorJvmGarbageCollector.getGarbageCollectorName());
                    this.update(monitorJvmGarbageCollector, lambdaUpdateWrapper);
                }
            }
            // 有要添加的java虚拟机GC信息
            if (CollectionUtils.isNotEmpty(saveMonitorJvmGarbageCollectors)) {
                ((IJvmGarbageCollectorService) AopContext.currentProxy()).saveBatch(saveMonitorJvmGarbageCollectors);
            }
        }
    }

}
