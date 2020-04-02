package com.transfar.agent.business.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 自定义指定代理方法的注解
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
     * 指定方法
     * </p>
     *
     * @return String
     * @author 皮锋
     * @custom.date 2020年3月4日 下午1:50:53
     */
    String method();
}
