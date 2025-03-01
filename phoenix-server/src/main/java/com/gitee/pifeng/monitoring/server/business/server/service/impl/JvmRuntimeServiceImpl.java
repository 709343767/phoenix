package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.jvm.RuntimeDomain;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmRuntimeDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmRuntime;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmRuntimeService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * java虚拟机运行时信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/12 19:27
 */
@Service
public class JvmRuntimeServiceImpl extends ServiceImpl<IMonitorJvmRuntimeDao, MonitorJvmRuntime> implements IJvmRuntimeService {

    /**
     * <p>
     * 把java虚拟机运行时信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/27 19:20
     */
    @Retryable
    @Override
    public void operateMonitorJvmRuntime(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 当前时间
        Date currentTime = new Date();
        // JVM运行时信息
        RuntimeDomain runtimeDomain = jvmPackage.getJvm().getRuntimeDomain();
        if (runtimeDomain != null) {
            LambdaQueryWrapper<MonitorJvmRuntime> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 封装对象
            MonitorJvmRuntime monitorJvmRuntime = new MonitorJvmRuntime();
            monitorJvmRuntime.setInstanceId(instanceId);
            monitorJvmRuntime.setName(runtimeDomain.getName());
            monitorJvmRuntime.setVmName(runtimeDomain.getVmName());
            monitorJvmRuntime.setVmVendor(runtimeDomain.getVmVendor());
            monitorJvmRuntime.setVmVersion(runtimeDomain.getVmVersion());
            monitorJvmRuntime.setSpecName(runtimeDomain.getSpecName());
            monitorJvmRuntime.setSpecVendor(runtimeDomain.getSpecVendor());
            monitorJvmRuntime.setSpecVersion(runtimeDomain.getSpecVersion());
            monitorJvmRuntime.setManagementSpecVersion(runtimeDomain.getManagementSpecVersion());
            monitorJvmRuntime.setClassPath(runtimeDomain.getClassPath());
            monitorJvmRuntime.setLibraryPath(runtimeDomain.getLibraryPath());
            monitorJvmRuntime.setIsBootClassPathSupported(runtimeDomain.isBootClassPathSupported() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorJvmRuntime.setBootClassPath(runtimeDomain.getBootClassPath());
            monitorJvmRuntime.setInputArguments(ArrayUtil.join(runtimeDomain.getInputArguments().toArray(new String[0]), ";"));
            monitorJvmRuntime.setUptime(runtimeDomain.getUptime());
            monitorJvmRuntime.setStartTime(runtimeDomain.getStartTime());
            // 新增java虚拟机运行时信息
            if (selectCountDb == 0) {
                monitorJvmRuntime.setInsertTime(currentTime);
                this.save(monitorJvmRuntime);
            }
            // 更新java虚拟机运行时信息
            else {
                monitorJvmRuntime.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorJvmRuntime> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
                this.update(monitorJvmRuntime, lambdaUpdateWrapper);
            }
        }
    }

}
