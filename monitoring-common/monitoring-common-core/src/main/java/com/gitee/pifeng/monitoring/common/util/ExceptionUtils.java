package com.gitee.pifeng.monitoring.common.util;

/**
 * <p>
 * 异常处理工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/7/17 14:50
 */
public class ExceptionUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/7/17 14:53
     */
    private ExceptionUtils() {
    }

    /**
     * <p>
     * 转换异常信息为字符串
     * </p>
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     * @return 异常信息字符串
     * @author 皮锋
     * @custom.date 2021/6/11 11:04
     */
    public static String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement stet : elements) {
            builder.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n" + builder.toString();
    }

}
