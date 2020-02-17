package com.yzt.service;

import com.yzt.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> allCustomer(Integer pageStart, Integer pageSize);

    int selCount();
}
