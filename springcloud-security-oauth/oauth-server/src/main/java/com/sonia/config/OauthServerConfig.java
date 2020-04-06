package com.sonia.config;

import com.sonia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * 它里面内置了7个对象，其实是建议放在 application的启动类里生成，这里为了清晰点，放在了OAuth2的配置类里生成
 * 开启了授权码模式认证，并且为了方便测试，4种模式都允许访问，即数据库oauth_client_details.authorized_grant_types指定
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 1.数据库连接池对象
     * application.yml里只要配置了 spring.datasource 就可以 autowired 了
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 2.认证业务对象 不是单点登录的token持有者, 是A系统访问B系统的token持有者
     */
    @Autowired
    private UserService userService;

    /**
     * 3.授权模式专用对象 如：授权码认证
     * 在 SecurityDbConfig 类中创建了
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 4.客户端信息来源，对于B系统而言，A系统就是他的客户，但是 是他白名单里的客户吗?
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 5.token的保存策略  内存还是数据库
     */
    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 6.授权信息保存策略 A系统能访问我的什么信息范围
     */
    @Bean
    public ApprovalStore approvalStore(){
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 7.授权码模式数据来源，比如：web_server_redirect_uri 客户端的重定向URI数据来源于？硬编码？
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 勾搭 客户端信息的数据来源
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 检查token的策略
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();//允许客户端以form表单的形式传达给你，而不是url里面带上信息
        security.checkTokenAccess("isAuthenticated()");//固定写法，表示一定要认证
    }

    /**
     * OAuth2的主配置信息
     * 即 勾搭上面的 bean
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .userDetailsService(userService) //只有加了这句话，才能使用 refresh_token
                .approvalStore(approvalStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore());
    }

}
