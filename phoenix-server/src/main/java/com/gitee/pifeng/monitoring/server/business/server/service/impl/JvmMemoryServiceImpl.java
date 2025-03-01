package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.jvm.MemoryDomain;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmMemoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemory;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmMemoryService;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机内存信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:06
 */
@Service
public class JvmMemoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryDao, MonitorJvmMemory> implements IJvmMemoryService {

    /**
     * <p>
     * 把java虚拟机内存信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2021/1/24 21:51
     */
    @Retryable
    @Override
    public void operateMonitorJvmMemory(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // 内存信息
        MemoryDomain memoryDomain = jvmPackage.getJvm().getMemoryDomain();
        if (memoryDomain != null) {
            Map<String, MemoryDomain.MemoryUsageDomain> memoryUsageDomainMap = memoryDomain.getMemoryUsageDomainMap();
            // 要添加的java虚拟机内存信息
            List<MonitorJvmMemory> saveMonitorJvmMemories = Lists.newArrayList();
            for (Map.Entry<String, MemoryDomain.MemoryUsageDomain> entry : memoryUsageDomainMap.entrySet()) {
                // 内存类型
                String memoryType = entry.getKey();
                // 内存使用量
                MemoryDomain.MemoryUsageDomain memoryPoolDomain = entry.getValue();
                // 查询数据库中有没有当前java虚拟机堆内存信息
                LambdaQueryWrapper<MonitorJvmMemory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorJvmMemory::getInstanceId, instanceId);
                lambdaQueryWrapper.eq(MonitorJvmMemory::getMemoryType, memoryType);
                int selectCountDb = this.count(lambdaQueryWrapper);
                // 封装对象
                MonitorJvmMemory monitorJvmMemory = new MonitorJvmMemory();
                monitorJvmMemory.setInstanceId(instanceId);
                monitorJvmMemory.setMemoryType(memoryType);
                monitorJvmMemory.setInit(memoryPoolDomain.getInit());
                monitorJvmMemory.setUsed(memoryPoolDomain.getUsed());
                monitorJvmMemory.setCommitted(memoryPoolDomain.getCommitted());
                monitorJvmMemory.setMax(memoryPoolDomain.getMax());
                // 新增java虚拟机内存信息
                if (selectCountDb == 0) {
                    monitorJvmMemory.setInsertTime(currentTime);
                    saveMonitorJvmMemories.add(monitorJvmMemory);
                }
                // 更新java虚拟机内存信息
                else {
                    monitorJvmMemory.setUpdateTime(currentTime);
                    LambdaUpdateWrapper<MonitorJvmMemory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(MonitorJvmMemory::getInstanceId, instanceId);
                    lambdaUpdateWrapper.eq(MonitorJvmMemory::getMemoryType, memoryType);
                    this.update(monitorJvmMemory, lambdaUpdateWrapper);
                }
            }
            // 有要添加的java虚拟机内存信息
            if (CollectionUtils.isNotEmpty(saveMonitorJvmMemories)) {
                ((IJvmMemoryService) AopContext.currentProxy()).saveBatch(saveMonitorJvmMemories);
            }
        }
    }

}
