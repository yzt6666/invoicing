package com.yzt.mapper;

import com.yzt.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    List<Product> allProduct(Integer pageStart, Integer pageSize);

    int dataCount();

    List<Map> selByID(Integer productID);
}
