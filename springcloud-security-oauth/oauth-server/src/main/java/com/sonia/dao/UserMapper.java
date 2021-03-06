package com.sonia.dao;

import com.sonia.model.SysUser;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {//mybatis-spring-boot-starter.jar允许它可以没有什么extends

    @Select("select * from sys_user where username = #{ username }")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "roles", column = "id", javaType = List.class,
            many = @Many(select = "com.sonia.dao.RoleMapper.findByUid"))
    })
    public SysUser findByUsername(String username);
}
