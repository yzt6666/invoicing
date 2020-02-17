package com.yzt.service.com.yzt.service.impl;

import com.yzt.mapper.UserMapper;
import com.yzt.entity.LoginUser;
import com.yzt.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public LoginUser selUser(String username) {
        return userMapper.selUser(username);
    }
}
