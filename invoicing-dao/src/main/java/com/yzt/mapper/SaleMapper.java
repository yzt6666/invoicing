package com.yzt.mapper;

import com.yzt.entity.Customer;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.SaleOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SaleMapper {
    List<Map> selSaleOrder(Integer pageStart, Integer pageSize);

    Integer selCount();

    List<Map> selSaleOrderDetail(String orderID);

    List<Customer> selCustomer();

    int insSaleOrder(SaleOrder saleOrder);

    int insSaleOrderDetail(List<SaleOrderDetail> saleOrderDetails);

    List<Map> selOrderByFlag(@Param("flag") String flag, @Param("startDate") String startDate, @Param("endDate") String endDate);

    Integer delOrder(String orderID);

    Integer updOrder(String flag, String orderID);

    Integer updStock(List<SaleOrderDetail> list);

    Integer updProduct(List<SaleOrderDetail> list);

    List<SaleOrderDetail> selDetail(String orderID);

    List<Map> selOrderByID(String orderID);
}
