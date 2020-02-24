package com.yzt.mapper;

import com.yzt.entity.Product;

import java.util.List;

public interface ProductMapper {
    List<Product> allProduct(Integer pageStart, Integer pageSize);

    int dataCount();

    Product selByID(Integer productID);
}
