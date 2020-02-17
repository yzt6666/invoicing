package com.yzt.service;

import com.yzt.entity.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> allSupplier(Integer pageStart, Integer pageSize);

    int selCount();
}
