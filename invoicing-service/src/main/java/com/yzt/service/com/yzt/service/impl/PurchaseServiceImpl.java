package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.*;
import com.yzt.mapper.PurchaseMapper;
import com.yzt.service.PurchaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public Integer insPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseMapper.insPurchaseOrder(purchaseOrder);
    }

    @Override
    public Integer insPurchaseOrderDetail(List<PurchaseOrderDetail> purchaseOrderDetails) {
        return purchaseMapper.insPurchaseOrderDetail(purchaseOrderDetails);
    }

    @Override
    public List<Map> selPurchaseOrder(Integer pageStart, Integer pageSize) {
        return purchaseMapper.selPurchaseOrder(pageStart, pageSize);
    }

    @Override
    public Integer selCount(String flag) {
        return purchaseMapper.selCount(flag);
    }

    @Override
    public List<Map> selByOrderID(String orderID) {
        return purchaseMapper.selByOrderID(orderID);
    }
}
