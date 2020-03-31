package com.yzt.mapper;

import com.yzt.entity.Supplier;

import java.util.List;

public interface SupplierMapper {
    List<Supplier> allSupplier(Integer pageStart, Integer pageSize);

    int selCount();

    List<Supplier> selByCompanyName(String companyName);

    Supplier selBySupplierID(Integer supplierID);

    Integer insSupplier(Supplier supplier);

    Integer updSupplier(Supplier supplier);

    Integer delSupplier(Integer supplierID);

    List<Supplier> selSupplierByNames(List<String> list);

    List<Supplier> selAllSupplier();
}
