package com.gitee.pifeng.monitoring.ui.config;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

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
@EnableJdbcHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 未开启第三方认证
@ConditionalOnExpression("!${third-auth.enable:false}")
public class SpringSecurityConfig extends BaseWebSecurityConfigurerAdapter {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

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
     * 设置认证提供者
     * </p>
     *
     * @param builder 身份验证管理器生成器 {@link AuthenticationManagerBuilder}
     * @author 皮锋
     * @custom.date 2020/7/5 14:01
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        super.configure(builder);
        builder.userDetailsService(this.monitorUserService).passwordEncoder(this.passwordEncoder());
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
                .antMatchers("/login", "/logout", "/doLogin").permitAll()
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
                .rememberMeServices(this.rememberMeServices())
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
                .logoutSuccessUrl("/logout-success")
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

    /**
     * <p>
     * 记住我
     * </p>
     *
     * @return {@link RememberMeServices}
     * @author 皮锋
     * @custom.date 2021/10/1 17:55
     */
    @Bean
    public RememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        // 60 * 60 * 24 * 30 = 一个月
        rememberMeServices.setValiditySeconds(2592000);
        rememberMeServices.setRememberMeParameterName("remember-me");
        return rememberMeServices;
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

}
