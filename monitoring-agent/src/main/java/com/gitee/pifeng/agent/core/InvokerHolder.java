package com.gitee.pifeng.agent.core;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 命令执行器管理器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:02:05
 */
public class InvokerHolder {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private InvokerHolder() {
    }

    /**
     * 命令执行器
     */
    private static final Map<Class<?>, Map<String, Invoker>> INVOKERS = new HashMap<>();

    /**
     * <p>
     * 获取执行器
     * </p>
     *
     * @param clazz  类-{@link Class}
     * @param method 方法
     * @return {@link Invoker}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午2:02:18
     */
    public static Invoker getInvoker(Class<?> clazz, String method) {
        Map<String, Invoker> map = INVOKERS.get(clazz);
        if (map != null) {
            return map.get(method);
        }
        return null;
    }

    /**
     * <p>
     * 添加执行器
     * </p>
     *
     * @param clazz   类-{@link Class}
     * @param method  方法
     * @param invoker 命令执行器-{@link Invoker}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午2:02:35
     */
    public static void addInvoker(Class<?> clazz, String method, Invoker invoker) {
        Map<String, Invoker> map = INVOKERS.computeIfAbsent(clazz, k -> new HashMap<>(16));
        map.put(method, invoker);
    }

}