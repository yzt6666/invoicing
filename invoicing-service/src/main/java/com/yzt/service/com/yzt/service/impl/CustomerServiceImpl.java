package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Customer;
import com.yzt.mapper.CustomerMapper;
import com.yzt.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> allCustomer(Integer pageStart, Integer pageSize) {
        return customerMapper.allCustomer(pageStart, pageSize);
    }

    @Override
    public int selCount() {
        return customerMapper.selCount();
    }
}
