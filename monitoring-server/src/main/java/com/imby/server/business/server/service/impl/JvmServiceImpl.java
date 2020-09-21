package com.imby.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Result;
import com.imby.common.domain.jvm.*;
import com.imby.common.dto.JvmPackage;
import com.imby.server.business.server.dao.*;
import com.imby.server.business.server.entity.*;
import com.imby.server.business.server.service.IJvmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/27 17:42
 */
@Service
public class JvmServiceImpl implements IJvmService {

    /**
     * java虚拟机运行时信息数据访问对象
     */
    @Autowired
    private IMonitorJvmRuntimeDao monitorJvmRuntimeDao;

    /**
     * java虚拟机类加载信息数据访问对象
     */
    @Autowired
    private IMonitorJvmClassLoadingDao monitorJvmClassLoadingDao;

    /**
     * java虚拟机内存信息数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryDao monitorJvmMemoryDao;

    /**
     * java虚拟机线程信息数据访问对象
     */
    @Autowired
    private IMonitorJvmThreadDao monitorJvmThreadDao;

    /**
     * java虚拟机GC信息数据访问对象
     */
    @Autowired
    private IMonitorJvmGarbageCollectorDao monitorJvmGarbageCollectorDao;

    /**
     * <p>
     * 处理java虚拟机信息包
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/8/27 17:45
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Result dealJvmPackage(JvmPackage jvmPackage) {
        // 把java虚拟机运行时信息添加或更新到数据库
        this.operateMonitorJvmRuntime(jvmPackage);
        // 把java虚拟机类加载信息添加或更新到数据库
        this.operateMonitorJvmClassLoading(jvmPackage);
        // 把java虚拟机内存信息添加到数据库
        this.operateMonitorJvmMemory(jvmPackage);
        // 把java虚拟机线程信息添加或更新到数据库
        this.operateMonitorJvmThread(jvmPackage);
        // 把java虚拟机GC信息添加或更新到数据库
        this.operateMonitorJvmGarbageCollector(jvmPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 把java虚拟机GC信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 10:38
     */
    private void operateMonitorJvmGarbageCollector(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // GC信息
        GarbageCollectorDomain garbageCollectorDomain = jvmPackage.getJvm().getGarbageCollectorDomain();
        // 详细GC信息列表
        List<GarbageCollectorDomain.GarbageCollectorInfoDomain> garbageCollectorInfoDomains = garbageCollectorDomain.getGarbageCollectorInfoDomains();

        for (int i = 0; i < garbageCollectorInfoDomains.size(); i++) {
            // 详细GC信息
            GarbageCollectorDomain.GarbageCollectorInfoDomain garbageCollectorInfoDomain = garbageCollectorInfoDomains.get(i);
            // 查询数据库，看有没有当前应用的GC信息
            LambdaQueryWrapper<MonitorJvmGarbageCollector> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmGarbageCollector::getInstanceId, instanceId);
            lambdaQueryWrapper.eq(MonitorJvmGarbageCollector::getGarbageCollectorName, garbageCollectorInfoDomain.getName());
            MonitorJvmGarbageCollector monitorJvmGarbageCollectorDb = this.monitorJvmGarbageCollectorDao.selectOne(lambdaQueryWrapper);
            // 新增或更新
            MonitorJvmGarbageCollector monitorJvmGarbageCollector = new MonitorJvmGarbageCollector();
            monitorJvmGarbageCollector.setInstanceId(instanceId);
            monitorJvmGarbageCollector.setGarbageCollectorNo(i);
            monitorJvmGarbageCollector.setGarbageCollectorName(garbageCollectorInfoDomain.getName());
            monitorJvmGarbageCollector.setCollectionCount(garbageCollectorInfoDomain.getCollectionCount());
            monitorJvmGarbageCollector.setCollectionTime(garbageCollectorInfoDomain.getCollectionTime());
            // 新增java虚拟机GC信息
            if (monitorJvmGarbageCollectorDb == null) {
                monitorJvmGarbageCollector.setInsertTime(jvmPackage.getDateTime());
                this.monitorJvmGarbageCollectorDao.insert(monitorJvmGarbageCollector);
            }
            // 更新java虚拟机GC信息
            else {
                monitorJvmGarbageCollector.setUpdateTime(jvmPackage.getDateTime());
                LambdaUpdateWrapper<MonitorJvmGarbageCollector> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmGarbageCollector::getInstanceId, instanceId);
                lambdaUpdateWrapper.eq(MonitorJvmGarbageCollector::getGarbageCollectorName, monitorJvmGarbageCollector.getGarbageCollectorName());
                this.monitorJvmGarbageCollectorDao.update(monitorJvmGarbageCollector, lambdaUpdateWrapper);
            }
        }
    }

    /**
     * <p>
     * 把java虚拟机线程信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 10:03
     */
    private void operateMonitorJvmThread(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 线程信息
        ThreadDomain threadDomain = jvmPackage.getJvm().getThreadDomain();
        // 封装信息
        MonitorJvmThread monitorJvmThread = new MonitorJvmThread();
        monitorJvmThread.setInstanceId(instanceId);
        monitorJvmThread.setThreadCount(threadDomain.getThreadCount());
        monitorJvmThread.setPeakThreadCount(threadDomain.getPeakThreadCount());
        monitorJvmThread.setTotalStartedThreadCount(threadDomain.getTotalStartedThreadCount());
        monitorJvmThread.setDaemonThreadCount(threadDomain.getDaemonThreadCount());
        // 查询数据库中有没有当前java虚拟机线程信息
        LambdaQueryWrapper<MonitorJvmThread> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
        MonitorJvmThread monitorJvmThreadDb = this.monitorJvmThreadDao.selectOne(lambdaQueryWrapper);
        // 新增java虚拟机线程信息
        if (monitorJvmThreadDb == null) {
            monitorJvmThread.setInsertTime(jvmPackage.getDateTime());
            this.monitorJvmThreadDao.insert(monitorJvmThread);
        }
        // 更新java虚拟机线程信息
        else {
            monitorJvmThread.setUpdateTime(jvmPackage.getDateTime());
            LambdaUpdateWrapper<MonitorJvmThread> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
            this.monitorJvmThreadDao.update(monitorJvmThread, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 把java虚拟机内存信息添加到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 9:28
     */
    private void operateMonitorJvmMemory(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 内存信息
        MemoryDomain memoryDomain = jvmPackage.getJvm().getMemoryDomain();
        // 堆内存信息
        MemoryDomain.HeapMemoryUsageDomain heapMemoryUsageDomain = memoryDomain.getHeapMemoryUsageDomain();
        MonitorJvmMemory monitorJvmHeapMemory = new MonitorJvmMemory();
        monitorJvmHeapMemory.setInstanceId(instanceId);
        monitorJvmHeapMemory.setMemoryType("Heap");
        monitorJvmHeapMemory.setInit(heapMemoryUsageDomain.getInit());
        monitorJvmHeapMemory.setUsed(heapMemoryUsageDomain.getUsed());
        monitorJvmHeapMemory.setCommitted(heapMemoryUsageDomain.getCommitted());
        monitorJvmHeapMemory.setMax(heapMemoryUsageDomain.getMax());
        monitorJvmHeapMemory.setInsertTime(jvmPackage.getDateTime());
        monitorJvmHeapMemory.setUpdateTime(jvmPackage.getDateTime());
        this.monitorJvmMemoryDao.insert(monitorJvmHeapMemory);
        // 堆外内存信息
        MemoryDomain.NonHeapMemoryUsageDomain nonHeapMemoryUsageDomain = memoryDomain.getNonHeapMemoryUsageDomain();
        MonitorJvmMemory monitorJvmNonHeapMemory = new MonitorJvmMemory();
        monitorJvmNonHeapMemory.setInstanceId(instanceId);
        monitorJvmNonHeapMemory.setMemoryType("Non_Heap");
        monitorJvmNonHeapMemory.setInit(nonHeapMemoryUsageDomain.getInit());
        monitorJvmNonHeapMemory.setUsed(nonHeapMemoryUsageDomain.getUsed());
        monitorJvmNonHeapMemory.setCommitted(nonHeapMemoryUsageDomain.getCommitted());
        monitorJvmNonHeapMemory.setMax(nonHeapMemoryUsageDomain.getMax());
        monitorJvmNonHeapMemory.setInsertTime(jvmPackage.getDateTime());
        monitorJvmNonHeapMemory.setUpdateTime(jvmPackage.getDateTime());
        this.monitorJvmMemoryDao.insert(monitorJvmNonHeapMemory);
        // 内存池信息
        Map<String, MemoryDomain.MemoryPoolDomain> memoryPoolDomainMap = memoryDomain.getMemoryPoolDomainMap();
        for (String key : memoryPoolDomainMap.keySet()) {
            // 内存池信息
            MemoryDomain.MemoryPoolDomain memoryPoolDomain = memoryPoolDomainMap.get(key);
            MonitorJvmMemory monitorJvmMemory = new MonitorJvmMemory();
            monitorJvmMemory.setInstanceId(instanceId);
            monitorJvmMemory.setMemoryType(key);
            monitorJvmMemory.setInit(memoryPoolDomain.getInit());
            monitorJvmMemory.setUsed(memoryPoolDomain.getUsed());
            monitorJvmMemory.setCommitted(memoryPoolDomain.getCommitted());
            monitorJvmMemory.setMax(memoryPoolDomain.getMax());
            monitorJvmMemory.setInsertTime(jvmPackage.getDateTime());
            monitorJvmMemory.setUpdateTime(jvmPackage.getDateTime());
            this.monitorJvmMemoryDao.insert(monitorJvmMemory);
        }
    }

    /**
     * <p>
     * 把java虚拟机类加载信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 8:54
     */
    private void operateMonitorJvmClassLoading(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // 类加载信息
        ClassLoadingDomain classLoadingDomain = jvmPackage.getJvm().getClassLoadingDomain();
        LambdaQueryWrapper<MonitorJvmClassLoading> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
        MonitorJvmClassLoading monitorJvmClassLoadingDb = this.monitorJvmClassLoadingDao.selectOne(lambdaQueryWrapper);
        MonitorJvmClassLoading monitorJvmClassLoading = new MonitorJvmClassLoading();
        monitorJvmClassLoading.setInstanceId(instanceId);
        monitorJvmClassLoading.setTotalLoadedClassCount(classLoadingDomain.getTotalLoadedClassCount());
        monitorJvmClassLoading.setLoadedClassCount(classLoadingDomain.getLoadedClassCount());
        monitorJvmClassLoading.setUnloadedClassCount(classLoadingDomain.getUnloadedClassCount());
        monitorJvmClassLoading.setIsVerbose(classLoadingDomain.isVerbose() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
        // 新增java虚拟机类加载信息
        if (monitorJvmClassLoadingDb == null) {
            monitorJvmClassLoading.setInsertTime(jvmPackage.getDateTime());
            this.monitorJvmClassLoadingDao.insert(monitorJvmClassLoading);
        }
        // 更新java虚拟机类加载信息
        else {
            monitorJvmClassLoading.setUpdateTime(jvmPackage.getDateTime());
            LambdaUpdateWrapper<MonitorJvmClassLoading> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
            this.monitorJvmClassLoadingDao.update(monitorJvmClassLoading, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 把java虚拟机运行时信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/27 19:20
     */
    private void operateMonitorJvmRuntime(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // JVM运行时信息
        RuntimeDomain runtimeDomain = jvmPackage.getJvm().getRuntimeDomain();
        LambdaQueryWrapper<MonitorJvmRuntime> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
        MonitorJvmRuntime monitorJvmRuntimeDb = this.monitorJvmRuntimeDao.selectOne(lambdaQueryWrapper);
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
        if (monitorJvmRuntimeDb == null) {
            monitorJvmRuntime.setInsertTime(jvmPackage.getDateTime());
            this.monitorJvmRuntimeDao.insert(monitorJvmRuntime);
        }
        // 更新java虚拟机运行时信息
        else {
            monitorJvmRuntime.setUpdateTime(jvmPackage.getDateTime());
            LambdaUpdateWrapper<MonitorJvmRuntime> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
            this.monitorJvmRuntimeDao.update(monitorJvmRuntime, lambdaUpdateWrapper);
        }
    }
}
