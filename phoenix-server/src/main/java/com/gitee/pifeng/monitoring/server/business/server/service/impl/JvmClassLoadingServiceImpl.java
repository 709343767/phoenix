package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.jvm.ClassLoadingDomain;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmClassLoadingDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmClassLoading;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmClassLoadingService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * java虚拟机类加载信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/12 19:34
 */
@Service
public class JvmClassLoadingServiceImpl extends ServiceImpl<IMonitorJvmClassLoadingDao, MonitorJvmClassLoading> implements IJvmClassLoadingService {

    /**
     * <p>
     * 把java虚拟机类加载信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 8:54
     */
    @Retryable
    @Override
    public void operateMonitorJvmClassLoading(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // 类加载信息
        ClassLoadingDomain classLoadingDomain = jvmPackage.getJvm().getClassLoadingDomain();
        if (classLoadingDomain != null) {
            LambdaQueryWrapper<MonitorJvmClassLoading> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 封装对象
            MonitorJvmClassLoading monitorJvmClassLoading = new MonitorJvmClassLoading();
            monitorJvmClassLoading.setInstanceId(instanceId);
            monitorJvmClassLoading.setTotalLoadedClassCount(classLoadingDomain.getTotalLoadedClassCount());
            monitorJvmClassLoading.setLoadedClassCount(classLoadingDomain.getLoadedClassCount());
            monitorJvmClassLoading.setUnloadedClassCount(classLoadingDomain.getUnloadedClassCount());
            monitorJvmClassLoading.setIsVerbose(classLoadingDomain.isVerbose() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            // 新增java虚拟机类加载信息
            if (selectCountDb == 0) {
                monitorJvmClassLoading.setInsertTime(currentTime);
                this.save(monitorJvmClassLoading);
            }
            // 更新java虚拟机类加载信息
            else {
                monitorJvmClassLoading.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorJvmClassLoading> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
                this.update(monitorJvmClassLoading, lambdaUpdateWrapper);
            }
        }
    }

}
