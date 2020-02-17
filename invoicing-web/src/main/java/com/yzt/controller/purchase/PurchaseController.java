package com.yzt.controller.purchase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {


    @RequestMapping("toPurchaseOrder")
    public String toPurchaseOrder() {
        return "/purchase/purchaseOrder";
    }
}
