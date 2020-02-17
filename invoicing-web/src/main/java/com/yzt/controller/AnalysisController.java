package com.yzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    @RequestMapping("toSaleAnalysis")
    public String toSaleAnalysis() {
        return "/analysis/saleAnalysis";
    }

    @RequestMapping("toProfitAnalysis")
    public String toProfitAnalysis() {
        return "/analysis/profitAnalysis";
    }

}
