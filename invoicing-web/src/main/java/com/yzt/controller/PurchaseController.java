package com.yzt.controller;

import com.yzt.entity.*;
import com.yzt.service.PurchaseService;
import com.yzt.util.CommonUtil;
import org.springframework.http.HttpStatus;
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
        count = purchaseService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        CommonUtil.dateConvert(purchaseOrders);
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
        CommonUtil.dateConvert(purchaseOrders);
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
    public ResponseEntity<Void> addOrder(@RequestBody List<Map<String, String>> list) {
        try {
            if (list == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            purchaseService.insPurchaseOrder(list);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "purchaseOrder/{categoryID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Product>> selByCategoryID(@PathVariable("categoryID") Integer categoryID) {
        List<Product> products = purchaseService.selByCategoryID(categoryID);
        return ResponseEntity.ok(products);
    }

    @RequestMapping(value = "purchaseOrder/detail/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> selOrderDetail(@PathVariable("orderID") String orderID) {
        List<Map> maps = purchaseService.selByOrderID(orderID);
        return ResponseEntity.ok(maps);
    }

    @RequestMapping(value = "purchaseOrder/order", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> selOrder(HttpServletRequest req) {
        try {
            String flag = req.getParameter("flag");
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            List<Map> maps = purchaseService.selOrderByFlag(flag, startDate, endDate);
            CommonUtil.dateConvert(maps);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 退单
     * @param map
     * @return
     */
    @RequestMapping(value = "purchaseOrder/order", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> returnOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            purchaseService.updOrder(orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 删除订单
     * @param map
     * @return
     */
    @RequestMapping(value = "purchaseOrder/order", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            purchaseService.delOrder(orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
