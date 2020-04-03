package com.sonia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

/**
 * 直接实现 GrantedAuthority 接口
 */
public class SysRole implements GrantedAuthority {
    private Integer id;
    private String roleName;
    private String roleDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    //标记此处不做该对象的 json 处理
    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
