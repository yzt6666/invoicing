package com.yzt.controller;

import com.yzt.entity.*;
import com.yzt.mapper.SaleMapper;
import com.yzt.service.PurchaseService;
import com.yzt.service.SaleService;
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
@RequestMapping("/sale")
public class SaleController {
    @Resource
    private SaleService saleService;
    @Resource
    private PurchaseService purchaseService;

    private int count;
    private int totalPage;
    private int pageSize = 50;

    @RequestMapping(value = "saleOrder", method = RequestMethod.GET)
    public String toSaleOrder(Model model) {
        List<Map> saleOrders = saleService.selSaleOrder(0, pageSize);
        count = saleService.selCount();
        totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize +1;
        CommonUtil.dateConvert(saleOrders);
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
    public ResponseEntity<Void> addOrder(@RequestBody List<Map<String, String>> list) {
        try {
            if (list == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            System.out.println(list);
            saleService.insSaleOrder(list);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "orderInfo", method = RequestMethod.GET)
    public String orderInfo(Model model, HttpServletRequest req) {
        int page = Integer.valueOf(req.getParameter("currentPage"));
        int pageStart = (page - 1) * pageSize;
        List<Map> saleOrders = saleService.selSaleOrder(pageStart, pageSize);
        CommonUtil.dateConvert(saleOrders);
        model.addAttribute("saleOrders", saleOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("count", count);
        return "/sale/saleOrder";
    }

    @RequestMapping(value = "saleOrder/detail/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<List<Map>> saleDetail(@PathVariable("orderID") String orderID){
        try {
            if (StringUtils.isEmpty(orderID)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> maps = saleService.selSaleOrderDetail(orderID);
            return ResponseEntity.ok(maps);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "saleOrder/order", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map>> selOrder(HttpServletRequest req) {
        try {
            String flag = req.getParameter("flag");
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            List<Map> maps = saleService.selOrderByFlag(flag, startDate, endDate);
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
    @RequestMapping(value = "saleOrder/order", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> returnOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            saleService.updOrder(orderID);
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
    @RequestMapping(value = "saleOrder/order", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> delOrder(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String orderID = map.get("orderID");
            saleService.delOrder(orderID);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
