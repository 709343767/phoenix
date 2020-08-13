package com.imby.common.util;

import com.imby.common.domain.server.JvmDomain;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

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
     * Java虚拟机运行时系统的管理接口
     */
    private final static RuntimeMXBean RUNTIMEMX_BEAN = ManagementFactory.getRuntimeMXBean();

    /**
     * <p>
     * 获取JVM运行时信息
     * </p>
     *
     * @return JVM运行时信息
     * @author 皮锋
     * @custom.date 2020/8/13 22:21
     */
    public static JvmDomain.RuntimeDomain getRuntimeDomainInfo() {
        return JvmDomain.RuntimeDomain.builder()
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
                .uptime(RUNTIMEMX_BEAN.getUptime())
                .startTime(new Date(RUNTIMEMX_BEAN.getStartTime()))
                .systemProperties(RUNTIMEMX_BEAN.getSystemProperties())
                .build();
    }

}
