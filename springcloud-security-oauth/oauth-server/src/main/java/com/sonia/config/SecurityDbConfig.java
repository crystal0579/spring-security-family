package com.sonia.config;


import com.sonia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityDbConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;// interface UserService extends UserDetailsService

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 认证用户的来源
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated() //所有页面都需要认证

                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .permitAll()

                .and()
                .csrf()
                .disable();
    }


    /**
     * 将 OAuth2 融入到 spring security 中
     * 授权码模式
     * AuthenticationManager对象在OAuth2认证服务中要使用，提前放入 IOC 容器中， 所以@Bean
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
