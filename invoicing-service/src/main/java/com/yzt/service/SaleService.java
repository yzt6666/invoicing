package com.yzt.service;

import com.yzt.entity.Customer;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.SaleOrderDetail;

import java.util.List;
import java.util.Map;

public interface SaleService {
    List<Map> selSaleOrder(Integer pageStart, Integer pageSize);

    Integer selCount();

    List<Map> selSaleOrderDetail(String orderID);

    List<Customer> selCustomer();

    int insSaleOrder(SaleOrder saleOrder);

    int insSaleOrderDetail(List<SaleOrderDetail> saleOrderDetails);
}
