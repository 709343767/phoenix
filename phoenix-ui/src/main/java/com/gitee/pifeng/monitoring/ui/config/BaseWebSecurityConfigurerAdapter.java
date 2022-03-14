package com.gitee.pifeng.monitoring.ui.config;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p>
 * 基础 Web 安全配置适配器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/14 9:19
 */
public class BaseWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    /**
     * 忽略URL
     */
    protected static final String[] URLS = {
            // 关闭端点
            "/actuator/shutdown"
    };

    /**
     * 忽略静态资源
     */
    protected static final String[] RESOURCES = {
            "/images/**",
            "/js/**",
            "/layui/**",
            "/lib/**",
            "/modules/**",
            "/style/**",
            "/tpl/**",
            "/config.js",
            //"/webjars/**",
            //"/v2/**",
            //"/swagger-resources/**",
            //"/swagger-ui.html",
            //"/docs.html",
            //"/doc.html",
            "/druid/**"
    };

}
