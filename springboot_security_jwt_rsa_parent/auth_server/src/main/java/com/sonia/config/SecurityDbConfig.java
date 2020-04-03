package com.sonia.config;

import com.sonia.filter.JwtLoginFilter;
import com.sonia.filter.JwtVerifyFilter;
import com.sonia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 两种授权方法，即角色授权和权限授权
 * 代码是hasRole和hasAuthority,
 * hasRole：授权代码不需要加ROLE_前缀 ，但是 数据库里授权的话，数据库里的数据一定要加
 * hasAuthority：设置和使用时，名称保持一至即可
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)//开启动态权限
public class SecurityDbConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    //用户名密码认证
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //单点登录 token 认证
    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    /**
     * 认证用户的来源
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * 释放静态资源，指定资源拦截规则，注意每个 url 前都要带上/， 不然会报
     * defaultTarget must start with '/' or with 'http(s)' 的异常
     * 指定自定义认证页面
     * 指定推出认证配置
     * csrf配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);   //删掉这句话，不然影响很大，会出现两种登录页面，又不是构造方法非写不可

        //spring security 默认是把csrf放到了最前面或最后面配置，不允许放在中间。
        http.csrf() //开启csrf的设置
                .disable()//方便测试
                .authorizeRequests()
                .antMatchers("/**").hasAnyRole("USER","ADMIN")  //所有请求都必须要有USER 或 ADMIN
                .anyRequest().authenticated() //剩下所有都必修认证过 所以被拦截的资源一定要写在前面

                .and()
                .addFilter(new JwtLoginFilter(super.authenticationManager(), rsaKeyProperties))
                .addFilter(new JwtVerifyFilter(super.authenticationManager(), rsaKeyProperties))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//表示禁用session
    }


}