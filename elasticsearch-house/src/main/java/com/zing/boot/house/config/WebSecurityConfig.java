package com.zing.boot.house.config;

import com.zing.boot.house.security.AuthProvider;
import com.zing.boot.house.security.LoginAuthFailHandler;
import com.zing.boot.house.security.LoginUrlEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Http权限控制
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //资源访问权限
        http.authorizeRequests()
                .antMatchers("/admin/login").permitAll() //管理员登录入口
                .antMatchers("/static/**").permitAll() //静态资源
                .antMatchers("/user/login").permitAll() //用户登录入口
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                .and()
            .formLogin()
                .loginProcessingUrl("/login") //配置角色登录处理入口
                .failureHandler(loginAuthFailHandler())
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout/page")
                .deleteCookies("JSESSIONID")
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint())
                .accessDeniedPage("/403")
                .and()
            .csrf().disable()
            .headers().frameOptions().sameOrigin();
    }

    /**
     * 自定义认证策略
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).eraseCredentials(true);
    }

    @Bean
    public AuthProvider authProvider() {
        return new AuthProvider();
    }

    @Bean
    public LoginUrlEntryPoint urlEntryPoint() {
        return new LoginUrlEntryPoint("/user/login");
    }

    @Bean
    public LoginAuthFailHandler loginAuthFailHandler() {
        return new LoginAuthFailHandler(urlEntryPoint());
    }
}
