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

    @Override
    public Integer insCustomer(Customer customer) {
        return customerMapper.insCustomer(customer);
    }

    @Override
    public Integer updCustomer(Customer customer) {
        return customerMapper.updCustomer(customer);
    }

    @Override
    public Integer delCustomer(String customerID) {
        return customerMapper.delCustomer(customerID);
    }

    @Override
    public List<Customer> selByCompanyName(String companyName) {
        return customerMapper.selByCompanyName(companyName);
    }

    @Override
    public Customer selByCustomerID(String customerID) {
        return customerMapper.selByCustomerID(customerID);
    }

    @Override
    public List<Customer> selAllCustomer() {
        return customerMapper.selAllCustomer();
    }

    @Override
    public List<Customer> selCustomerByNames(List<String> list) {
        return customerMapper.selCustomerByNames(list);
    }
}
