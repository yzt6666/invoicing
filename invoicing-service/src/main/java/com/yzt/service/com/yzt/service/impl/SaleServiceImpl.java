package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Customer;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.SaleOrderDetail;
import com.yzt.mapper.SaleMapper;
import com.yzt.service.SaleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SaleServiceImpl implements SaleService {
    @Resource
    private SaleMapper saleMapper;

    @Override
    public List<Map> selSaleOrder(Integer pageStart, Integer pageSize) {
        return saleMapper.selSaleOrder(pageStart, pageSize);
    }

    @Override
    public Integer selCount() {
        return saleMapper.selCount();
    }

    @Override
    public List<Map> selSaleOrderDetail(String orderID) {
        return saleMapper.selSaleOrderDetail(orderID);
    }

    @Override
    public List<Customer> selCustomer() {
        return saleMapper.selCustomer();
    }

    @Override
    public int insSaleOrder(SaleOrder saleOrder) {
        return saleMapper.insSaleOrder(saleOrder);
    }

    @Override
    public int insSaleOrderDetail(List<SaleOrderDetail> saleOrderDetails) {
        return saleMapper.insSaleOrderDetail(saleOrderDetails);
    }
}
