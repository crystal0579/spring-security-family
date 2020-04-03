package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.domain.SysRole;
import com.itheima.domain.SysUser;
import com.itheima.service.RoleService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    /**
     * 因为 spring-security.xml里有引入
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void save(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @Secured({"ROLE_ADMIN"})//有多个，请用逗号间隔 {"ROLE_ADMIN","ROLE_ORDER"}
    // 也可以使用 jsr250注解 @RolesAllowed({"ROLE_ADMIN","ROLE_PRODUCT"})//JSR-250注解
    //也可以使用spring注解 @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')")//spring表达式注解
    public List<SysUser> findAll() {
        return userDao.findAll();
    }

    @Override
    public Map<String, Object> toAddRolePage(Integer id) {
        List<SysRole> allRoles = roleService.findAll();
        List<Integer> myRoles = userDao.findRolesByUid(id);
        Map<String, Object> map = new HashMap<>();
        map.put("allRoles", allRoles);
        map.put("myRoles", myRoles);
        return map;
    }

    @Override
    public void addRoleToUser(Integer userId, Integer[] ids) {
        userDao.removeRoles(userId);
        for (Integer rid : ids) {
            userDao.addRoles(userId, rid);
        }
    }

    /**
     * 这里是 spring UserDetailsService里的方法实现，
     * 它只传入了 username
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            SysUser sysUser = userDao.findByName(username);
            if(sysUser == null){
                return null;
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            //先hardcoding测试
//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            List<SysRole> roles = sysUser.getRoles();
            roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getRoleName())));

            //发现 UserDetails 是一个接口
            //密码先明文测试
            //UserDetails userDetails = new User(sysUser.getUsername(),  "{noop}" + sysUser.getPassword(), authorities);

            //加密时，{noop}可删除
            //简单user
//            UserDetails userDetails = new User(sysUser.getUsername(),  sysUser.getPassword(), authorities);

            //带上用户是否有效等信息
            UserDetails userDetails = new User(sysUser.getUsername(), sysUser.getPassword(),
                    sysUser.getStatus() == 1 ,
                    true,
                    true,
                    true,
                    authorities);

            return userDetails;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
