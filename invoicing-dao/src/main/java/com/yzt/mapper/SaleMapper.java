package com.yzt.mapper;

import com.yzt.entity.Customer;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.SaleOrderDetail;

import java.util.List;
import java.util.Map;

public interface SaleMapper {
    List<Map> selSaleOrder(Integer pageStart, Integer pageSize);

    Integer selCount();

    List<Map> selSaleOrderDetail(String orderID);

    List<Customer> selCustomer();

    int insSaleOrder(SaleOrder saleOrder);

    int insSaleOrderDetail(List<SaleOrderDetail> saleOrderDetails);
}
