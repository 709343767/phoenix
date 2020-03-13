package com.transfar.annotation;

import com.transfar.selector.EnableMonitoringPlugSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * 激活监控功能
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/13 15:02
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
// 导入注册springboot类
@Import({ EnableMonitoringPlugSelector.class })
@Documented
public @interface EnableMonitoringPlug {
}
