package com.yzt.service;

import com.yzt.entity.UserList;

import java.util.List;

public interface SystemService {
    List<UserList> selAllUser(Integer pageStart, Integer pageSize);

    List<String> selByEmployeeID(Integer employeeID);

    Integer updPerms(List<String> list);

    Integer delUser(Integer employeeID);
}
