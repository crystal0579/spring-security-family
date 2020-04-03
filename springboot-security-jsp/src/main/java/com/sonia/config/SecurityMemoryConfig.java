//package com.sonia.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * 两种授权方法，即角色授权和权限授权
// * 代码是hasRole和hasAuthority
// * hasRole：授权代码不需要加ROLE_前缀
// * hasAuthority：设置和使用时，名称保持一至即可
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityMemoryConfig extends WebSecurityConfigurerAdapter {
//
//    /**
//     * 认证用户的来源
//     * @param auth
//     * @throws Exception
//     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        super.configure(auth);
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("{noop}123")
//                .roles("USER");//注意这里千万不要写 USER-COMMON,或者别的之类的，因为它springboot里面有个坑，但凡带前缀都会有问题
//    }
//
//    /**
//     * 释放静态资源，指定资源拦截规则，注意每个 url 前都要带上/， 不然会报
//     * defaultTarget must start with '/' or with 'http(s)' 的异常
//     * 指定自定义认证页面
//     * 指定推出认证配置
//     * csrf配置
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        super.configure(http);   //删掉这句话，不然影响很大，会出现两种登录页面，又不是构造方法非写不可
//        http.authorizeRequests()
//                .antMatchers("/login.jsp","/failer.jsp","/css/**","/img/**", "/plugins/**").permitAll()
//                .antMatchers("/**").hasAnyRole("USER","ADMIN")  //所有请求都必须要有USER 或 ADMIN
//                .anyRequest().authenticated() //剩下所有都必修认证过 所以被拦截的资源一定要写在前面
//
//                .and() // return HttpSecurity http, 必须写，不然下面的话接不下去，也就是说一种类型设置的开启
//                .formLogin()//这句话一定要写，这样才能登录到登陆页面,并且下面的也开启了登录页面的设置
//                .loginPage("/login.jsp") //定制登录页
//                .loginProcessingUrl("/login") //而实际真正处理的页面还是spring security的内核/login方法(所以你的toLogin页面submit时的方法应该写"/login")，只是ui界面变成你的/login.jsp
//                .successForwardUrl("/index.jsp")
//                .failureForwardUrl("/failer.jsp")
//                .permitAll()//一定要写，不然login也会过滤掉
//
//                .and()
//                .logout()  //开启 logout 的配置
//                .logoutUrl("/logout") //可以不写  因为默认也是这样
//                .logoutSuccessUrl("/login.jsp")
//                .invalidateHttpSession(true)
//                .permitAll() //必须要写 表示 logout 可被访问
//
//                .and()
//                .csrf() //开启csrf的设置
//                .disable();//方便测试
//
//    }
//
//
//}
