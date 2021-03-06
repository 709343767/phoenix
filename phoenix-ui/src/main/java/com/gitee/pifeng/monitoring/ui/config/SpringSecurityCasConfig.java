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
 * cas + springSecurity?????????????????????
 * </p>
 *
 * @author ??????
 * @custom.date 2022/3/11 17:28
 */
@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
// ???????????????????????????????????????????????????cas
@ConditionalOnExpression("${third-auth.enable:false}&&'cas'.equalsIgnoreCase('${third-auth.type}')")
public class SpringSecurityCasConfig extends BaseWebSecurityConfigurerAdapter {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    /**
     * ?????????
     */
    @Autowired
    private DataSource dataSource;

    /**
     * ?????????????????????
     */
    @Autowired
    private IMonitorUserService monitorUserService;

    /**
     * CAS????????????
     */
    @Autowired
    private CasConfigurationProperties casConfigurationProperties;


    /**
     * <p>
     * WebSecurity??????????????????????????????????????????????????????????????????????????????????????????????????????HttpFirewall???????????????debug???????????????SecurityFilterChain?????????privilegeEvaluator???expressionHandler???securityInterceptor??????
     * </p>
     *
     * @param web {@link WebSecurity}
     * @author ??????
     * @custom.date 2020/7/4 23:01
     */
    @Override
    public void configure(WebSecurity web) {
        // web.ignoring????????????spring security?????????filter?????????????????????
        web.ignoring().antMatchers(RESOURCES);
        web.ignoring().antMatchers(URLS);
    }

    /**
     * <p>
     * ?????????????????????
     * </p>
     *
     * @param auth ?????????????????????????????? {@link AuthenticationManagerBuilder}
     * @throws Exception ??????
     * @author ??????
     * @custom.date 2022/3/14 9:37
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(this.casAuthenticationProvider());
    }

    /**
     * <p>
     * HttpSecurity?????????????????????????????????
     * </p>
     *
     * @param httpSecurity {@link HttpSecurity}
     * @author ??????
     * @custom.date 2020/7/1 15:23
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // ???????????????????????????
                .anyRequest().authenticated()
                .and()
                //??????????????????
                .formLogin()
                .permitAll()
                .and()
                //??????logout
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
                // ??????????????????
                .exceptionHandling()
                .authenticationEntryPoint(this.casAuthenticationEntryPoint())
                .and()
                // ???????????????
                .addFilter(this.casAuthenticationFilter())
                .addFilterBefore(this.logoutFilter(), LogoutFilter.class)
                .addFilterBefore(this.singleSignOutFilter(), CasAuthenticationFilter.class)
                // ????????????iframe
                .headers()
                // ????????????
                .cacheControl()
                .disable()
                .frameOptions()
                .disable();
    }

    /**
     * <p>
     * cas????????????
     * </p>
     *
     * @return {@link CasAuthenticationEntryPoint}
     * @author ??????
     * @custom.date 2022/3/14 9:49
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        //??????cas???????????????url
        casAuthenticationEntryPoint.setLoginUrl(this.casConfigurationProperties.getServerLoginUrl());
        //??????cas???????????????
        casAuthenticationEntryPoint.setServiceProperties(this.serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * <p>
     * cas???????????????
     * </p>
     *
     * @return {@link ServiceProperties}
     * @author ??????
     * @custom.date 2022/3/14 9:51
     */
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        //??????cas????????????????????????url
        serviceProperties.setService(this.casConfigurationProperties.getClientHostUrl() + "/login");
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    /**
     * <p>
     * cas???????????????
     * </p>
     *
     * @return {@link CasAuthenticationFilter}
     * @throws Exception ??????
     * @author ??????
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
     * cas???????????????
     * </p>
     *
     * @return {@link CasAuthenticationProvider}
     * @author ??????
     * @custom.date 2022/3/14 9:59
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        //??????UserDetailService??????????????????????????????loadUserDetails?????????
        casAuthenticationProvider.setAuthenticationUserDetailsService(this.monitorUserService);
        //??????cas???????????????
        casAuthenticationProvider.setServiceProperties(this.serviceProperties());
        //??????cas???????????????
        casAuthenticationProvider.setTicketValidator(this.casTicketValidationFilter());
        //??????cas??????
        casAuthenticationProvider.setKey(this.casConfigurationProperties.getKey());
        return casAuthenticationProvider;
    }

    /**
     * <p>
     * cas???????????????
     * </p>
     *
     * @return {@link Cas20ServiceTicketValidator}
     * @author ??????
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
     * ????????????????????????????????????cas????????????????????????
     * </p>
     *
     * @return {@link SingleSignOutFilter}
     * @author ??????
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
     * ???????????????????????????????????????cas?????????
     * </p>
     *
     * @return {@link LogoutFilter}
     * @author ??????
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
     * ?????????token
     * </p>
     * spring Security?????????????????????PersistentTokenRepository?????????InMemoryTokenRepositoryImpl??????token??????????????????<br>
     * ????????????JdbcTokenRepositoryImpl???????????????persistent_logins??????token????????????????????????
     *
     * @return {@link PersistentTokenRepository}
     * @author ??????
     * @custom.date 2021/7/1 11:29
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // ??????????????????????????????????????????
        jdbcTokenRepository.setCreateTableOnStartup(false);
        // ???????????????
        jdbcTokenRepository.setDataSource(this.dataSource);
        return jdbcTokenRepository;
    }

    /**
     * <p>
     * ??????session????????????
     * </p>
     *
     * @return {@link SessionRegistry}
     * @author ??????
     * @custom.date 2021/6/24 18:12
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

}
