package com.yzt.service;

import com.yzt.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> allCustomer(Integer pageStart, Integer pageSize);

    int selCount();

    Integer insCustomer(Customer customer);

    Integer updCustomer(Customer customer);

    Integer delCustomer(String customerID);

    List<Customer> selByCompanyName(String companyName);

    Customer selByCustomerID(String customerID);

    List<Customer> selAllCustomer();

    List<Customer> selCustomerByNames(List<String> list);
}
