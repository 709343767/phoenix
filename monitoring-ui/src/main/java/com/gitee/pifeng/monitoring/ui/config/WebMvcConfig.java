package com.gitee.pifeng.monitoring.ui.config;

import com.gitee.pifeng.monitoring.ui.business.web.component.OperateLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * MVC配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/7/17 13:50
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 操作日志拦截器
     */
    @Autowired
    private OperateLogInterceptor operateLogInterceptor;

    /**
     * <p>
     * 添加 spring MVC 拦截器
     * </p>
     *
     * @param registry 拦截器注册表
     * @author 皮锋
     * @custom.date 2021/7/17 13:58
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加操作日志拦截器
        registry.addInterceptor(this.operateLogInterceptor);
    }

}
