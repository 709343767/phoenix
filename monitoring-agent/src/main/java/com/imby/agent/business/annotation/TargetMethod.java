package com.imby.agent.business.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义指定代理执行方法的注解
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午1:50:40
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetMethod {

    /**
     * <p>
     * 指定代理执行方法
     * </p>
     *
     * @return 方法名
     * @author 皮锋
     * @custom.date 2020年3月4日 下午1:50:53
     */
    String method();
}
