package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.jvm.ThreadDomain;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmThreadDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmThread;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmThreadService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * java虚拟机线程信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/14 8:12
 */
@Service
public class JvmThreadServiceImpl extends ServiceImpl<IMonitorJvmThreadDao, MonitorJvmThread> implements IJvmThreadService {

    /**
     * <p>
     * 把java虚拟机线程信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 10:03
     */
    @Retryable
    @Override
    public void operateMonitorJvmThread(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // 线程信息
        ThreadDomain threadDomain = jvmPackage.getJvm().getThreadDomain();
        if (threadDomain != null) {
            // 封装对象
            MonitorJvmThread monitorJvmThread = new MonitorJvmThread();
            monitorJvmThread.setInstanceId(instanceId);
            monitorJvmThread.setThreadCount(threadDomain.getThreadCount());
            monitorJvmThread.setPeakThreadCount(threadDomain.getPeakThreadCount());
            monitorJvmThread.setTotalStartedThreadCount(threadDomain.getTotalStartedThreadCount());
            monitorJvmThread.setDaemonThreadCount(threadDomain.getDaemonThreadCount());
            List<String> threadInfos = threadDomain.getThreadInfos();
            if (CollectionUtils.isNotEmpty(threadInfos)) {
                monitorJvmThread.setThreadInfos(String.join(";", threadInfos));
            }
            // 查询数据库中有没有当前java虚拟机线程信息
            LambdaQueryWrapper<MonitorJvmThread> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 新增java虚拟机线程信息
            if (selectCountDb == 0) {
                monitorJvmThread.setInsertTime(currentTime);
                this.save(monitorJvmThread);
            }
            // 更新java虚拟机线程信息
            else {
                monitorJvmThread.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorJvmThread> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
                this.update(monitorJvmThread, lambdaUpdateWrapper);
            }
        }
    }

}
