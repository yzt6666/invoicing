package com.yzt.mapper;

import com.yzt.entity.Customer;

import java.util.List;

public interface CustomerMapper {
    List<Customer> allCustomer(Integer pageStart, Integer pageSize);

    int selCount();
}
