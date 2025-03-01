package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.jvm.MemoryDomain;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmMemoryHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemoryHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmMemoryHistoryService;
import org.assertj.core.util.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机内存历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/6 9:57
 */
@Service
public class JvmMemoryHistoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryHistoryDao, MonitorJvmMemoryHistory> implements IJvmMemoryHistoryService {

    /**
     * <p>
     * 把java虚拟机内存历史信息添加到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 9:28
     */
    @Retryable
    @Override
    public void operateMonitorJvmMemoryHistory(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // 内存信息
        MemoryDomain memoryDomain = jvmPackage.getJvm().getMemoryDomain();
        if (memoryDomain != null) {
            Map<String, MemoryDomain.MemoryUsageDomain> memoryUsageDomainMap = memoryDomain.getMemoryUsageDomainMap();
            // 要添加的内存信息
            List<MonitorJvmMemoryHistory> saveMonitorJvmMemoryHistory = Lists.newArrayList();
            for (Map.Entry<String, MemoryDomain.MemoryUsageDomain> entry : memoryUsageDomainMap.entrySet()) {
                // 内存类型
                String memoryType = entry.getKey();
                // 内存使用量
                MemoryDomain.MemoryUsageDomain memoryPoolDomain = entry.getValue();
                // 封装对象
                MonitorJvmMemoryHistory monitorJvmMemoryHistory = new MonitorJvmMemoryHistory();
                monitorJvmMemoryHistory.setInstanceId(instanceId);
                monitorJvmMemoryHistory.setMemoryType(memoryType);
                monitorJvmMemoryHistory.setInit(memoryPoolDomain.getInit());
                monitorJvmMemoryHistory.setUsed(memoryPoolDomain.getUsed());
                monitorJvmMemoryHistory.setCommitted(memoryPoolDomain.getCommitted());
                monitorJvmMemoryHistory.setMax(memoryPoolDomain.getMax());
                monitorJvmMemoryHistory.setInsertTime(currentTime);
                monitorJvmMemoryHistory.setUpdateTime(currentTime);
                saveMonitorJvmMemoryHistory.add(monitorJvmMemoryHistory);
            }
            ((IJvmMemoryHistoryService) AopContext.currentProxy()).saveBatch(saveMonitorJvmMemoryHistory);
        }
    }

}
