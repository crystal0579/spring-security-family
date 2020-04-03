package com.itheima.test;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodingTest {
    public static void main(String[] args){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("1"));
        //$2a$10$rLuweYAPxadvpri5M.OOueodriLUweoIGfzVsdOYF9T3LWO3.CDDK
        //$2a$10$r4vpcR7rAuqiJLJeLc8uTus/O0BQbBtGqlYU7.EqI3x4f1OHMyaAO
        //发现每一次都不一样
        //因为 spring security 是一种加盐加密，但是它这个盐每次都不一样，
        //也就是规则加密，但不像MD5，每次规则都是一样的，它的规则是不一样的
    }
}
