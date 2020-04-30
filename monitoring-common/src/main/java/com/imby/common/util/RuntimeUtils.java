package com.imby.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * <p>
 * 获取运行时信息的工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/11 22:16
 */
public class RuntimeUtils {

    /**
     * <p>
     * 获取java进程PID
     * </p>
     *
     * @return PID
     * @author 皮锋
     * @custom.date 2020年3月4日 下午10:48:07
     */
    public static String getJavaPid() {
        String pid = null;
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        int index = name.indexOf("@");
        if (index != -1) {
            pid = name.substring(0, index);
        }
        return pid;
    }

}
