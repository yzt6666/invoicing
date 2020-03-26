package com.yzt.service;

import com.yzt.entity.*;

import java.util.List;
import java.util.Map;

public interface PurchaseService {
    List<Category> selCategory();

    List<Product> selByCategoryID(Integer categoryName);

    List<Supplier> selSupplier();

    Integer insPurchaseOrder(List<Map<String, String>> maps);

    List<Map> selPurchaseOrder(Integer pageStart, Integer pageSize);

    Integer selCount();

    List<Map> selByOrderID(String orderID);

    List<Map> selOrderByFlag(String flag, String startDate, String endDate);

    Integer delOrder(String orderID);

    Integer updOrder(String orderID);

}
