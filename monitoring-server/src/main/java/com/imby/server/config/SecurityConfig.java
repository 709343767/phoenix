package com.imby.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
     * <p>
     * 重写configure(HttpSecurity http)的方法，来自定义自己的拦截方法和业务逻辑。
     * </p>
     *
     * @param httpSecurity http安全对象
     * @author 皮锋
     * @custom.date 2020/7/1 15:23
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/images/*", "/fonts/**", "/**/*.png", "/**/*.jpg").permitAll()
                .antMatchers("/", "/login", "/signin").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .rememberMe().rememberMeParameter("remember-me") //其实默认就是remember-me，这里可以指定更换
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")  //退出登录
                .permitAll()
                .and()
                .csrf().disable();

    }

    //@Override
    //public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
    //}
}
