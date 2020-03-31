package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.*;
import com.yzt.mapper.PurchaseMapper;
import com.yzt.service.PurchaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.Subject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Resource
    private PurchaseMapper purchaseMapper;

    @Override
    public List<Category> selCategory() {
        return purchaseMapper.selCategory();
    }

    @Override
    public List<Product> selByCategoryID(Integer categoryName) {
        return purchaseMapper.selByCategoryID(categoryName);
    }

    @Override
    public List<Supplier> selSupplier() {
        return purchaseMapper.selSupplier();
    }

    @Override
    public Integer insPurchaseOrder(List<Map<String, String>> list, Integer employeeID) {
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat idSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderDate = sdf.format(date.getTime());
        String orderID = idSdf.format(date);
        String flag = "未完成";
        String remark = list.get(0).get("remark");
        Integer supplierID = Integer.valueOf(list.get(0).get("supplierID"));
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setEmployeeID(employeeID);
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setOrderID(orderID);
        purchaseOrder.setFlag(flag);
        purchaseOrder.setRemark(remark);
        purchaseOrder.setSupplierID(supplierID);
        purchaseMapper.insPurchaseOrder(purchaseOrder);
        List<PurchaseOrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            int quantity = Integer.valueOf(map.get("quantity"));
            double purchasePrice = Double.valueOf(map.get("purchasePrice"));
            detail.setOrderID(orderID);
            detail.setQuantity(quantity);
            detail.setProductID(Integer.valueOf(map.get("productID")));
            detail.setPurchasePrice(purchasePrice);
            detail.setTotalPrice(quantity * purchasePrice);
            orderDetails.add(detail);
        }
        return purchaseMapper.insPurchaseOrderDetail(orderDetails);
    }

    @Override
    public List<Map> selPurchaseOrder(Integer pageStart, Integer pageSize) {
        return purchaseMapper.selPurchaseOrder(pageStart, pageSize);
    }

    @Override
    public Integer selCount() {
        return purchaseMapper.selCount();
    }

    @Override
    public List<Map> selByOrderID(String orderID) {
        return purchaseMapper.selByOrderID(orderID);
    }

    @Override
    public List<Map> selOrderByFlag(String flag, String startDate, String endDate) {
        return purchaseMapper.selOrderByFlag(flag, startDate, endDate);
    }

    @Override
    public Integer delOrder(String orderID) {
        return purchaseMapper.delOrder(orderID);
    }

    @Override
    public List<Map> selOrderByID(String orderID) {
        return purchaseMapper.selOrderByID(orderID);
    }

    @Override
    public Integer updOrder(String orderID) {
        String flag = "已退单";
        List<PurchaseOrderDetail> list = purchaseMapper.selDetail(orderID);
        purchaseMapper.updStock(list);
        purchaseMapper.updProduct(list);
        return purchaseMapper.updOrder(flag, orderID);
    }

}
