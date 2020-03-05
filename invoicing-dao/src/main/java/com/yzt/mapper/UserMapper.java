package com.yzt.mapper;

import com.yzt.entity.UserList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserList selUser(String username);
}
