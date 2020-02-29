package com.yzt.controller;

import com.yzt.entity.*;
import com.yzt.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;

    private int count;
    private int totalPage;
    private int pageSize = 10;

    @RequestMapping(value = "purchaseOrder", method = RequestMethod.GET)
    public String toPurchaseOrder(Model model) {
        List<Map> purchaseOrders = purchaseService.selPurchaseOrder(0, pageSize);
        count = purchaseService.selCount("未到货");
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        //解决时间精确到毫秒的情况
        for (int i = 0; i < purchaseOrders.size(); i++) {
            Map map = purchaseOrders.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/purchase/purchaseOrder";
    }

    @RequestMapping(value = "orderInfo", method = RequestMethod.GET)
    public String orderInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> purchaseOrders = purchaseService.selPurchaseOrder(pageStart, pageSize);
        //解决时间精确到毫秒的情况
        for (int i = 0; i < purchaseOrders.size(); i++) {
            Map map = purchaseOrders.get(i);
            map.put("orderDate", String.valueOf(map.get("orderDate")).replace(".0", ""));
        }
        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/purchase/purchaseOrder";
    }

    @RequestMapping(value = "purchaseOrder/create", method = RequestMethod.GET)
    public String toAdd(Model model) {
        List<Category> categories = purchaseService.selCategory();
        List<Product> productInfo = purchaseService.selByCategoryID(1);
        List<Supplier> suppliers = purchaseService.selSupplier();
        model.addAttribute("categories", categories);
        model.addAttribute("products", productInfo);
        model.addAttribute("suppliers", suppliers);
        return "/purchase/addOrder";
    }

    @RequestMapping(value = "purchaseOrder/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addOrder(@RequestBody List<Map<String, String>> list) {
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat idSdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderDate = sdf.format(date.getTime());
        String orderID = idSdf.format(date);
        String flag = "未到货";
        String remark = list.get(0).get("remark");
        Integer supplierID = Integer.valueOf(list.get(0).get("supplierID"));
        Integer employeeID = 3;
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setEmployeeID(employeeID);
        purchaseOrder.setOrderDate(orderDate);
        purchaseOrder.setOrderID(orderID);
        purchaseOrder.setFlag(flag);
        purchaseOrder.setRemark(remark);
        purchaseOrder.setSupplierID(supplierID);
        Integer res = purchaseService.insPurchaseOrder(purchaseOrder);
        List<PurchaseOrderDetail> orderDetails = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            int quantity = Integer.valueOf(map.get("quantity"));
            double purchasePrice = Double.valueOf(map.get("purchasePrice"));
            detail.setOrderID(orderID);
            detail.setQuantity(quantity);
            detail.setProductID(Integer.valueOf(map.get("productID")));
            detail.setPurchasePrice(purchasePrice);
            detail.setTotalPrice(quantity * purchasePrice);
            orderDetails.add(detail);
        }
        Integer res2 = purchaseService.insPurchaseOrderDetail(orderDetails);
        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "purchaseOrder/{categoryID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Product>> selBycategoryID(@PathVariable("categoryID") Integer categoryID) {
        List<Product> products = purchaseService.selByCategoryID(categoryID);
        return ResponseEntity.ok(products);
    }

    @RequestMapping(value = "purchaseOrder/detail/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> selOrderDetail(@PathVariable("orderID") String orderID) {
        List<Map> maps = purchaseService.selByOrderID(orderID);
        return ResponseEntity.ok(maps);
    }

}
