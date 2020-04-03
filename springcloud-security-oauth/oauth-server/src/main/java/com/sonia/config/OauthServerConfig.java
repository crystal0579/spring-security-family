package com.sonia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    //数据库连接池对象

    //认证业务对象 不是单点登录的token持有者, 是A系统访问B系统的token持有者

    //授权模式专用对象 如：授权码认证

    //客户端信息来源，对于B系统而言，A系统就是他的客户，但是 是他白名单里的客户吗?


}
