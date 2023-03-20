package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.domain.jvm.*;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmMemoryHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.*;
import com.gitee.pifeng.monitoring.server.business.server.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
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
     * java虚拟机内存历史记录服务接口
     */
    @Autowired
    private IJvmMemoryHistoryService jvmMemoryHistoryService;

    /**
     * java虚拟机内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryHistoryDao monitorJvmMemoryHistoryDao;

    /**
     * 应用实例服务接口
     */
    @Autowired
    private IInstanceService instanceService;

    /**
     * java虚拟机运行时信息服务接口
     */
    @Autowired
    private IJvmRuntimeService jvmRuntimeService;

    /**
     * java虚拟机类加载信息服务接口
     */
    @Autowired
    private IJvmClassLoadingService jvmClassLoadingService;

    /**
     * java虚拟机内存信息服务接口
     */
    @Autowired
    private IJvmMemoryService jvmMemoryService;

    /**
     * java虚拟机线程信息服务接口
     */
    @Autowired
    private IJvmThreadService jvmThreadService;

    /**
     * java虚拟机GC信息服务接口
     */
    @Autowired
    private IJvmGarbageCollectorService jvmGarbageCollectorService;

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
    //@Transactional(rollbackFor = Throwable.class)
    @Override
    @Retryable
    public Result dealJvmPackage(JvmPackage jvmPackage) {
        // 先判断有没有此应用实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, jvmPackage.getInstanceId());
        int count = this.instanceService.count(lambdaQueryWrapper);
        if (count == 0) {
            return Result.builder().isSuccess(false).msg(ResultMsgConstants.FAILURE).build();
        }
        // 把java虚拟机运行时信息添加或更新到数据库
        this.operateMonitorJvmRuntime(jvmPackage);
        // 把java虚拟机类加载信息添加或更新到数据库
        this.operateMonitorJvmClassLoading(jvmPackage);
        // 把java虚拟机内存信息添加或更新到数据库
        this.operateMonitorJvmMemory(jvmPackage);
        // 把java虚拟机内存历史信息添加到数据库
        this.operateMonitorJvmMemoryHistory(jvmPackage);
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
                int selectCountDb = this.jvmGarbageCollectorService.count(lambdaQueryWrapper);
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
                    this.jvmGarbageCollectorService.update(monitorJvmGarbageCollector, lambdaUpdateWrapper);
                }
            }
            // 有要添加的java虚拟机GC信息
            if (CollectionUtils.isNotEmpty(saveMonitorJvmGarbageCollectors)) {
                this.jvmGarbageCollectorService.saveBatch(saveMonitorJvmGarbageCollectors);
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
            // 查询数据库中有没有当前java虚拟机线程信息
            LambdaQueryWrapper<MonitorJvmThread> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
            int selectCountDb = this.jvmThreadService.count(lambdaQueryWrapper);
            // 新增java虚拟机线程信息
            if (selectCountDb == 0) {
                monitorJvmThread.setInsertTime(currentTime);
                this.jvmThreadService.save(monitorJvmThread);
            }
            // 更新java虚拟机线程信息
            else {
                monitorJvmThread.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorJvmThread> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmThread::getInstanceId, instanceId);
                this.jvmThreadService.update(monitorJvmThread, lambdaUpdateWrapper);
            }
        }
    }

    /**
     * <p>
     * 把java虚拟机内存信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2021/1/24 21:51
     */
    private void operateMonitorJvmMemory(JvmPackage jvmPackage) {
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
                int selectCountDb = this.jvmMemoryService.count(lambdaQueryWrapper);
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
                    this.jvmMemoryService.update(monitorJvmMemory, lambdaUpdateWrapper);
                }
            }
            // 有要添加的java虚拟机内存信息
            if (CollectionUtils.isNotEmpty(saveMonitorJvmMemories)) {
                this.jvmMemoryService.saveBatch(saveMonitorJvmMemories);
            }
        }
    }

    /**
     * <p>
     * 把java虚拟机内存历史信息添加到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 9:28
     */
    private void operateMonitorJvmMemoryHistory(JvmPackage jvmPackage) {
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
            this.jvmMemoryHistoryService.saveBatch(saveMonitorJvmMemoryHistory);
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
        // 当前时间
        Date currentTime = new Date();
        // 类加载信息
        ClassLoadingDomain classLoadingDomain = jvmPackage.getJvm().getClassLoadingDomain();
        if (classLoadingDomain != null) {
            LambdaQueryWrapper<MonitorJvmClassLoading> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
            int selectCountDb = this.jvmClassLoadingService.count(lambdaQueryWrapper);
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
                this.jvmClassLoadingService.save(monitorJvmClassLoading);
            }
            // 更新java虚拟机类加载信息
            else {
                monitorJvmClassLoading.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorJvmClassLoading> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmClassLoading::getInstanceId, instanceId);
                this.jvmClassLoadingService.update(monitorJvmClassLoading, lambdaUpdateWrapper);
            }
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
        // 当前时间
        Date currentTime = new Date();
        // JVM运行时信息
        RuntimeDomain runtimeDomain = jvmPackage.getJvm().getRuntimeDomain();
        if (runtimeDomain != null) {
            LambdaQueryWrapper<MonitorJvmRuntime> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
            int selectCountDb = this.jvmRuntimeService.count(lambdaQueryWrapper);
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
                this.jvmRuntimeService.save(monitorJvmRuntime);
            }
            // 更新java虚拟机运行时信息
            else {
                monitorJvmRuntime.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorJvmRuntime> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
                this.jvmRuntimeService.update(monitorJvmRuntime, lambdaUpdateWrapper);
            }
        }
    }

    /**
     * <p>
     * 清理JVM历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2021/12/9 20:46
     */
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaUpdateWrapper<MonitorJvmMemoryHistory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.le(MonitorJvmMemoryHistory::getInsertTime, historyTime);
        return this.monitorJvmMemoryHistoryDao.delete(lambdaUpdateWrapper);
    }

}
