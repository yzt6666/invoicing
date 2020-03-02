package com.yzt.service;

import com.yzt.entity.Stock;

import java.util.List;
import java.util.Map;

public interface StockService {
    List<Map> selStock(Integer pageStart, Integer pageSize);

    Integer selCount();

    List<Stock> selStockArea();

    Integer updStockArea(Stock stock);

    List<Map> selByName(String productName, Integer pageStart, Integer pageSize);

    List<Map> selInboundOrder(String flag);

    List<Map> selOutboundOrder(String flag);

    Integer updInbound(String flag, String orderID);

    Integer updOutbound(String flag, String orderID);

    List<Map> selInboundByID(String orderID);

    List<Map> selOutboundByID(String orderID);

    Integer updStockUnit(List<Map> maps);

    Integer updOutboundUnit(List<Map> maps);

    Integer updUnitsOnOrder(List<Map> maps);
}
