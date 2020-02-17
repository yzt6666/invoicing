package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockController {
    @RequestMapping("toStocktaking")
    public String toStocktaking() {
        return "/stock/stocktaking";
    }

    @RequestMapping("toInbound")
    public String toInbound() {
        return "/stock/inbound";
    }

    @RequestMapping("toOutbound")
    public String toOutbound() {
        return "/stock/outbound";
    }
}
