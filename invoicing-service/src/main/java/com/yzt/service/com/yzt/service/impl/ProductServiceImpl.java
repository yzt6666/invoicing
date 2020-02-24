package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Product;
import com.yzt.mapper.ProductMapper;
import com.yzt.service.ProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Product> allProduct(Integer pageStart, Integer pageSize) {
        return productMapper.allProduct(pageStart, pageSize);
    }

    @Override
    public int dataCount() {
        return productMapper.dataCount();
    }

    @Override
    public Product selByID(Integer productID) {
        return productMapper.selByID(productID);
    }
}
