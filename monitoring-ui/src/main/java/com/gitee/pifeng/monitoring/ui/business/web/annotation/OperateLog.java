package com.gitee.pifeng.monitoring.ui.business.web.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义操作日志注解
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/6/9 18:04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    /**
     * 操作模块
     */
    String operModul();

    /**
     * 操作类型
     */
    String operType();

    /**
     * 操作说明
     */
    String operDesc() default "";

}
