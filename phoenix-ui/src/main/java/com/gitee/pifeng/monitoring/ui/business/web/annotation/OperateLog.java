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
     * <p>
     * 操作模块
     * </p>
     *
     * @return 操作模块
     * @author 皮锋
     * @custom.date 2021/6/11 11:19
     */
    String operModule();

    /**
     * <p>
     * 操作类型
     * </p>
     *
     * @return 操作类型
     * @author 皮锋
     * @custom.date 2021/6/11 11:20
     */
    String operType();

    /**
     * <p>
     * 操作说明
     * </p>
     *
     * @return 操作说明
     * @author 皮锋
     * @custom.date 2021/6/11 11:20
     */
    String operDesc() default "";

}
