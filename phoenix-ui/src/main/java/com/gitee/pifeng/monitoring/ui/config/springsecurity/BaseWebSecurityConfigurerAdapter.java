package com.gitee.pifeng.monitoring.ui.config.springsecurity;

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
            "/actuator/shutdown",
            // 健康端点
            "/actuator/health"
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
            "/apple-touch-icon.png",
            "/favicon.ico",
            "/favicon16.png",
            "/favicon32.png",
            "/favicon.svg",
            "/druid/**"
            //"/webjars/**",
            //"/v2/**",
            //"/swagger-resources/**",
            //"/swagger-ui.html",
            //"/docs.html",
            //"/doc.html"
    };

}
