package com.yzt.service.com.yzt.service.impl;

import com.yzt.entity.Stock;
import com.yzt.mapper.StockMapper;
import com.yzt.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class StockServiceImpl implements StockService {
    @Resource
    private StockMapper stockMapper;

    @Override
    public List<Map> selStock(Integer pageStart, Integer pageSize) {
        return stockMapper.selStock(pageStart, pageSize);
    }

    @Override
    public Integer selCount() {
        return stockMapper.selCount();
    }

    @Override
    public List<Stock> selStockArea() {
        return stockMapper.selStockArea();
    }

    @Override
    public Integer updStockArea(Stock stock) {
        return stockMapper.updStockArea(stock);
    }

    @Override
    public List<Map> selByName(String productName, Integer pageStart, Integer pageSize) {
        return stockMapper.selByName(productName, pageStart, pageSize);
    }

    @Override
    public List<Map> selInboundOrder(String flag) {
        return stockMapper.selInboundOrder(flag);
    }

    @Override
    public List<Map> selOutboundOrder(String flag) {
        return stockMapper.selOutboundOrder(flag);
    }

    @Override
    public Integer updInbound(String flag,String orderID) {
        return stockMapper.updInbound(flag, orderID);
    }

    @Override
    public Integer updOutbound(String flag,String orderID) {
        return stockMapper.updOutbound(flag, orderID);
    }

    @Override
    public List<Map> selInboundByID(String orderID) {
        return stockMapper.selInboundByID(orderID);
    }

    @Override
    public List<Map> selOutboundByID(String orderID) {
        return stockMapper.selOutboundByID(orderID);
    }

    @Override
    public Integer updStockUnit(List<Map> maps) {
        return stockMapper.updStockUnit(maps);
    }

    @Override
    public Integer updOutboundUnit(List<Map> maps) {
        return stockMapper.updOutboundUnit(maps);
    }

    @Override
    public Integer updUnitsOnOrder(List<Map> maps) {
        return stockMapper.updUnitsOnOrder(maps);
    }

}
