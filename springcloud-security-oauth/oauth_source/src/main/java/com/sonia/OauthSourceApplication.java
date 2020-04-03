package com.sonia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan("com.sonia.dao")
public class OauthSourceApplication {

    /**
     * application.yml里只要配置了 spring.datasource 就可以 autowired 了
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 指定token的持久化策略
     * 如： 用户在A 系统上 访问 B系统的资源，那么A肯定要传递给B 一个token ,详见印象笔记【spring security-db-boot】文章10
     * @return
     */
    @Bean(name="jdbcTokenStore")
    public TokenStore jdbcTokenStore(){
        return new JdbcTokenStore(dataSource);
    }

    public static void main(String[] args){
        SpringApplication.run(OauthSourceApplication.class, args);
    }
}
