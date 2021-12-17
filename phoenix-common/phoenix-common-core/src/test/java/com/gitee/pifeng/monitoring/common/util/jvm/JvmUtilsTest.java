package com.gitee.pifeng.monitoring.common.util.jvm;

import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.domain.jvm.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试JVM工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:22
 */
@Slf4j
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
        assertNotNull(runtimeDomain);
        log.info(runtimeDomain.toJsonString());
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
        assertNotNull(threadDomain);
        log.info(threadDomain.toJsonString());
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
        assertNotNull(classLoadingDomain);
        log.info(classLoadingDomain.toJsonString());
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
        assertNotNull(memoryDomain);
        log.info(memoryDomain.toJsonString());
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
        assertNotNull(garbageCollectorDomain);
        log.info(garbageCollectorDomain.toJsonString());
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
        assertNotNull(jvm);
        log.info(jvm.toJsonString());
    }

}
