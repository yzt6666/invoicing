package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.UserList;
import com.yzt.mapper.SystemMapper;
import com.yzt.service.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {
    @Resource
    private SystemMapper systemMapper;
    @Override
    public List<UserList> selAllUser(Integer pageStart, Integer pageSize) {
        return systemMapper.selAllUser(pageStart, pageSize);
    }

    @Override
    public Integer selCount() {
        return systemMapper.selCount();
    }

    @Override
    public List<String> selByEmployeeID(Integer employeeID) {
        UserList userList = systemMapper.selByEmployeeID(employeeID);
        if (userList == null) {
            return null;
        }
        String perms = userList.getPerms();
        String[] split = perms.split(",");
        List<String> list = new ArrayList<>();
        for (String str : split) {
            list.add(str);
        }
        return list;
    }

    @Override
    public Integer updPerms(List<String> list) {
        if (list == null) {
            return null;
        }
        Integer employeeID = Integer.valueOf(list.get(0));
        list.remove(0);
        String perms = StringUtils.join(list.toArray(), ",");
        return systemMapper.updPerms(perms, employeeID);
    }

    @Override
    public Integer delUser(Integer employeeID) {
        return systemMapper.delUser(employeeID);
    }
}
