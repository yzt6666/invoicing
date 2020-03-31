package com.yzt.mapper;

import com.yzt.entity.UserList;

import java.util.List;

public interface SystemMapper {
    List<UserList> selAllUser(Integer pageStart, Integer pageSize);

    UserList selByEmployeeID(Integer employeeID);

    Integer selCount();

    Integer updPerms(String perms, Integer employeeID);

    Integer delUser(Integer employeeID);
}
