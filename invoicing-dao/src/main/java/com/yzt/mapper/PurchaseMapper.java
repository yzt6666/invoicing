package com.yzt.mapper;

import com.yzt.entity.*;

import java.util.List;
import java.util.Map;

public interface PurchaseMapper {
    List<Category> selCategory();

    List<Product> selByCategoryID(Integer categoryName);

    List<Supplier> selSupplier();

    Integer insPurchaseOrder(PurchaseOrder purchaseOrder);

    Integer insPurchaseOrderDetail(List<PurchaseOrderDetail> purchaseOrderDetails);

    List<Map> selPurchaseOrder(Integer pageStart, Integer pageSize);

    Integer selCount(String flag);

    List<Map> selByOrderID(String orderID);
}
