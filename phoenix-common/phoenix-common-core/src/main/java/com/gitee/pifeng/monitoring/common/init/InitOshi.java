package com.gitee.pifeng.monitoring.common.init;

import oshi.SystemInfo;
import oshi.util.GlobalConfig;

/**
 * <p>
 * 初始化oshi
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/9/11 12:36
 */
public class InitOshi {

    /**
     * 初始化oshi，并创建SystemInfo对象
     */
    public static final SystemInfo SYSTEM_INFO = initOshi();

    /**
     * <p>
     * 初始化oshi
     * </p>
     *
     * @return {@link SystemInfo}
     * @author 皮锋
     * @custom.date 2022/6/27 14:51
     */
    private static SystemInfo initOshi() {
        // 全局配置属性
        GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_LOADAVERAGE, true);
        return new SystemInfo();
    }

}