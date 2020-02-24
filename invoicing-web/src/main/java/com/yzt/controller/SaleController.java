package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @RequestMapping("toSaleOrder")
    public String toSaleOrder() {
        return "/sale/saleOrder";
    }
}
