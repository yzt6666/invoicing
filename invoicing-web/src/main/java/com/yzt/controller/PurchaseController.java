package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @RequestMapping("purchases")
    public String toPurchaseOrder() {
        return "/purchase/purchaseOrder";
    }

    @RequestMapping("add")
    public String toAdd() {
        return "/purchase/addOrder";
    }
}
