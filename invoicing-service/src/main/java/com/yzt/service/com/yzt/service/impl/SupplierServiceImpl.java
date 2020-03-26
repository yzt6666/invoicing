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

    @Override
    public List<Supplier> selByCompanyName(String companyName) {
        return supplierMapper.selByCompanyName(companyName);
    }

    @Override
    public Supplier selBySupplierID(Integer supplierID) {
        return supplierMapper.selBySupplierID(supplierID);
    }

    @Override
    public Integer insSupplier(Supplier supplier) {
        return supplierMapper.insSupplier(supplier);
    }

    @Override
    public Integer updSupplier(Supplier supplier) {
        return supplierMapper.updSupplier(supplier);
    }

    @Override
    public Integer delSupplier(Integer supplierID) {
        return supplierMapper.delSupplier(supplierID);
    }
}
