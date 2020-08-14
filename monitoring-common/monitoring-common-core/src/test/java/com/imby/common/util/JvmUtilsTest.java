package com.imby.common.util;

import com.imby.common.domain.Jvm;
import com.imby.common.domain.jvm.*;
import org.junit.Test;

/**
 * <p>
 * 测试JVM工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:22
 */
public class JvmUtilsTest {

    /**
     * <p>
     * 测试获取JVM运行时信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/13 22:24
     */
    @Test
    public void testGetRuntimeInfo() {
        RuntimeDomain runtimeDomain = JvmUtils.getRuntimeInfo();
        System.out.println(runtimeDomain.toJsonString());
    }

    /**
     * <p>
     * 测试获取线程信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 10:33
     */
    @Test
    public void testGetThreadInfo() {
        ThreadDomain threadDomain = JvmUtils.getThreadInfo();
        System.out.println(threadDomain.toJsonString());
    }

    /**
     * <p>
     * 测试获取类加载信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 11:20
     */
    @Test
    public void testGetClassLoadingInfo() {
        ClassLoadingDomain classLoadingDomain = JvmUtils.getClassLoadingInfo();
        System.out.println(classLoadingDomain.toJsonString());
    }

    /**
     * <p>
     * 测试获取内存信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 12:39
     */
    @Test
    public void testGetMemoryInfo() {
        MemoryDomain memoryDomain = JvmUtils.getMemoryInfo();
        System.out.println(memoryDomain.toJsonString());
    }

    /**
     * <p>
     * 获取GC信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 16:34
     */
    @Test
    public void testGetGarbageCollectorInfo() {
        GarbageCollectorDomain garbageCollectorDomain = JvmUtils.getGarbageCollectorInfo();
        System.out.println(garbageCollectorDomain.toJsonString());
    }

    /**
     * <p>
     * 测试获取Java虚拟机信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 18:00
     */
    @Test
    public void testGetJvmInfo() {
        Jvm jvm = JvmUtils.getJvmInfo();
        System.out.println(jvm.toJsonString());
    }

}
