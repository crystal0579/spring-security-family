package com.sonia.service.impl;

import com.sonia.dao.UserMapper;
import com.sonia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;//本来此处会有编译爆红，因为dao层没有 annotation，但是application上已经加了 MapperScan的路径了

    @Override
    public UserDetails loadUserByUsername(String username ) throws UsernameNotFoundException {
        UserDetails details =  userMapper.findByUsername(username);
        return details;
    }
}
