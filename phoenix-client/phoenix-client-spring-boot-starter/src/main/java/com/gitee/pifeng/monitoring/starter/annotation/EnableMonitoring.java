package com.gitee.pifeng.monitoring.starter.annotation;

import com.gitee.pifeng.monitoring.starter.selector.EnableMonitoringPlugSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * 开启监控功能
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/13 15:02
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
// 导入注册springboot类
@Import({EnableMonitoringPlugSelector.class})
@Documented
public @interface EnableMonitoring {

    /**
     * <p>
     * 设置配置文件路径，可以是类路径，也可以是文件路径<br>
     * </p>
     * 例如：configFilePath = "classpath:conf/" 或者 configFilePath = "filepath:conf/"
     *
     * @return 配置文件路径
     * @author 皮锋
     * @custom.date 2020/3/15 21:18
     */
    String configFilePath() default "";

    /**
     * <p>
     * 设置配置文件名字<br>
     * 例如：configFileName = "monitoring.properties"
     * </p>
     *
     * @return 配置文件名字
     * @author 皮锋
     * @custom.date 2020/3/15 21:18
     */
    String configFileName() default "";

    /**
     * <p>
     * 是否使用独立的监控配置文件。<br>
     * 如果是，则需要添加一个独立的监控配置文件（monitoring.properties 或者 monitoring-{profile}.properties），<br>
     * 如果不是，则共用springboot的 application.yml 配置文件，不需要自己添加监控配置文件了<br>
     * </p>
     *
     * @return 是否使用独立的监控配置文件
     * @author 皮锋
     * @custom.date 2024/3/27 20:07
     */
    boolean usingMonitoringConfigFile() default false;

}
