package com.yzt.service;

import com.yzt.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Map> allProduct(Integer pageStart, Integer pageSize);

    int dataCount();

    List<Map> selByID(Integer productID);

    Integer insProduct(Product product);

    Integer updProduct(Product product);

    Integer delProduct(Integer productID);

    List<Map> selByProductName(String productName);

    Integer updImg(String productName, String imgPath);

    String selImgPath(Integer productID);
}

