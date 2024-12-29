package com.gitee.pifeng.monitoring.common.util.jvm;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.domain.jvm.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.*;
import java.util.*;

/**
 * <p>
 * JVM工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 21:06
 */
public class JvmUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/20 16:19
     */
    private JvmUtils() {
    }

    /**
     * Java虚拟机运行时系统的管理接口
     */
    private static final RuntimeMXBean RUNTIMEMX_BEAN = ManagementFactory.getRuntimeMXBean();

    /**
     * Java虚拟机线程系统的管理接口
     */
    private static final ThreadMXBean THREADMX_BEAN = ManagementFactory.getThreadMXBean();

    /**
     * Java虚拟机类的加载管理接口
     */
    private static final ClassLoadingMXBean CLASS_LOADINGMX_BEAN = ManagementFactory.getClassLoadingMXBean();

    /**
     * Java虚拟机内存系统的管理接口
     */
    private static final MemoryMXBean MEMORYMX_BEAN = ManagementFactory.getMemoryMXBean();

    /**
     * 内存池的管理接口
     */
    private static final List<MemoryPoolMXBean> MEMORY_POOLMX_BEANS = ManagementFactory.getMemoryPoolMXBeans();

    /**
     * Java虚拟机垃圾回收的管理接口
     */
    private static final List<GarbageCollectorMXBean> GARBAGE_COLLECTORMX_BEANS = ManagementFactory.getGarbageCollectorMXBeans();

    /**
     * <p>
     * 获取JVM运行时信息
     * </p>
     *
     * @return JVM运行时信息
     * @author 皮锋
     * @custom.date 2020/8/13 22:21
     */
    public static RuntimeDomain getRuntimeInfo() {
        return RuntimeDomain.builder()
                .name(RUNTIMEMX_BEAN.getName())
                .vmName(RUNTIMEMX_BEAN.getVmName())
                .vmVendor(RUNTIMEMX_BEAN.getVmVendor())
                .vmVersion(RUNTIMEMX_BEAN.getVmVersion())
                .specName(RUNTIMEMX_BEAN.getSpecName())
                .specVendor(RUNTIMEMX_BEAN.getSpecVendor())
                .specVersion(RUNTIMEMX_BEAN.getSpecVersion())
                .managementSpecVersion(RUNTIMEMX_BEAN.getManagementSpecVersion())
                .classPath(RUNTIMEMX_BEAN.getClassPath())
                .libraryPath(RUNTIMEMX_BEAN.getLibraryPath())
                .isBootClassPathSupported(RUNTIMEMX_BEAN.isBootClassPathSupported())
                .bootClassPath(RUNTIMEMX_BEAN.isBootClassPathSupported() ? RUNTIMEMX_BEAN.getBootClassPath() : null)
                .inputArguments(RUNTIMEMX_BEAN.getInputArguments())
                .uptime(DateUtil.formatBetween(RUNTIMEMX_BEAN.getUptime(), BetweenFormatter.Level.MILLISECOND))
                .startTime(new Date(RUNTIMEMX_BEAN.getStartTime()))
                .build();
    }

    /**
     * <p>
     * 获取线程信息
     * </p>
     *
     * @return 线程信息
     * @author 皮锋
     * @custom.date 2020/8/14 10:33
     */
    public static ThreadDomain getThreadInfo() {
        // 获取所有线程的 ID
        long[] threadIds = THREADMX_BEAN.getAllThreadIds();
        // 所有线程信息
        List<String> threadInfos = Lists.newArrayList();
        // 遍历所有线程
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = THREADMX_BEAN.getThreadInfo(threadId);
            if (threadInfo != null) {
                threadInfos.add(StringUtils.trim(threadInfo.toString()));
            }
        }
        // 按字母顺序排序
        threadInfos.sort(Comparator.naturalOrder());
        return ThreadDomain.builder()
                .threadCount(THREADMX_BEAN.getThreadCount())
                .peakThreadCount(THREADMX_BEAN.getPeakThreadCount())
                .daemonThreadCount(THREADMX_BEAN.getDaemonThreadCount())
                .totalStartedThreadCount(THREADMX_BEAN.getTotalStartedThreadCount())
                .threadInfos(threadInfos)
                .build();
    }

    /**
     * <p>
     * 获取类加载信息
     * </p>
     *
     * @return 类加载信息
     * @author 皮锋
     * @custom.date 2020/8/14 11:16
     */
    public static ClassLoadingDomain getClassLoadingInfo() {
        return ClassLoadingDomain.builder()
                .totalLoadedClassCount(CLASS_LOADINGMX_BEAN.getTotalLoadedClassCount())
                .loadedClassCount(CLASS_LOADINGMX_BEAN.getLoadedClassCount())
                .unloadedClassCount(CLASS_LOADINGMX_BEAN.getUnloadedClassCount())
                .isVerbose(CLASS_LOADINGMX_BEAN.isVerbose())
                .build();
    }

    /**
     * <p>
     * 获取内存信息
     * </p>
     *
     * @return 内存信息
     * @author 皮锋
     * @custom.date 2020/8/14 12:18
     */
    public static MemoryDomain getMemoryInfo() {
        MemoryUsage heapMemoryUsage = MEMORYMX_BEAN.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = MEMORYMX_BEAN.getNonHeapMemoryUsage();
        // 设置内存信息
        Map<String, MemoryDomain.MemoryUsageDomain> memoryPoolDomainMap = new HashMap<>(6);
        // 设置堆内存
        MemoryDomain.MemoryUsageDomain heapMemoryUsageDomain = wrapMemoryUsageDomain(heapMemoryUsage);
        memoryPoolDomainMap.put("Heap", heapMemoryUsageDomain);
        // 设置非堆内存
        MemoryDomain.MemoryUsageDomain nonHeapMemoryUsageDomain = wrapMemoryUsageDomain(nonHeapMemoryUsage);
        memoryPoolDomainMap.put("Non_Heap", nonHeapMemoryUsageDomain);
        MEMORY_POOLMX_BEANS.forEach(pool -> {
            MemoryUsage memoryUsage = pool.getUsage();
            MemoryDomain.MemoryUsageDomain poolMemoryUsageDomain = wrapMemoryUsageDomain(memoryUsage);
            memoryPoolDomainMap.put(pool.getName().replace(" ", "_"), poolMemoryUsageDomain);
        });
        // 返回内存信息
        return MemoryDomain.builder().memoryUsageDomainMap(memoryPoolDomainMap).build();
    }

    /**
     * <p>
     * 封装内存使用量
     * </p>
     *
     * @param memoryUsage 内存使用量
     * @return {@link MemoryDomain.MemoryUsageDomain}
     * @author 皮锋
     * @custom.date 2021/1/24 22:32
     */
    private static MemoryDomain.MemoryUsageDomain wrapMemoryUsageDomain(MemoryUsage memoryUsage) {
        MemoryDomain.MemoryUsageDomain nonHeapMemoryUsageDomain = new MemoryDomain.MemoryUsageDomain();
        nonHeapMemoryUsageDomain.setInit(memoryUsage.getInit());
        nonHeapMemoryUsageDomain.setUsed(memoryUsage.getUsed());
        nonHeapMemoryUsageDomain.setCommitted(memoryUsage.getCommitted());
        nonHeapMemoryUsageDomain.setMax(memoryUsage.getMax() == -1L ? "未定义" : String.valueOf(memoryUsage.getMax()));
        return nonHeapMemoryUsageDomain;
    }

    /**
     * <p>
     * 获取GC信息
     * </p>
     *
     * @return GC信息
     * @author 皮锋
     * @custom.date 2020/8/14 16:16
     */
    public static GarbageCollectorDomain getGarbageCollectorInfo() {
        List<GarbageCollectorDomain.GarbageCollectorInfoDomain> garbageCollectorInfoDomains = Lists.newLinkedList();
        GARBAGE_COLLECTORMX_BEANS.forEach(collector -> {
            GarbageCollectorDomain.GarbageCollectorInfoDomain garbageCollectorInfoDomain = GarbageCollectorDomain.GarbageCollectorInfoDomain.builder()
                    .name(collector.getName())
                    .collectionCount(collector.getCollectionCount() == -1L ? "未定义" : String.valueOf(collector.getCollectionCount()))
                    .collectionTime(collector.getCollectionTime() == -1L ? "未定义" : DateUtil.formatBetween(collector.getCollectionTime(), BetweenFormatter.Level.MILLISECOND))
                    .build();
            garbageCollectorInfoDomains.add(garbageCollectorInfoDomain);
        });
        return GarbageCollectorDomain.builder()
                .garbageCollectorInfoDomains(garbageCollectorInfoDomains)
                .build();
    }

    /**
     * <p>
     * 获取Java虚拟机信息
     * </p>
     *
     * @return Java虚拟机信息
     * @author 皮锋
     * @custom.date 2020/8/14 17:59
     */
    public static Jvm getJvmInfo() {
        return Jvm.builder()
                .classLoadingDomain(getClassLoadingInfo())
                .memoryDomain(getMemoryInfo())
                .garbageCollectorDomain(getGarbageCollectorInfo())
                .runtimeDomain(getRuntimeInfo())
                .threadDomain(getThreadInfo())
                .build();
    }

}
