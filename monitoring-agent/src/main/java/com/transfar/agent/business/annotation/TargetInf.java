package com.transfar.agent.business.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 自定义代理接口注解
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
     * @return Class
     * @author 皮锋
     * @custom.date 2020年3月7日 上午11:32:20
     */
    Class<?> inf();
}