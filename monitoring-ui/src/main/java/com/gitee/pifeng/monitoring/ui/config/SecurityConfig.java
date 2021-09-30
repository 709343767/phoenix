package com.gitee.pifeng.monitoring.ui.config;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.sql.DataSource;

/**
 * <p>
 * springSecurity安全访问配置。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 15:02
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    /**
     * 忽略URL
     */
    private static final String[] URLS = {
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
            //"/webjars/**",
            //"/v2/**",
            //"/swagger-resources/**",
            //"/swagger-ui.html",
            //"/docs.html",
            //"/doc.html",
            "/druid/**"
    };

    /**
     * 监控用户服务类
     */
    @Autowired
    private IMonitorUserService monitorUserService;

    /**
     * 数据源
     */
    @Autowired
    private DataSource dataSource;

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
                // 如果有允许匿名的url，填在下面
                .antMatchers("/login", "/logout").permitAll()
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
                .defaultSuccessUrl("/login-success", true)
                .permitAll()
                .and()
                // 记住我
                .rememberMe()
                .rememberMeParameter("remember-me")
                // 60 * 60 * 24 * 30 = 一个月
                .tokenValiditySeconds(2592000)
                // 持久化token
                .tokenRepository(this.persistentTokenRepository())
                .userDetailsService(this.monitorUserService)
                .and()
                .sessionManagement()
                // session超时后的操作
                .invalidSessionUrl("/login?timeout=true")
                .maximumSessions(-1)
                // 当达到最大值时，是否保留已经登录的用户
                .maxSessionsPreventsLogin(false)
                // 当达到最大值时，旧用户被踢出后的操作
                .expiredUrl("/login?expire=true")
                .sessionRegistry(this.sessionRegistry())
                .and()
                .and()
                // 退出登录配置
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("SESSION", "JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                .and()
                // 允许嵌入iframe
                .headers()
                // 禁用缓存
                .cacheControl()
                .disable()
                .frameOptions()
                .disable();
    }

    /**
     * <p>
     * 持久化token
     * </p>
     * spring Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中，<br>
     * 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库。
     *
     * @return {@link PersistentTokenRepository}
     * @author 皮锋
     * @custom.date 2021/7/1 11:29
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 启动创建表，创建成功后注释掉
        jdbcTokenRepository.setCreateTableOnStartup(false);
        // 设置数据源
        jdbcTokenRepository.setDataSource(this.dataSource);
        return jdbcTokenRepository;
    }

    /**
     * <p>
     * 维护session注册信息
     * </p>
     *
     * @return {@link SessionRegistry}
     * @author 皮锋
     * @custom.date 2021/6/24 18:12
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
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
        builder.userDetailsService(this.monitorUserService).passwordEncoder(this.passwordEncoder());
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
