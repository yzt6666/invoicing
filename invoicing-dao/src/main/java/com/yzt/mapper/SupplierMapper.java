package com.yzt.mapper;

import com.yzt.entity.Supplier;

import java.util.List;

public interface SupplierMapper {
    List<Supplier> allSupplier(Integer pageStart, Integer pageSize);

    int selCount();
}
