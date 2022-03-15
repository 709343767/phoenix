package com.gitee.pifeng.monitoring.ui.config;

import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorUserService;
import com.gitee.pifeng.monitoring.ui.thirdauth.cas.CasConfigurationProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.sql.DataSource;

/**
 * <p>
 * cas + springSecurity安全访问配置。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/11 17:28
 */
@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 开启了第三方认证，而且第三方认证为cas
@ConditionalOnExpression("${third-auth.enable:false}&&'cas'.equalsIgnoreCase('${third-auth.type}')")
public class SpringSecurityCasConfig extends BaseWebSecurityConfigurerAdapter {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    /**
     * 数据源
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 监控用户服务类
     */
    @Autowired
    private IMonitorUserService monitorUserService;

    /**
     * CAS配置属性
     */
    @Autowired
    private CasConfigurationProperties casConfigurationProperties;


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
     * @param auth 身份验证管理器生成器 {@link AuthenticationManagerBuilder}
     * @throws Exception 异常
     * @author 皮锋
     * @custom.date 2022/3/14 9:37
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(this.casAuthenticationProvider());
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
                // 所有请求都需要验证
                .anyRequest().authenticated()
                .and()
                //放行表单登录
                .formLogin()
                .permitAll()
                .and()
                //放行logout
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
                // 配置异常处理
                .exceptionHandling()
                .authenticationEntryPoint(this.casAuthenticationEntryPoint())
                .and()
                // 配置过滤器
                .addFilter(this.casAuthenticationFilter())
                .addFilterBefore(this.logoutFilter(), LogoutFilter.class)
                .addFilterBefore(this.singleSignOutFilter(), CasAuthenticationFilter.class)
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
     * cas认证入口
     * </p>
     *
     * @return {@link CasAuthenticationEntryPoint}
     * @author 皮锋
     * @custom.date 2022/3/14 9:49
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        //设置cas服务端登录url
        casAuthenticationEntryPoint.setLoginUrl(this.casConfigurationProperties.getServerLoginUrl());
        //设置cas客户端信息
        casAuthenticationEntryPoint.setServiceProperties(this.serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * <p>
     * cas客户端信息
     * </p>
     *
     * @return {@link ServiceProperties}
     * @author 皮锋
     * @custom.date 2022/3/14 9:51
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        //设置cas客户端登录完整的url
        serviceProperties.setService(this.casConfigurationProperties.getClientHostUrl() + "/login");
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * <p>
     * cas认证过滤器
     * </p>
     *
     * @return {@link CasAuthenticationFilter}
     * @throws Exception 异常
     * @author 皮锋
     * @custom.date 2022/3/14 9:54
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl("/login");
        casAuthenticationFilter.setServiceProperties(this.serviceProperties());
        return casAuthenticationFilter;
    }

    /**
     * <p>
     * cas认证提供者
     * </p>
     *
     * @return {@link CasAuthenticationProvider}
     * @author 皮锋
     * @custom.date 2022/3/14 9:59
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        //设置UserDetailService，登录成功后，会进入loadUserDetails方法中
        casAuthenticationProvider.setAuthenticationUserDetailsService(this.monitorUserService);
        //设置cas客户端信息
        casAuthenticationProvider.setServiceProperties(this.serviceProperties());
        //设置cas票证验证器
        casAuthenticationProvider.setTicketValidator(this.casTicketValidationFilter());
        //设置cas秘钥
        casAuthenticationProvider.setKey(this.casConfigurationProperties.getKey());
        return casAuthenticationProvider;
    }

    /**
     * <p>
     * cas票证验证器
     * </p>
     *
     * @return {@link Cas20ServiceTicketValidator}
     * @author 皮锋
     * @custom.date 2022/3/14 10:20
     */
    @Bean
    public TicketValidator casTicketValidationFilter() {
        final Cas20ServiceTicketValidator targetCasValidationFilter;
        String serverUrlPrefix = this.casConfigurationProperties.getServerUrlPrefix();
        switch (this.casConfigurationProperties.getValidationType()) {
            case CAS:
                targetCasValidationFilter = new Cas20ServiceTicketValidator(serverUrlPrefix);
                break;
            case CAS3:
                targetCasValidationFilter = new Cas30ServiceTicketValidator(serverUrlPrefix);
                break;
            default:
                throw new IllegalStateException("Unknown CAS validation type");
        }
        return targetCasValidationFilter;
    }

    /**
     * <p>
     * 单点注销过滤器，用于接收cas服务端的注销请求
     * </p>
     *
     * @return {@link SingleSignOutFilter}
     * @author 皮锋
     * @custom.date 2022/3/14 10:21
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    /**
     * <p>
     * 单点登出过滤器，用于跳转到cas服务端
     * </p>
     *
     * @return {@link LogoutFilter}
     * @author 皮锋
     * @custom.date 2022/3/14 10:23
     */
    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(this.casConfigurationProperties.getServerLogoutUrl(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout");
        return logoutFilter;
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
