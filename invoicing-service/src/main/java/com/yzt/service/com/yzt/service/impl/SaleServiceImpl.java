package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Customer;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.SaleOrderDetail;
import com.yzt.mapper.SaleMapper;
import com.yzt.service.SaleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public int insSaleOrder(List<Map<String, String>> list, Integer employeeID) {
        SaleOrder saleOrder = new SaleOrder();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderID = sdf2.format(date);
        String orderDate = sdf.format(date);
        saleOrder.setOrderID(orderID);
        saleOrder.setCustomerID(list.get(0).get("customerID"));
        saleOrder.setEmployeeID(employeeID);
        saleOrder.setOrderDate(orderDate);
        saleOrder.setShipCity(list.get(0).get("shipCity"));
        saleOrder.setShipProvince(list.get(0).get("shipProvince"));
        saleOrder.setShipPostalCode(list.get(0).get("shipPostalCode"));
        saleOrder.setShipAddress(list.get(0).get("shipAddress"));
        saleOrder.setShipName(list.get(0).get("shipName"));
        List<SaleOrderDetail> orderList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            SaleOrderDetail detail = new SaleOrderDetail();
            int quantity = Integer.valueOf(map.get("quantity"));
            double unitPrice = Double.valueOf(map.get("unitPrice").replace("元", ""));
            detail.setOrderID(orderID);
            detail.setProductID(Integer.valueOf(map.get("productID")));
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setTotalPrice(quantity * unitPrice);
            orderList.add(detail);
        }
        saleMapper.insSaleOrder(saleOrder);
        return saleMapper.insSaleOrderDetail(orderList);
    }

    @Override
    public List<Map> selOrderByFlag(String flag, String startDate, String endDate) {
        return saleMapper.selOrderByFlag(flag, startDate, endDate);
    }

    @Override
    public Integer delOrder(String orderID) {
        return saleMapper.delOrder(orderID);
    }

    @Override
    public Integer updOrder(String orderID) {
        String flag = "已退单";
        List<SaleOrderDetail> list = saleMapper.selDetail(orderID);
        saleMapper.updStock(list);
        saleMapper.updProduct(list);
        return saleMapper.updOrder(flag, orderID);
    }

    @Override
    public List<Map> selOrderByID(String orderID) {
        return saleMapper.selOrderByID(orderID);
    }

}
