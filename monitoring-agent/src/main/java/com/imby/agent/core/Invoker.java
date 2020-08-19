package com.imby.agent.core;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * 命令执行器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:00:29
 */
@Slf4j
@Setter
public class Invoker {

    /**
     * 目标方法
     */
    private Method method;

    /**
     * 目标对象
     */
    private Object target;

    /**
     * <p>
     * 实例化Invoker
     * </p>
     *
     * @param method 目标方法
     * @param target 目标对象
     * @return {@link Invoker}
     * @author 皮锋
     * @custom.date 2020/3/4 16:32
     */
    public static Invoker valueOf(Method method, Object target) {
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        return invoker;
    }

    /**
     * <p>
     * 执行
     * </p>
     *
     * @param paramValues 业务参数
     * @return {@link Object}
     * @throws Exception 代理执行业务方法异常
     * @author 皮锋
     * @custom.date 2020年3月4日 下午2:00:42
     */
    public Object invoke(Object... paramValues) throws Exception {
        try {
            return method.invoke(target, paramValues);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error("代理执行业务方法异常！", e);
            throw new Exception(e.getCause());
        }
    }

}