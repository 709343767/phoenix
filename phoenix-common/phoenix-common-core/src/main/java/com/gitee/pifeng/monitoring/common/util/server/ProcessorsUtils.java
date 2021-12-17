package com.gitee.pifeng.monitoring.common.util.server;

/**
 * <p>
 * 处理器工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 12:27
 */
public class ProcessorsUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 9:44
     */
    private ProcessorsUtils() {
    }

    /**
     * <p>
     * 获取系统可用的处理器核心数
     * </p>
     *
     * @return 系统可用的处理器核心数
     * @author 皮锋
     * @custom.date 2020/8/25 9:04
     */
    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

}
