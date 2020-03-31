package com.yzt.mapper;

import com.yzt.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PurchaseMapper {
    List<Category> selCategory();

    List<Product> selByCategoryID(Integer categoryName);

    List<Supplier> selSupplier();

    Integer insPurchaseOrder(PurchaseOrder purchaseOrder);

    Integer insPurchaseOrderDetail(List<PurchaseOrderDetail> purchaseOrderDetails);

    List<Map> selPurchaseOrder(Integer pageStart, Integer pageSize);

    Integer selCount();

    List<Map> selByOrderID(String orderID);

    List<Map> selOrderByFlag(@Param("flag") String flag, @Param("startDate") String startDate, @Param("endDate") String endDate);

    Integer delOrder(String orderID);

    Integer updOrder(String flag, String orderID);

    Integer updStock(List<PurchaseOrderDetail> list);

    Integer updProduct(List<PurchaseOrderDetail> list);

    List<PurchaseOrderDetail> selDetail(String orderID);

    List<Map> selOrderByID(String orderID);
}
