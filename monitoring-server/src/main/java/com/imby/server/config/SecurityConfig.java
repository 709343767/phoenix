package com.imby.server.config;

import com.imby.server.business.web.service.impl.MonitorUserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 * springsecurity安全访问配置。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 15:02
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 忽略URL
     */
    private static final String[] URLS = {
            "/alarm/accept-alarm-package",
            "/heartbeat/accept-heartbeat-package",
            "/server/accept-server-package",
            "/jvm/accept-jvm-package",
            // 关闭端点
            "/actuator/shutdown"
    };

    /**
     * 忽略静态资源
     */
    private static final String[] RESOURCES = {
            "/images/**",
            "/js/**",
            "/layui/**",
            "/lib/**",
            "/modules/**",
            "/style/**",
            "/tpl/**",
            "/config.js",
            "/favicon.ico",
            "/webjars/**",
            "/v2/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/docs.html",
            "/doc.html",
            "/druid/**"
    };

    /**
     * <p>
     * WebSecurity主要针对全局请求忽略规则配置（比如说静态文件，比如说注册页面）、全局HttpFirewall配置、是否debug配置、全局SecurityFilterChain配置、privilegeEvaluator、expressionHandler、securityInterceptor等。
     * </p>
     *
     * @param web {@link WebSecurity}
     * @author 皮锋
     * @custom.date 2020/7/4 23:01
     */
    @Override
    public void configure(WebSecurity web) {
        // web.ignoring直接绕开spring security的所有filter，直接跳过验证
        web.ignoring().antMatchers(RESOURCES);
        web.ignoring().antMatchers(URLS);
    }

    /**
     * <p>
     * HttpSecurity主要针对权限控制配置。
     * </p>
     *
     * @param httpSecurity {@link HttpSecurity}
     * @author 皮锋
     * @custom.date 2020/7/1 15:23
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // 所有的访问都必须进行认证处理后才可以正常访问
                .anyRequest().authenticated()
                .and()
                // 登录配置
                .formLogin()
                .usernameParameter("account")
                .passwordParameter("password")
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/index")
                .permitAll()
                .and()
                // 记住我
                .rememberMe().rememberMeParameter("remember")
                .tokenValiditySeconds(60 * 60 * 24 * 30)
                .and()
                // 退出登录配置
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                .and()
                // 允许嵌入iframe
                .headers()
                .frameOptions()
                .disable();
    }

    /**
     * <p>
     * 自定义认证数据源
     * </p>
     *
     * @param builder {@link AuthenticationManagerBuilder}
     * @author 皮锋
     * @custom.date 2020/7/5 14:01
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(this.monitorUserService())
                .passwordEncoder(passwordEncoder());
    }

    /**
     * <p>
     * 监控用户服务
     * </p>
     *
     * @return {@link MonitorUserServiceImpl}
     * @author 皮锋
     * @custom.date 2020/7/5 14:03
     */
    @Bean
    public MonitorUserServiceImpl monitorUserService() {
        return new MonitorUserServiceImpl();
    }

    /**
     * <p>
     * 密码加密
     * </p>
     *
     * @return {@link BCryptPasswordEncoder}
     * @author 皮锋
     * @custom.date 2020/7/5 14:00
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
