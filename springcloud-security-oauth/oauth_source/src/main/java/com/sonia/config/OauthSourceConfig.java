package com.sonia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * 为了方便测试，4种模式都允许访问，即数据库oauth_client_details.authorized_grant_types指定
 */
@Configuration
@EnableResourceServer //开启oauth的资源服务器，查看源码，发现它并不适合写在application的启动类上,因为需要继承和实现某个类
//@EnableResourceServer 类似于 spring-security 的 @EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)//spring security 的动态控制权限，即之前在 springboot-security-jsp 工程里启动类上的标注，配合在 controller 上写 @Secured("ROLE_PRODUCT")
public class OauthSourceConfig extends ResourceServerConfigurerAdapter {

    @Resource //@Autowired是根据类型查找，实现类较多，感觉Resource更安全合适//这个的bean的生成详见启动类
    private TokenStore jdbcTokenStore;

    /**
     * 来指定当前的资源ID和存储方案
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("product_api") //这个可以任意命名 指定该工程的 resourceId,也可以将 resourceId 写入配置文件中，@value引入
                .tokenStore(jdbcTokenStore);//指定token store 为 jdbc
    }

    /**
     * 类似于 spring security 的configure(http)
     * 表明 scope 是 read 还是 write
     * 允许跨域访问的设置
     * 浏览器限制跨域请求一般有两种方式：
     * 1. 浏览器限制发起跨域请求
     * 2. 跨域请求可以正常发起，但是返回的结果被浏览器拦截了
     * 一般浏览器都是第二种方式限制跨域请求，那就是说请求已到达服务器，并有可能对数据库里的数据进行了操作，但是返回的结果被浏览器拦截了，
     * 那么我们就获取不到返回结果，这是一次失败的请求，但是可能对数据库里的数据产生了影响。
     *
     * 为了防止这种情况的发生，规范要求，对这种可能对服务器数据产生副作用的HTTP请求方法，浏览器必须先使用OPTIONS方法发起一个预检请求，
     * 从而获知服务器是否允许该跨域请求：如果允许，就发送带数据的真实请求；如果不允许，则阻止发送带数据的真实请求。
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //指定不同请求方式访问资源所需要的权限，一般查询是read，其余是write。
                //这时候所有资源的访问请求都被 OAuth2的jar控制了，不需要别的额外配置了
                .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")

                .and()
                .headers()
                .addHeaderWriter((request, response) -> {
                    //允许跨域
                    response.addHeader("Access-Control-Allow-Origin", "*");//*这是允许所有外来域都以可以访问， 也可以单指，如 "https://foo.bar.org"
                    //如果是跨域的预检请求，则原封不动地向下传达请求头信息
                    //什么是预检请求，请参考方法上的注释
                    if (request.getMethod().equals("OPTIONS")){
                        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                    }
                });
    }

}
