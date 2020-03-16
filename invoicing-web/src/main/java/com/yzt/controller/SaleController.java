package com.yzt.controller;

import com.yzt.entity.*;
import com.yzt.mapper.SaleMapper;
import com.yzt.service.PurchaseService;
import com.yzt.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sale")
public class SaleController {
    @Resource
    private SaleService saleService;
    @Resource
    private PurchaseService purchaseService;

    private int count;
    private int totalPage;
    private int pageSize = 20;

    @RequestMapping(value = "saleOrder", method = RequestMethod.GET)
    public String toSaleOrder(Model model) {
        List<Map> saleOrders = saleService.selSaleOrder(0, pageSize);
        count = saleService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        //解决时间精确到毫秒的情况
        for (int i = 0; i < saleOrders.size(); i++) {
            Map map = saleOrders.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        model.addAttribute("saleOrders", saleOrders);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/saleOrder";
    }

    @RequestMapping(value = "saleOrder/create", method = RequestMethod.GET)
    public String toAddOrder(Model model) {
        List<Category> categories = purchaseService.selCategory();
        List<Product> productInfo = purchaseService.selByCategoryID(1);
        List<Customer> customers = saleService.selCustomer();
        model.addAttribute("categories", categories);
        model.addAttribute("products", productInfo);
        model.addAttribute("customers", customers);
        return "/sale/addSaleOrder";
    }

    @RequestMapping(value = "saleOrder/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addOrder(@RequestBody List<Map<String, String>> list) {
        SaleOrder saleOrder = new SaleOrder();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderID = sdf2.format(date);
        String orderDate = sdf.format(date);
        saleOrder.setOrderID(orderID);
        saleOrder.setCustomerID(list.get(0).get("customerID"));
        saleOrder.setEmployeeID(6);
        saleOrder.setOrderDate(orderDate);
        saleOrder.setShipCity(list.get(0).get("shipCity"));
        saleOrder.setShipProvince(list.get(0).get("shipProvince"));
        saleOrder.setShipPostalCode(list.get(0).get("shipPostalCode"));
        saleOrder.setShipAddress(list.get(0).get("shipAddress"));
        List<SaleOrderDetail> orderList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            SaleOrderDetail detail = new SaleOrderDetail();
            int quantity = Integer.valueOf(map.get("quantity"));
            double unitPrice = Double.valueOf(map.get("unitPrice").replace("元", ""));
            detail.setOrderID(orderID);
            detail.setProductID(Integer.valueOf(map.get("productID")));
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setTotalPrice(quantity * unitPrice);
            orderList.add(detail);
        }
        int res = saleService.insSaleOrder(saleOrder);
        int res2 = saleService.insSaleOrderDetail(orderList);
        System.out.println(res + " " + res2);
        return ResponseEntity.ok("yes");
    }

    @RequestMapping(value = "orderInfo", method = RequestMethod.GET)
    public String orderInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> saleOrders = saleService.selSaleOrder(pageStart, pageSize);
        //解决时间精确到毫秒的情况
        for (int i = 0; i < saleOrders.size(); i++) {
            Map map = saleOrders.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        model.addAttribute("saleOrders", saleOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/saleOrder";
    }

    @RequestMapping(value = "saleOrder/detail/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> saleDetail(@PathVariable("orderID") String orderID){
        List<Map> maps = saleService.selSaleOrderDetail(orderID);
        return ResponseEntity.ok(maps);
    }

}
