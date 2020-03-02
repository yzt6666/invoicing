package com.yzt.controller;

import com.yzt.entity.Product;
import com.yzt.entity.PurchaseOrder;
import com.yzt.entity.SaleOrder;
import com.yzt.entity.Stock;
import com.yzt.service.SaleService;
import com.yzt.service.StockService;
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
    public ResponseEntity<String> updateArea(@RequestBody Stock stock) {
        Integer res = stockService.updStockArea(stock);
        System.out.println(res);
        System.out.println(stock);
        return ResponseEntity.ok("yes");
    }

    @RequestMapping(value = "toStocktaking/search/{productName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> searchProduct(@PathVariable("productName") String productName) {
        System.out.println(productName);
        List<Map> stock = stockService.selByName(productName, 0, pageSize);
        return ResponseEntity.ok(stock);
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
        List<Map> order = stockService.selInboundOrder("未完成");
        for (int i = 0; i < order.size(); i++) {
            Map map = order.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        return ResponseEntity.ok(order);
    }

    @RequestMapping(value = "stockManage/inbound", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> inboundOrder(@RequestBody PurchaseOrder purchaseOrder) {
        String orderID = purchaseOrder.getOrderID();
        String flag = "已完成";
        List<Map> maps = stockService.selInboundByID(orderID);
        Integer res = stockService.updStockUnit(maps);
        Integer res2 = stockService.updInbound(flag, orderID);

        return ResponseEntity.ok("yes");
    }

    @RequestMapping(value = "stockManage/outbound", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> outbound() {
        List<Map> order = stockService.selOutboundOrder("未完成");
        for (int i = 0; i < order.size(); i++) {
            Map map = order.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        return ResponseEntity.ok(order);
    }

    @RequestMapping(value = "stockManage/outbound", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> outboundOrder(@RequestBody SaleOrder saleOrder) {
        String flag = "已完成";
        String orderID = saleOrder.getOrderID();
        List<Map> maps = stockService.selOutboundByID(orderID);
        Integer res = stockService.updUnitsOnOrder(maps);
        Integer res2 = stockService.updOutboundUnit(maps);
        Integer res3 = stockService.updOutbound(flag, orderID);
        return ResponseEntity.ok("yes");
    }

    @RequestMapping(value = "stockManage/inbound/{orderID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> expandInbound(@PathVariable("orderID") String orderID) {
        List<Map> maps = stockService.selInboundByID(orderID);
        return ResponseEntity.ok(maps);
    }

    @RequestMapping(value = "stockManage/outbound/{orderID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> expandOutbound(@PathVariable("orderID") String orderID) {
        List<Map> maps = stockService.selOutboundByID(orderID);
        return ResponseEntity.ok(maps);
    }
}
