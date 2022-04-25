package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.domain.jvm.*;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.*;
import com.gitee.pifeng.monitoring.server.business.server.entity.*;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmMemoryHistoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.IJvmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 应用实例数据访问对象
     */
    @Autowired
    private IMonitorInstanceDao monitorInstanceDao;

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
     * java虚拟机内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryHistoryDao monitorJvmMemoryHistoryDao;

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
    //@Transactional(rollbackFor = Throwable.class)
    @Override
    @Retryable
    public Result dealJvmPackage(JvmPackage jvmPackage) {
        // 先判断有没有此应用实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, jvmPackage.getInstanceId());
        Integer integer = this.monitorInstanceDao.selectCount(lambdaQueryWrapper);
        if (null == integer || integer <= 0) {
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
            Integer selectCountDb = this.monitorJvmGarbageCollectorDao.selectCount(lambdaQueryWrapper);
            // 封装对象
            MonitorJvmGarbageCollector monitorJvmGarbageCollector = new MonitorJvmGarbageCollector();
            monitorJvmGarbageCollector.setInstanceId(instanceId);
            monitorJvmGarbageCollector.setGarbageCollectorNo(i + 1);
            monitorJvmGarbageCollector.setGarbageCollectorName(garbageCollectorInfoDomain.getName());
            monitorJvmGarbageCollector.setCollectionCount(garbageCollectorInfoDomain.getCollectionCount());
            monitorJvmGarbageCollector.setCollectionTime(garbageCollectorInfoDomain.getCollectionTime());
            // 新增java虚拟机GC信息
            if (selectCountDb == null || selectCountDb == 0) {
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
        Integer selectCountDb = this.monitorJvmThreadDao.selectCount(lambdaQueryWrapper);
        // 新增java虚拟机线程信息
        if (selectCountDb == null || selectCountDb == 0) {
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
        // 内存信息
        Map<String, MemoryDomain.MemoryUsageDomain> memoryUsageDomainMap = jvmPackage.getJvm().getMemoryDomain().getMemoryUsageDomainMap();
        for (Map.Entry<String, MemoryDomain.MemoryUsageDomain> entry : memoryUsageDomainMap.entrySet()) {
            // 内存类型
            String memoryType = entry.getKey();
            // 内存使用量
            MemoryDomain.MemoryUsageDomain memoryPoolDomain = entry.getValue();
            // 查询数据库中有没有当前java虚拟机堆内存信息
            LambdaQueryWrapper<MonitorJvmMemory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorJvmMemory::getInstanceId, instanceId);
            lambdaQueryWrapper.eq(MonitorJvmMemory::getMemoryType, memoryType);
            Integer selectCountDb = this.monitorJvmMemoryDao.selectCount(lambdaQueryWrapper);
            // 封装对象
            MonitorJvmMemory monitorJvmMemory = new MonitorJvmMemory();
            monitorJvmMemory.setInstanceId(instanceId);
            monitorJvmMemory.setMemoryType(memoryType);
            monitorJvmMemory.setInit(memoryPoolDomain.getInit());
            monitorJvmMemory.setUsed(memoryPoolDomain.getUsed());
            monitorJvmMemory.setCommitted(memoryPoolDomain.getCommitted());
            monitorJvmMemory.setMax(memoryPoolDomain.getMax());
            // 新增java虚拟机内存信息
            if (selectCountDb == null || selectCountDb == 0) {
                monitorJvmMemory.setInsertTime(jvmPackage.getDateTime());
                this.monitorJvmMemoryDao.insert(monitorJvmMemory);
            }
            // 更新java虚拟机内存信息
            else {
                monitorJvmMemory.setUpdateTime(jvmPackage.getDateTime());
                LambdaUpdateWrapper<MonitorJvmMemory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorJvmMemory::getInstanceId, instanceId);
                lambdaUpdateWrapper.eq(MonitorJvmMemory::getMemoryType, memoryType);
                this.monitorJvmMemoryDao.update(monitorJvmMemory, lambdaUpdateWrapper);
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
        // 内存信息
        Map<String, MemoryDomain.MemoryUsageDomain> memoryUsageDomainMap = jvmPackage.getJvm().getMemoryDomain().getMemoryUsageDomainMap();
        // 要添加的内存信息
        List<MonitorJvmMemoryHistory> saveMonitorJvmMemoryHistory = new ArrayList<>();
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
            monitorJvmMemoryHistory.setInsertTime(jvmPackage.getDateTime());
            monitorJvmMemoryHistory.setUpdateTime(jvmPackage.getDateTime());
            saveMonitorJvmMemoryHistory.add(monitorJvmMemoryHistory);
        }
        this.jvmMemoryHistoryService.saveBatch(saveMonitorJvmMemoryHistory);
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
        Integer selectCountDb = this.monitorJvmClassLoadingDao.selectCount(lambdaQueryWrapper);
        // 封装对象
        MonitorJvmClassLoading monitorJvmClassLoading = new MonitorJvmClassLoading();
        monitorJvmClassLoading.setInstanceId(instanceId);
        monitorJvmClassLoading.setTotalLoadedClassCount(classLoadingDomain.getTotalLoadedClassCount());
        monitorJvmClassLoading.setLoadedClassCount(classLoadingDomain.getLoadedClassCount());
        monitorJvmClassLoading.setUnloadedClassCount(classLoadingDomain.getUnloadedClassCount());
        monitorJvmClassLoading.setIsVerbose(classLoadingDomain.isVerbose() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
        // 新增java虚拟机类加载信息
        if (selectCountDb == null || selectCountDb == 0) {
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
        Integer selectCountDb = this.monitorJvmRuntimeDao.selectCount(lambdaQueryWrapper);
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
        if (selectCountDb == null || selectCountDb == 0) {
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
