package com.imby.agent.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义指定代理接口的注解
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午1:58:22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetInf {

    /**
     * <p>
     * 指定接口
     * </p>
     *
     * @return {@link Class}
     * @author 皮锋
     * @custom.date 2020年3月7日 上午11:32:20
     */
    Class<?> inf();
}