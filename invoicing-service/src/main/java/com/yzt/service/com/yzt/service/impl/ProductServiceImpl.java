package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Product;
import com.yzt.entity.Stock;
import com.yzt.mapper.ProductMapper;
import com.yzt.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Map> allProduct(Integer pageStart, Integer pageSize) {
        return productMapper.allProduct(pageStart, pageSize);
    }

    @Override
    public int dataCount() {
        return productMapper.dataCount();
    }

    @Override
    public List<Map> selByID(Integer productID) {
        return productMapper.selByID(productID);
    }

    @Override
    public Integer insProduct(Product product) {
        product.setUnitsOnOrder(0);
        Stock stock = new Stock();
        stock.setUnitsInStock(0);
        stock.setStockArea("A1");
        productMapper.insProduct(product);
        return productMapper.insStock();
    }

    @Override
    public Integer updProduct(Product product) {
        return productMapper.updProduct(product);
    }

    @Override
    public Integer delProduct(Integer productID) {
        return productMapper.delProduct(productID);
    }

    @Override
    public List<Map> selByProductName(String productName) {
        return productMapper.selByProductName(productName);
    }

    @Override
    public Integer updImg(String productName, String imgPath) {
        return productMapper.updImg(productName, imgPath);
    }

    @Override
    public String selImgPath(Integer productID) {
        return productMapper.selImgPath(productID);
    }

}
