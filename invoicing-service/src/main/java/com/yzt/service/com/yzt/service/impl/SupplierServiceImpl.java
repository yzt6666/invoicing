package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Supplier;
import com.yzt.mapper.SupplierMapper;
import com.yzt.service.SupplierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public List<Supplier> allSupplier(Integer pageStart, Integer pageSize) {
        return supplierMapper.allSupplier(pageStart, pageSize);
    }

    @Override
    public int selCount() {
        return supplierMapper.selCount();
    }
}
