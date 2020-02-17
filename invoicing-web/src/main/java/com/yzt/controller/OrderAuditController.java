package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orderAudit")
public class OrderAuditController {
    @RequestMapping("toSaleAudit")
    public String toSaleAudit() {
        return "/orderAudit/saleAudit";
    }

    @RequestMapping("toPurchaseAudit")
    public String toPurchaseAudit() {
        return "/orderAudit/purchaseAudit";
    }
}
