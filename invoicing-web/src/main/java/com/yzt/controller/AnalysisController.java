package com.yzt.controller;

import com.yzt.entity.Category;
import com.yzt.service.AnalysisService;
import com.yzt.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {
    @Resource
    private AnalysisService analysisService;
    @Resource
    private PurchaseService purchaseService;

    private static final String flag = "已完成";

    @RequestMapping(value = "toSaleAnalysis", method = RequestMethod.GET)
    public String toSaleAnalysis(Model model) {
        List<Category> categories = purchaseService.selCategory();
        List<Map> year = analysisService.selYear();
        List<Map> productRank = analysisService.selProductRank(String.valueOf(year.get(0).get("year")), "1", flag);
        model.addAttribute("categories", categories);
        model.addAttribute("year", year);
        model.addAttribute("productRank", productRank);
        return "/analysis/saleAnalysis";
    }

    @RequestMapping(value = "toProfitAnalysis", method = RequestMethod.GET)
    public String toProfitAnalysis(Model model) {
        List<Map> year = analysisService.selYear();
        model.addAttribute("year", year);
        return "/analysis/profitAnalysis";
    }

    @RequestMapping(value = "toSaleAnalysis/date", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Map>> dateAnalysis(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String year = map.get("year");
            String month = map.get("month");
            List<Map> sale = analysisService.selUnitsOnOrder(year, month, flag);
            return ResponseEntity.ok(sale);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toSaleAnalysis/category", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Map>> categoryAnalysis(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String year = map.get("year");
            String month = map.get("month");
            String categoryID = map.get("categoryID");
            List<Map> sale = analysisService.selCategorySale(year, month, Integer.valueOf(categoryID), flag);
            return ResponseEntity.ok(sale);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "toSaleAnalysis/productRank", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<Map>> productRank(@RequestBody Map<String, String> map) {
        try {
            if (map == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            String year = map.get("year");
            String month = map.get("month");
            List<Map> productRank = analysisService.selProductRank(year, month, flag);
            return ResponseEntity.ok(productRank);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "toProfitAnalysis/profit", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> profit(String year) {
        try {
            if (year == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<Map> pay = analysisService.selPay(year);
            List<Map> profit = analysisService.selProfit(year);
            List<Map<String, Object>> profitList = new ArrayList<>();
            //先初始化连个list，然后将数据put到相应的位置
            for(int i = 1; i <= 12; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("month", i);
                map.put("pay", 0);
                map.put("sale", 0);
                map.put("profit", 0);
                profitList.add(map);
            }
            for (int i = 0; i < pay.size(); i++) {
                int index = (Integer)pay.get(i).get("month");
                Map<String, Object> map = profitList.get(index - 1);
                double payCount = Double.valueOf(pay.get(i).get("pay").toString());
                map.put("profit", Double.valueOf(map.get("profit").toString()) - payCount);
                map.put("pay", payCount);
            }
            for (int i = 0; i < profit.size(); i++) {
                int index = (Integer)profit.get(i).get("month");
                Map<String, Object> map = profitList.get(index - 1);
                double sale = Double.valueOf(profit.get(i).get("profit").toString());
                map.put("profit", Double.valueOf(map.get("profit").toString()) + sale);
                map.put("sale", sale);
            }
            return ResponseEntity.ok(profitList);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
