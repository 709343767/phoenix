package com.gitee.pifeng.monitoring.ui.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <p>
 * 打印IOC容器中所有的Bean名称
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/10/26 10:58
 */
@Component
@Profile("dev")
@Slf4j
public class ApplicationContextBean implements ApplicationContextAware, InitializingBean {

    public static ApplicationContext applicationContext;

    /**
     * <p>
     * 获取 ApplicationContext
     * </p>
     *
     * @param applicationContext {@link ApplicationContext}
     * @throws BeansException Beans异常
     * @author 皮锋
     * @custom.date 2023/10/26 10:59
     */
    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        ApplicationContextBean.applicationContext = applicationContext;
    }

    /**
     * <p>
     * 打印IOC容器中所有的Bean名称
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/10/26 11:01
     */
    @Override
    public void afterPropertiesSet() {
        String[] names = applicationContext.getBeanDefinitionNames();
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        for (String name : names) {
            log.info(name);
        }
        log.info("------ Bean 总计:" + applicationContext.getBeanDefinitionCount());
    }

}
