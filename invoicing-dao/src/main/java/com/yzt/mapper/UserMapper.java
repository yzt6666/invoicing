package com.yzt.mapper;

import com.yzt.entity.LoginUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public LoginUser selUser(String username);
}
