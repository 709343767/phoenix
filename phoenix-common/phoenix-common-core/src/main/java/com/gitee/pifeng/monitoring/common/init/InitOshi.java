package com.gitee.pifeng.monitoring.common.init;

import oshi.SystemInfo;

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
    public static final SystemInfo SYSTEM_INFO = new SystemInfo();
}