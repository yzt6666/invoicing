package com.yzt.controller;

import com.yzt.entity.PurchaseOrder;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.Stock;
import com.yzt.service.StockService;
import com.yzt.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stock")
public class StockController {
    @Resource
    private StockService stockService;

    private int count;
    private int totalPage;
    private int pageSize = 20;
    List<Stock> stockArea;
    @RequestMapping(value = "toStocktaking", method = RequestMethod.GET)
    public String toStocktaking(Model model) {
        List<Map> stock = stockService.selStock(0, pageSize);
        stockArea = stockService.selStockArea();
        count = stockService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        model.addAttribute("stock", stock);
        model.addAttribute("currentPage", 1);
        model.addAttribute("stockArea", stockArea);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/stock/stocktaking";
    }

    @RequestMapping(value = "stockInfo", method = RequestMethod.GET)
    public String stockInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> stock = stockService.selStock(pageStart, pageSize);
        model.addAttribute("stock", stock);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        model.addAttribute("stockArea", stockArea);
        return "/stock/stocktaking";
    }

    @RequestMapping(value = "toStocktaking", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updateArea(@RequestBody Stock stock) {
        try {
            if (stock == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            stockService.updStockArea(stock);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toStocktaking/search/{productName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> searchProduct(@PathVariable("productName") String productName) {
        try {
            if (StringUtils.isEmpty(productName)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> stock = stockService.selByName(productName, 0, pageSize);
            return ResponseEntity.ok(stock);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "stockManage", method = RequestMethod.GET)
    public String stockManage(Model model) {
        List<Map> order = stockService.selInboundOrder("未完成");
        for (int i = 0; i < order.size(); i++) {
            Map map = order.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        model.addAttribute("order", order);
        model.addAttribute("count", order.size());
        return "/stock/stockManage";
    }

    @RequestMapping(value = "stockManage/inbound", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> inbound() {
        try {
            List<Map> order = stockService.selInboundOrder("未完成");
            CommonUtil.dateConvert(order);
            return ResponseEntity.ok(order);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "stockManage/inbound", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> inboundOrder(@RequestBody PurchaseOrder purchaseOrder) {
        try {
            if (purchaseOrder == null || StringUtils.isEmpty(purchaseOrder.getOrderID())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = purchaseOrder.getOrderID();
            String flag = "已完成";
            List<Map> maps = stockService.selInboundByID(orderID);
            stockService.updStockUnit(maps);
            stockService.updInbound(flag, orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "stockManage/outbound", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> outbound() {
        try {
            List<Map> order = stockService.selOutboundOrder("未完成");
            CommonUtil.dateConvert(order);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "stockManage/outbound", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> outboundOrder(@RequestBody SaleOrder saleOrder) {
        try {
            if (saleOrder == null || StringUtils.isEmpty(saleOrder.getOrderID())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String flag = "已完成";
            String orderID = saleOrder.getOrderID();
            List<Map> maps = stockService.selOutboundByID(orderID);
            stockService.updUnitsOnOrder(maps);
            stockService.updOutboundUnit(maps);
            stockService.updOutbound(flag, orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "stockManage/inbound/{orderID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> expandInbound(@PathVariable("orderID") String orderID) {
        try {
            if (StringUtils.isEmpty(orderID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> maps = stockService.selInboundByID(orderID);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "stockManage/outbound/{orderID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> expandOutbound(@PathVariable("orderID") String orderID) {
        try {
            if (StringUtils.isEmpty(orderID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> maps = stockService.selOutboundByID(orderID);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
