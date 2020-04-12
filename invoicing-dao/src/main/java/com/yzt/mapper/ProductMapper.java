package com.yzt.mapper;

import com.yzt.entity.Product;
import com.yzt.entity.Stock;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    List<Map> allProduct(Integer pageStart, Integer pageSize);

    int dataCount();

    List<Map> selByID(Integer productID);

    Integer insProduct(Product product);

    Integer updProduct(Product product);

    Integer delProduct(Integer productID);

    Integer insStock();

    List<Map> selByProductName(String productName);

    Integer updImg(String productName, String imgPath);

    String selImgPath(Integer productID);
}
