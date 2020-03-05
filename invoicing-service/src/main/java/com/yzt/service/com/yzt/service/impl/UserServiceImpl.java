package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.UserList;
import com.yzt.mapper.UserMapper;
import com.yzt.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserList selUser(String username) {
        return userMapper.selUser(username);
    }
}
