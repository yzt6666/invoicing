package com.yzt.service;

import com.yzt.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> allProduct(Integer pageStart, Integer pageSize);

    int dataCount();

    Product selByID(Integer productID);
}
