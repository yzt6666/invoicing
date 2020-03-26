package com.yzt.service;

import com.yzt.entity.Supplier;

import java.util.List;

public interface SupplierService {
    List<Supplier> allSupplier(Integer pageStart, Integer pageSize);

    int selCount();

    List<Supplier> selByCompanyName(String companyName);

    Supplier selBySupplierID(Integer supplierID);

    Integer insSupplier(Supplier supplier);

    Integer updSupplier(Supplier supplier);

    Integer delSupplier(Integer supplierID);
}
