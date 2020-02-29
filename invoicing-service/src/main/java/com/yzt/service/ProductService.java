package com.yzt.service;

import com.yzt.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> allProduct(Integer pageStart, Integer pageSize);

    int dataCount();

//    Product selByID(Integer productID);
    List<Map> selByID(Integer productID);
}

